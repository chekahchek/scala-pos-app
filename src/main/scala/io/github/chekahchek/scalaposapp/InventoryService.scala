package io.github.chekahchek.scalaposapp

import dataclass.InventoryItem

trait InventoryOps[F[_]] {
//  def addItem(item: InventoryItem) : Unit
//  def deleteItem(item: InventoryItem): Unit
//  def modifyItem(item: InventoryItem): Unit
  def checkStock(itemName: String) : F[InventoryItem]

}


object InventoryService extends InventoryOps[Option] {
  private val uuid = java.util.UUID.randomUUID
  val aInventory: InventoryItem = InventoryItem(uuid, "Apple", "Fruits&Co.", "Fruits", 100)
  val inventories: Map[String, InventoryItem] = Map(aInventory.name -> aInventory)

  def checkStock(itemName: String): Option[InventoryItem] = inventories.get(itemName)

}
