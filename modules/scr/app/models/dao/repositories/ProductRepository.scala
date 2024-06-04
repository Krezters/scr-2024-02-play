package models.dao.repositories

import models.dao.entities.{Product, ProductItem}
import models.dao.schema.ProductSchema
import org.squeryl.Table

trait ProductRepository {
  def getAll: List[(Product, List[ProductItem])]
  def findByTitle(title: String): List[(Product, List[ProductItem])]
  def addProduct(product: Product): Unit
  def addProduct(product: Product, productItems: List[ProductItem]): Unit
  def updateProduct(product: Product): Unit
  def updateItems(productItems: List[ProductItem]): Unit
  def deleteProduct(id: String): Unit
}

class ProductRepositoryImpl extends ProductRepository {
  val products: Table[Product] = ProductSchema.products
  val pItems: Table[ProductItem] = ProductSchema.productItems
  import org.squeryl.PrimitiveTypeMode._

  override def getAll: List[(Product, List[ProductItem])] = transaction {
    val prods = from(products)(p => select(p)).toList
    prods.map(p => (p, p.items.toList))
  }

  override def findByTitle(title: String): List[(Product, List[ProductItem])] = transaction {
    val prods = from(products)(p => where(p.title === title) select(p)).toList
    prods.map(p => (p, p.items.toList))
  }

  override def addProduct(product: Product): Unit = transaction(products.insert(product))


  override def addProduct(product: Product, productItems: List[ProductItem]): Unit = transaction {
    productItems.map(productItem => product.items.associate(productItem))
  }

  override def updateProduct(product: Product): Unit = transaction {
    update(products)(
      p => where(p.id === product.id)
        set(p.title := product.title, p.description := product.description)
    )
  }

  override def updateItems(productItems: List[ProductItem]): Unit =
    transaction(productItems.map(item => updateItem(item)))

  private def updateItem(item: ProductItem) = update(pItems)(
    p => where(p.id === item.id)
      set(
        p.price := item.price,
        p.quantity := item.quantity,
        p.inStock := item.inStock
      )
  )

  override def deleteProduct(id: String): Unit = transaction(products.deleteWhere(_.id === id))
}