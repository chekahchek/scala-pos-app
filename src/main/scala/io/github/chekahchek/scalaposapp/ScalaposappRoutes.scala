package io.github.chekahchek.scalaposapp

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

import org.http4s.dsl.impl._
import org.http4s.circe._
//import io.circe.generic.auto._
import io.circe.syntax._



object InventoryQueryParamMatcher extends QueryParamDecoderMatcher[String]("inventory")

object ScalaposappRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  // HttpRoutes = Type alias for Request => F[Option[Response]]
  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._

    // Each case statement represents a route and matches a Request object
    // Deconstructor is used to extract HTTP method (GET/POST etc.) and its Path to perform pattern matching
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting) // Create a Response with 200
        } yield resp
    }
  }

  def inventoryRoutes[F[_]: Sync]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
//    import InventoryService._

    HttpRoutes.of[F] {
      case GET -> Root / "quantity" :? InventoryQueryParamMatcher(inventory) =>
        InventoryService.checkStock(inventory) match {
          case Some(item) => Ok(item.quantity.asJson)
          case _ => NotFound(s"$inventory has no stock")
        }
    }
  }


}