package io.github.chekahchek.scalaposapp

import dataclass.InventoryItem

trait InventoryOps[F[_]] {
//  def addItem(item: InventoryItem) : Unit
//  def deleteItem(item: InventoryItem): Unit
//  def modifyItem(item: InventoryItem): Unit
  def checkStock(itemName: String) : F[InventoryItem]

}


object InventoryService extends InventoryOps[Option] {
  val aInventory: InventoryItem = InventoryItem("Fruits", "Apple", 100)
  val inventories: Map[String, InventoryItem] = Map(aInventory.name -> aInventory)

  def checkStock(itemName: String): Option[InventoryItem] = inventories.get(itemName)

}
