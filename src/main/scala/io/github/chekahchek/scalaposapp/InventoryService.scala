package io.github.chekahchek.scalaposapp

import dataclass.InventoryItem
import skunk._
import skunk.implicits._
import skunk.codec.all._
import cats.effect.{IO, Resource}
//import java.util.UUID

//trait InventoryOps[F[_]] {
////  def addItem(item: InventoryItem) : Unit
////  def deleteItem(item: InventoryItem): Unit
////  def modifyItem(item: InventoryItem): Unit
//  def checkStock(session: Session[IO], itemName: String) : F[InventoryItem]
//
//}


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
                  quantity:Integer): IO[String] = for {
    cmd <- session.use{ s => IO(s.prepare(insertByName))}
    _ <- cmd.use(c => c.execute(InventoryItem(name, brand, category, quantity)))
  } yield s"$name has been created"


}
