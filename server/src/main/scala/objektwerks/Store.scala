package objektwerks

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import java.time.Instant

import scalikejdbc.*
import scala.concurrent.duration.FiniteDuration

object Store:
  def cache(minSize: Int,
            maxSize: Int,
            expireAfter: FiniteDuration): Cache[String, String] =
    Scaffeine()
      .initialCapacity(minSize)
      .maximumSize(maxSize)
      .expireAfterWrite(expireAfter)
      .build[String, String]()

final class Store(config: Config,
                  cache: Cache[String, String]) extends LazyLogging:
  private val url = config.getString("db.url")
  private val user = config.getString("db.user")
  private val password = config.getString("db.password")
  private val initialSize = config.getInt("db.initialSize")
  private val maxSize = config.getInt("db.maxSize")
  private val connectionTimeoutMillis = config.getLong("db.connectionTimeoutMillis")

  private val settings = ConnectionPoolSettings(
    initialSize = initialSize,
    maxSize = maxSize,
    connectionTimeoutMillis = connectionTimeoutMillis
  )

  ConnectionPool.singleton(url, user, password, settings)

  def register(account: Account): Account = addAccount(account)

  def login(email: String, pin: String): Option[Account] =
    DB readOnly { implicit session =>
      sql"select * from account where email_address = $email and pin = $pin"
        .map(rs =>
          Account(
            rs.long("id"),
            rs.string("license"),
            rs.string("email_address"),
            rs.string("pin"),
            rs.long("activated"),
            rs.long("deactivated")
          )
        )
        .single()
    }

  def isEmailAddressUnique(emailAddress: String): Boolean =
    val count = DB readOnly { implicit session =>
      sql"select id from account where email_address = $emailAddress"
        .map(rs => rs.long("id"))
        .single()
    }
    if count.isDefined then false else true

  def isAuthorized(license: String): Boolean =
    cache.getIfPresent(license) match
      case Some(_) =>
        logger.debug(s"*** store cache get: $license")
        true
      case None =>
        val optionalLicense = DB readOnly { implicit session =>
          sql"select license from account where license = $license"
            .map(rs => rs.string("license"))
            .single()
        }
        if optionalLicense.isDefined then
          cache.put(license, license)
          logger.debug(s"*** store cache put: $license")
          true
        else false

  def listAccounts(): List[Account] =
    DB readOnly { implicit session =>
      sql"select * from account"
        .map(rs =>
          Account(
            rs.long("id"),
            rs.string("license"),
            rs.string("email_address"),
            rs.string("pin"),
            rs.long("activated"),
            rs.long("deactivated")
          )
        )
        .list()
    }

  def addAccount(account: Account): Account =
    val id = DB localTx { implicit session =>
      sql"insert into account(license, email_address, pin, activated, deactivated) values(${account.license}, ${account.emailAddress}, ${account.pin}, ${account.activated}, ${account.deactivated})"
      .update()
    }
    account.copy(id = id)

  def removeAccount(license: String): Unit =
    DB localTx { implicit session =>
      sql"delete account where license = $license"
      .update()
    }
    ()

  def deactivateAccount(license: String): Option[Account] =
    DB localTx { implicit session =>
      val deactivated = sql"update account set deactivated = ${Instant.now.getEpochSecond}, activated = 0 where license = $license"
      .update()
      if deactivated > 0 then
        sql"select * from account where license = $license"
          .map(rs =>
            Account(
              rs.long("id"),
              rs.string("license"),
              rs.string("email_address"),
              rs.string("pin"),
              rs.long("activated"),
              rs.long("deactivated")
            )
          )
          .single()
      else None
    }

  def reactivateAccount(license: String): Option[Account] =
    DB localTx { implicit session =>
      val activated = sql"update account set activated = ${Instant.now.getEpochSecond}, deactivated = 0 where license = $license"
      .update()
      if activated > 0 then
        sql"select * from account where license = $license"
          .map(rs =>
            Account(
              rs.long("id"),
              rs.string("license"),
              rs.string("email_address"),
              rs.string("pin"),
              rs.long("activated"),
              rs.long("deactivated")
            )
          )
          .single()
      else None
    }

  def listProfiles: List[Profile] = DB readOnly { implicit session =>
    sql"select * from profile order by name"
      .map(rs =>
        Profile(
          rs.long("id"),
          rs.long("account_id"),
          rs.string("name"), 
          rs.long("created"), 
        )
      )
      .list()
  }

  def addProfile(profile: Profile): Long = DB localTx { implicit session =>
    sql"insert into profile(account_id, name, created) values(${profile.accountId}, ${profile.name}, ${profile.created})"
      .updateAndReturnGeneratedKey()
  }

  def updateProfile(profile: Profile): Long = DB localTx { implicit session =>
    sql"update profile set name = ${profile.name} where id = ${profile.id}"
      .update()
    profile.id
  }

  def listEdibles(profileId: Long): List[Edible] = DB readOnly { implicit session =>
    sql"select * from edible where profile_id = $profileId order by ate desc"
      .map(rs =>
        Edible(
          rs.long("id"),
          rs.long("profile_id"),
          rs.string("kind"),
          rs.string("detail"),
          rs.boolean("organic"),
          rs.int("calories"),
          rs.long("ate")
        )
      )
      .list()
  }

  def addEdible(edible: Edible): Long = DB localTx { implicit session =>
    sql"""
       insert into edible(profile_id, kind, detail, organic, calories, ate)
       values(${edible.profileId}, ${edible.kind}, ${edible.detail}, ${edible.organic}, ${edible.calories}, ${edible.ate})
       """
      .updateAndReturnGeneratedKey()
  }

  def updateEdible(edible: Edible): Long = DB localTx { implicit session =>
    sql"""
      update edible set kind = ${edible.kind}, detail = ${edible.detail}, organic = ${edible.organic},
      calories = ${edible.calories}, ate = ${edible.ate} where id = ${edible.id}
      """
      .update()
    edible.id
  }

  def listDrinkables(profileId: Long): List[Drinkable] = DB readOnly { implicit session =>
    sql"select * from drinkable where profile_id = $profileId order by drank desc"
      .map(rs =>
        Drinkable(
          rs.long("id"),
          rs.long("profile_id"),
          rs.string("kind"),
          rs.string("detail"),
          rs.boolean("organic"),
          rs.int("count"),
          rs.int("calories"),
          rs.long("ate")
        )
      )
      .list()
  }

  def addDrinkable(drinkable: Drinkable): Long = DB localTx { implicit session =>
    sql"""
       insert into drinkable(profile_id, kind, detail, organic, count, calories, drank)
       values(${drinkable.profileId}, ${drinkable.kind}, ${drinkable.detail}, ${drinkable.organic},
       ${drinkable.count}, ${drinkable.calories}, ${drinkable.drank})
       """
      .updateAndReturnGeneratedKey()
  }