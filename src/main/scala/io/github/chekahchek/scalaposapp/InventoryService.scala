package io.github.chekahchek.scalaposapp

import dataclass.InventoryItem
import skunk._
import skunk.implicits._
import skunk.codec.all._
import cats.effect.{IO, Resource}
import skunk.data.Completion.{Delete, Insert}


class InventoryService(session: Resource[IO, Session[IO]]) {

  private val codec: Codec[InventoryItem] =
    (varchar ~ varchar ~ varchar ~ int4).imap {
      case name ~ brand ~ category ~ quantity => InventoryItem(name, brand, category, quantity)
    }(item => item.name ~ item.brand ~ item.category ~ item.quantity)
    
  private val selectByName: Query[String, InventoryItem] =
    sql"""
      SELECT * FROM inventory
      WHERE name = $varchar
      LIMIT 1
       """.query(codec)

  def checkStock(itemName: String): IO[Option[InventoryItem]] = {
      session.use { s =>
        s.prepare(selectByName).use {_.option(itemName)}
      }
    }

  private val insertByName: Command[InventoryItem] =
    sql"""
       INSERT INTO inventory
       VALUES ($codec)
       """.command

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


  private val deleteByName: Command[String] =
    sql"""DELETE FROM inventory
         WHERE name = $varchar
       """.command

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

// Takes in 2 input (String, InventoryItem) and return a String
// contramap transform input data into format expected by query
// ${params} references the item in the contramap in the order specified
private val updateByName: Query[String ~ InventoryItem, String] =
  sql"""
    UPDATE inventory
    SET name = ${varchar}, brand = ${varchar}, category = ${varchar}, quantity = ${int4}
    WHERE name = ${varchar}
    RETURNING name
  """.query(varchar).contramap {
    case itemName ~ InventoryItem(name, brand, category, quantity) =>
      name ~ brand ~ category ~ quantity ~ itemName
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
