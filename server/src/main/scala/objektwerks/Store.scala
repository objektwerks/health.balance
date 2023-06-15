package objektwerks

import com.github.blemale.scaffeine.{Cache, Scaffeine}
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import java.time.LocalDate

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
      val deactivated = sql"update account set deactivated = ${LocalDate.now.toEpochDay}, activated = 0 where license = $license"
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
      val activated = sql"update account set activated = ${LocalDate.now.toEpochDay}, deactivated = 0 where license = $license"
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