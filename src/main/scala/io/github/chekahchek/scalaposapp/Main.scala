package io.github.chekahchek.scalaposapp

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = ScalaposappServer.run[IO]
}
