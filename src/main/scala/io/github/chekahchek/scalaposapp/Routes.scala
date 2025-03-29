package io.github.chekahchek.scalaposapp

import cats.effect.IO
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes
import org.http4s.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import dataclass.InventoryItem


class Routes(repository: InventoryService) extends Http4sDsl[IO] {
  object InventoryQueryParamMatcher extends QueryParamDecoderMatcher[String]("inventory")

  val routes = HttpRoutes.of[IO] {

      // Create
      case req @ POST -> Root / "insert" =>
      for {
        stock <- req.decodeJson[InventoryItem]
        results <- repository.createStock(stock.name, stock.brand, stock.category, stock.quantity).flatMap {
          case Right(_) => Created()
          case Left(_) => BadRequest()
        }
      } yield results

      // Read
      case GET -> Root / "quantity" :? InventoryQueryParamMatcher(inventory) =>
        repository.checkStock(inventory).flatMap {
          case Some(item) => Ok(item.quantity.asJson)
          case _ => NotFound (s"$inventory has no stock")
        }

      // Update
      case req @ PUT -> Root / itemName =>
        for {
          stock <- req.decodeJson[InventoryItem]
          results <- repository.updateStock(itemName, stock).flatMap{
            case Right(_) => Ok("Updated")
            case Left(_) => NotFound()
          }
        } yield results

      // Delete
      case DELETE -> Root / itemName =>
        repository.deleteStock(itemName).flatMap{
          case Right(_) => NoContent()
          case Left(_) => NotFound()
        }

  }
    }


