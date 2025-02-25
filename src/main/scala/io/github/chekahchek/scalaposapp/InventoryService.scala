package io.github.chekahchek.scalaposapp

import dataclass.InventoryItem
import skunk._
import skunk.implicits._
import skunk.codec.all._
import cats.effect.{IO, Resource}

//trait InventoryOps[F[_]] {
////  def addItem(item: InventoryItem) : Unit
////  def deleteItem(item: InventoryItem): Unit
////  def modifyItem(item: InventoryItem): Unit
//  def checkStock(session: Session[IO], itemName: String) : F[InventoryItem]
//
//}


class InventoryService(session: Resource[IO, Session[IO]]) {

  private val codec: Codec[InventoryItem] =
    (uuid ~ varchar ~ varchar ~ varchar ~ int4).imap {
      case id ~ name ~ brand ~ category ~ quantity => InventoryItem(id, name, brand, category, quantity)
    }(item => item.id ~ item.name ~ item.brand ~ item.category ~ item.quantity)
    
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

}
