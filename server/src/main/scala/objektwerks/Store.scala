package objektwerks

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariDataSource

import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.sql.DataSource

import scala.concurrent.duration.FiniteDuration

import scalikejdbc.*

object Store:
  def apply(config: Config) = new Store( cache(config), dataSource(config) )

  def cache(config: Config): Cache[String, String] =
    Scaffeine()
      .initialCapacity(config.getInt("cache.initialSize"))
      .maximumSize(config.getInt("cache.maxSize"))
      .expireAfterWrite( FiniteDuration( config.getLong("cache.expireAfter"), TimeUnit.HOURS) )
      .build[String, String]()

  def dataSource(config: Config): DataSource =
    val ds = HikariDataSource()
    ds.setDataSourceClassName(config.getString("db.driver"))
    ds.addDataSourceProperty("url", config.getString("db.url"))
    ds.addDataSourceProperty("user", config.getString("db.user"))
    ds.addDataSourceProperty("password", config.getString("db.password"))
    ds

final class Store(cache: Cache[String, String],
                  dataSource: DataSource):
  ConnectionPool.singleton( DataSourceConnectionPool(dataSource) )

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
      case Some(_) => true
      case None =>
        val optionalLicense = DB readOnly { implicit session =>
          sql"select license from account where license = $license"
            .map(rs => rs.string("license"))
            .single()
        }
        if optionalLicense.isDefined then
          cache.put(license, license)
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
      sql"""
         insert into account(license, email_address, pin, activated, deactivated) values(${account.license},
         ${account.emailAddress}, ${account.pin}, ${account.activated}, ${account.deactivated})
         """
      .updateAndReturnGeneratedKey()
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

  def listProfiles(): List[Profile] =
    DB readOnly { implicit session =>
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

  def addProfile(profile: Profile): Long =
    DB localTx { implicit session =>
      sql"insert into profile(account_id, name, created) values(${profile.accountId}, ${profile.name}, ${profile.created})"
        .updateAndReturnGeneratedKey()
    }

  def updateProfile(profile: Profile): Long =
    DB localTx { implicit session =>
      sql"update profile set name = ${profile.name} where id = ${profile.id}"
        .update()
      profile.id
    }

  def listEdibles(profileId: Long): List[Edible] =
    DB readOnly { implicit session =>
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

  def addEdible(edible: Edible): Long =
    DB localTx { implicit session =>
      sql"""
        insert into edible(profile_id, kind, detail, organic, calories, ate)
        values(${edible.profileId}, ${edible.kind}, ${edible.detail}, ${edible.organic}, ${edible.calories}, ${edible.ate})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateEdible(edible: Edible): Long =
    DB localTx { implicit session =>
      sql"""
        update edible set kind = ${edible.kind}, detail = ${edible.detail}, organic = ${edible.organic},
        calories = ${edible.calories}, ate = ${edible.ate} where id = ${edible.id}
        """
        .update()
      edible.id
    }

  def listDrinkables(profileId: Long): List[Drinkable] =
    DB readOnly { implicit session =>
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
            rs.long("drank")
          )
        )
        .list()
    }

  def addDrinkable(drinkable: Drinkable): Long =
    DB localTx { implicit session =>
      sql"""
        insert into drinkable(profile_id, kind, detail, organic, count, calories, drank)
        values(${drinkable.profileId}, ${drinkable.kind}, ${drinkable.detail}, ${drinkable.organic},
        ${drinkable.count}, ${drinkable.calories}, ${drinkable.drank})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateDrinkable(drinkable: Drinkable): Long =
    DB localTx { implicit session =>
      sql"""
        update drinkable set kind = ${drinkable.kind}, detail = ${drinkable.detail}, organic = ${drinkable.organic},
        count = ${drinkable.count}, calories = ${drinkable.calories}, drank = ${drinkable.drank} where id = ${drinkable.id}
        """
        .update()
      drinkable.id
    }

  def listExpendables(profileId: Long): List[Expendable] =
    DB readOnly { implicit session =>
      sql"select * from expendable where profile_id = $profileId order by finish desc"
        .map(rs =>
          Expendable(
            rs.long("id"),
            rs.long("profile_id"),
            rs.string("kind"),
            rs.string("detail"),
            rs.boolean("sunshine"),
            rs.boolean("freshair"),
            rs.int("calories"),
            rs.long("start"),
            rs.long("finish")
          )
        )
        .list()
    }

  def addExpendable(expendable: Expendable): Long =
    DB localTx { implicit session =>
      sql"""
        insert into expendable(profile_id, kind, detail, sunshine, freshair, calories, start, finish)
        values(${expendable.profileId}, ${expendable.kind}, ${expendable.detail}, ${expendable.sunshine},
        ${expendable.freshair}, ${expendable.calories}, ${expendable.start}, ${expendable.finish})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateExpendable(expendable: Expendable): Long =
    DB localTx { implicit session =>
      sql"""
        update expendable set kind = ${expendable.kind}, detail = ${expendable.detail}, sunshine = ${expendable.sunshine},
        freshair = ${expendable.freshair}, calories = ${expendable.calories}, start = ${expendable.start}, finish = ${expendable.finish} 
        where id = ${expendable.id}
        """
        .update()
      expendable.id
    }

  def listMeasurables(profileId: Long): List[Measurable] =
    DB readOnly { implicit session =>
      sql"select * from measurable where profile_id = $profileId order by measured desc"
        .map(rs =>
          Measurable(
            rs.long("id"),
            rs.long("profile_id"),
            rs.string("kind"),
            rs.int("measurement"),
            rs.long("measured")
          )
        )
        .list()
    }

  def addMeasurable(measurable: Measurable): Long =
    DB localTx { implicit session =>
      sql"""
        insert into measurable(profile_id, kind, measurement, measured)
        values(${measurable.profileId}, ${measurable.kind}, ${measurable.measurement}, ${measurable.measured})
        """
        .updateAndReturnGeneratedKey()
    }

  def updateMeasurable(measurable: Measurable): Long =
    DB localTx { implicit session =>
      sql"""
        update measurable set kind = ${measurable.kind}, measurement = ${measurable.measurement}, measured = ${measurable.measured}
        where id = ${measurable.id}
        """
        .update()
      measurable.id
    }

  def listFaults(): List[Fault] =
    DB readOnly { implicit session =>
      sql"select * from fault order by occurred desc"
        .map(rs =>
          Fault(
            rs.string("cause"),
            rs.long("occurred").toString
          )
        )
        .list()
    }

  def addFault(fault: Fault): Fault =
    DB localTx { implicit session =>
      sql"""
        insert into fault(cause, occurred) values(${fault.cause}, ${Entity.instantAsStringToEpochMilli(fault.occurred)})
        """
        .update()
      fault
    }