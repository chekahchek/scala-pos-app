package io.github.chekahchek.scalaposapp

import cats.data.Kleisli
import cats.effect._
import io.github.chekahchek.scalaposapp.dataclass.DbConfig
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.blaze.BlazeServerBuilder
import skunk.Session
import pureconfig.ConfigSource
import natchez.Trace.Implicits.noop
import org.http4s.{Request, Response}
import pureconfig.generic.auto._

object Main extends IOApp {

  import scala.concurrent.ExecutionContext.global

  def createSession(config: DbConfig): Resource[IO, Session[IO]] =
    Session.single(
      host = config.host,
      port = config.port,
      user = config.username,
      password = Some(config.password),
      database = config.database,
    )

  def createRouter(session: Session[IO]): Kleisli[IO, Request[IO], Response[IO]] = Router(
    "/api" -> Routes.inventoryRoutes(session)
  ).orNotFound

  //  override def run(args: List[String]): IO[ExitCode] = {
  def runServer: Resource[IO, ExitCode] = {
    val config: DbConfig = ConfigSource.default.at("db").loadOrThrow[DbConfig]


    for {
      session <- createSession(config)
      _ <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "localhost")
        .withHttpApp(createRouter(session))
        .resource
    } yield ExitCode.Success
    }


  override def run(args: List[String]): IO[ExitCode] =
    runServer.use(_ => IO.never).as(ExitCode.Success)
}