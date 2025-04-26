package scalaposapp

import dataclass.InventoryItem
import skunk._
import skunk.implicits._
import cats.effect.{IO, Resource}
import skunk.data.Completion.{Delete, Insert}
import scalaposapp.Codec._


class InventoryService(session: Resource[IO, Session[IO]]) {

  def checkStock(itemName: String): IO[Option[InventoryItem]] = {
      session.use { s =>
        s.prepare(selectByName).use {_.option(itemName)}
      }
    }

  def createStock(name: String,
                  brand: String,
                  category: String,
                  quantity:Integer): IO[Either[Unit, String]] =
    session.use { s =>
      s.prepare(insertByName).use {cmd =>
        cmd.execute(InventoryItem(name, brand, category, quantity)).map{
          case Insert(1) => Right(name)
          case _ => Left(())
        }
      }
    }

  def deleteStock(name: String): IO[Either[Unit, String]] = {
    session.use { s =>
      s.prepare(deleteByName).use { cmd =>
          cmd.execute(name).map {
            case Delete(1) => Right(name)
            case _         => Left(())
          }
        }
      }
  }

  def updateStock(itemName: String, item: InventoryItem): IO[Either[Unit, String]] = {
    session.use { s =>
      s.prepare(updateByName).use { cmd =>
        cmd.option(itemName ~ item).map {
          case Some(name) => Right(name)
          case None => Left(())
        }
      }
    }
  }

}
