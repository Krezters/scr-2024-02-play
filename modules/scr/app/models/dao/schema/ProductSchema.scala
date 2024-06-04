package models.dao.schema

import models.dao.entities.{Product, ProductItem}
import org.squeryl.{PrimitiveTypeMode, Schema, Table}

object ProductSchema extends Schema{
  import org.squeryl.PrimitiveTypeMode._

  val products: Table[Product] = table[Product]
  val productItems: Table[ProductItem] = table[ProductItem]
  val productItemRelation: PrimitiveTypeMode.OneToManyRelationImpl[Product, ProductItem] =
    oneToManyRelation(products, productItems).via(_.id === _.productId)
}