package models.dao.entities

import models.dao.schema.ProductSchema
import org.squeryl.KeyedEntity
import org.squeryl.dsl.OneToMany

case class Product(id: String, title: String, description: String) extends KeyedEntity[String] {
    lazy val items: OneToMany[ProductItem] = ProductSchema.productItemRelation.left(this)
}