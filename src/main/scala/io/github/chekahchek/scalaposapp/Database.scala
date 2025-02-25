package io.github.chekahchek.scalaposapp

import cats.effect.{IO, Resource}
import io.github.chekahchek.scalaposapp.dataclass.DbConfig
import skunk.Session
import natchez.Trace
import natchez.Trace.Implicits.noop

object Database {
  def pooledSession(config: DbConfig)(implicit trace: Trace[IO]): Resource[IO, Resource[IO, Session[IO]]] = {
    Session.pooled[IO](
      host = config.host,
      port = config.port,
      user = config.username,
      password = Some(config.password),
      database = config.database,
      max = 3
    )
  }

  def createDbSession(config: DbConfig): Resource[IO, Resource[IO, Session[IO]]] = {
    for {
      session <- Database.pooledSession(config)
    } yield session
  }
}
