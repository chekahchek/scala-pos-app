package scalaposapp

import cats.effect._
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import Database._
import scalaposapp.dataclass.DbConfig

object Main extends IOApp {

  import scala.concurrent.ExecutionContext.global

  def runServer: Resource[IO, ExitCode] = {
    val config: DbConfig = ConfigSource.default.at("db").loadOrThrow[DbConfig]

    for {
      session <- createDbSession(config)
      repository = new InventoryService(session)
      _ <- BlazeServerBuilder[IO](global)
        .bindHttp(8080, "localhost")
        .withHttpApp(new Routes(repository).routes.orNotFound)
        .resource
    } yield ExitCode.Success
    }


  override def run(args: List[String]): IO[ExitCode] =
    runServer.use(_ => IO.never).as(ExitCode.Success)
}
