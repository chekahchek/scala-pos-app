package scalaposapp

import dataclass.InventoryItem
import skunk._
import skunk.implicits._
import skunk.codec.all._

object Codec {

  val codec: Codec[InventoryItem] =
    (varchar ~ varchar ~ varchar ~ int4).imap {
      case name ~ brand ~ category ~ quantity => InventoryItem(name, brand, category, quantity)
    }(item => item.name ~ item.brand ~ item.category ~ item.quantity)

  val insertByName: Command[InventoryItem] =
    sql"""
       INSERT INTO inventory
       VALUES ($codec)
       """.command

  val selectByName: Query[String, InventoryItem] =
    sql"""
      SELECT * FROM inventory
      WHERE name = $varchar
      LIMIT 1
       """.query(codec)


  // Takes in 2 input (String, InventoryItem) and return a String
  // contramap transform input data into format expected by query
  // ${params} references the item in the contramap in the order specified
  val updateByName: Query[String ~ InventoryItem, String] =
    sql"""
    UPDATE inventory
    SET name = ${varchar}, brand = ${varchar}, category = ${varchar}, quantity = ${int4}
    WHERE name = ${varchar}
    RETURNING name
  """.query(varchar).contramap {
      case itemName ~ InventoryItem(name, brand, category, quantity) =>
        name ~ brand ~ category ~ quantity ~ itemName
    }

  val deleteByName: Command[String] =
    sql"""DELETE FROM inventory
         WHERE name = $varchar
       """.command


}
