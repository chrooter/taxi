package config

import com.google.inject.{AbstractModule, Provides}
import com.mohiva.play.silhouette.api.util.{PasswordHasher, Clock}
import com.mohiva.play.silhouette.api.{Environment, EventBus}
import com.mohiva.play.silhouette.impl.authenticators.{JWTAuthenticator, JWTAuthenticatorService, JWTAuthenticatorSettings}
import com.mohiva.play.silhouette.impl.util.{BCryptPasswordHasher, SecureRandomIDGenerator}
import models.generated.Tables.SystemUser
import net.codingwell.scalaguice.ScalaModule
import play.api.Play
import service.auth.{LoginInfoServiceImpl, LoginInfoService}

import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global

class SilhouetteModule extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[PasswordHasher].to[BCryptPasswordHasher].in[Singleton]
    bind[LoginInfoService].to[LoginInfoServiceImpl]
  }

  @Provides
  def provideSettings(): JWTAuthenticatorSettings = {
    JWTAuthenticatorSettings(
      headerName = Play.configuration.getString("silhouette.authenticator.headerName").getOrElse { "X-Auth-Token" },
      issuerClaim = Play.configuration.getString("silhouette.authenticator.issueClaim").getOrElse { "play-silhouette" },
      encryptSubject = Play.configuration.getBoolean("silhouette.authenticator.encryptSubject").getOrElse { true },
      authenticatorIdleTimeout = Play.configuration.getInt("silhouette.authenticator.authenticatorIdleTimeout"),
      authenticatorExpiry = Play.configuration.getInt("silhouette.authenticator.authenticatorExpiry").getOrElse { 12 * 60 * 60 },
      sharedSecret = Play.configuration.getString("application.secret").get
    )
  }

  @Provides
  def provideAuthenticatorService(settings: JWTAuthenticatorSettings): JWTAuthenticatorService = {
    new JWTAuthenticatorService(settings, None, new SecureRandomIDGenerator, Clock())
  }

  @Provides
  def provideEnvironment(
    userService: LoginInfoService,
    authenticatorService: JWTAuthenticatorService,
    eventBus: EventBus): Environment[SystemUser, JWTAuthenticator] = {

    Environment(userService, authenticatorService, Seq(), eventBus)
  }
}
