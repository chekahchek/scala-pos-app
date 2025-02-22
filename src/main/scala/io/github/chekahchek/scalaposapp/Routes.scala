package io.github.chekahchek.scalaposapp

import cats.effect.IO
import org.http4s.circe._
import org.http4s._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import skunk.Session

object Routes {

  object InventoryQueryParamMatcher extends QueryParamDecoderMatcher[String]("inventory")

  def inventoryRoutes(session: Session[IO]): HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]
    import dsl._

    HttpRoutes.of[IO] {
      case GET -> Root / "quantity" :? InventoryQueryParamMatcher(inventory) =>
        InventoryService.checkStock(inventory) match {
          case Some(item) => Ok(item.quantity.asJson)
          case _ => NotFound(s"$inventory has no stock")
        }
    }
  }

}