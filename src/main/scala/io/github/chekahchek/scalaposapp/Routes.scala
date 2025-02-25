package io.github.chekahchek.scalaposapp

import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.circe._
import io.circe.syntax._


class Routes(repository: InventoryService) extends Http4sDsl[IO] {
  object InventoryQueryParamMatcher extends QueryParamDecoderMatcher[String]("inventory")

  val routes = HttpRoutes.of[IO] {
      case GET -> Root / "quantity" :? InventoryQueryParamMatcher(inventory) =>

        repository.checkStock(inventory).flatMap {
          case Some(item) => Ok(item.quantity.asJson)
          case _ => NotFound (s"$inventory has no stock")
        }
  }
    }


