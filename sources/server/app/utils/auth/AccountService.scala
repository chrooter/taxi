package utils.auth

import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.api.services.IdentityService
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import com.mohiva.play.silhouette.impl.services.DelegableAuthInfoService
import models.generated.Tables.Account
import utils.AccountAlreadyExistsException
import utils.db.DbAccessor
import utils.db.repo.AccountRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait AccountService extends DbAccessor {
  def hasUsers: Future[Boolean]
  def createAccount(user: Account): Future[Account]
}

class AccountServiceImpl(passwordHasher: PasswordHasher,
                      authInfoService: DelegableAuthInfoService,
                       identityService: IdentityService[Account]) extends AccountService {
  def hasUsers: Future[Boolean] = Future {
    withDb { session => AccountRepo.isEmpty(session) }
  }

  def createAccount(user: Account): Future[Account] = {
    // тут мы должны сделать хеш пароля и сохранить его
    val loginInfo = LoginInfo(CredentialsProvider.ID, user.login)
    identityService.retrieve(loginInfo) flatMap {
      case None =>
        // пользователя с таким логином в БД нет
        // хешируем пароль
        val authInfo = passwordHasher.hash(user.passwordHash)
        Future {
          // сохраняем пользователя
          val id = withDb { session => AccountRepo.create(user.copy(passwordHash = ""))(session) }
          user.copy(id = id)
        } flatMap { u =>
          // сохраняем хешированный пароль
          authInfoService.save(loginInfo, authInfo) map { _ => u }
        }
      case Some(u) =>
        // пользователь уже существует
        throw new AccountAlreadyExistsException(user.login)
    }
  }
}


