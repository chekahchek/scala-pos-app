package io.github.chekahchek.scalaposapp.dataclass

import java.util.UUID

case class InventoryItem(id: UUID,
                         name: String,
                         brand: String,
                         category: String,
                         quantity: Int
                        )