package models.dto

import models.dao.entities.ProductItem
import play.api.libs.json.{Json, Reads, Writes}
import scala.util.Random

case class ProductItemDTO(id: String, price: Int, quantity: Int, inStock: Boolean)

object ProductItemDTO {
  implicit val reads: Reads[ProductItemDTO] = Json.reads[ProductItemDTO]
  implicit val writes: Writes[ProductItemDTO] = Json.writes[ProductItemDTO]

  private def toModel(productId: String, itemDTO: ProductItemDTO): ProductItem = {
    ProductItem(itemDTO.id, itemDTO.price, itemDTO.quantity, itemDTO.inStock, productId)
  }

  def toModel(product: ProductDTO): List[ProductItem] = {
    product.items.map(i => ProductItemDTO.toModel(product.id, i))
  }

  def fromModel(item: ProductItem): ProductItemDTO = {
    ProductItemDTO(item.id, item.price, item.quantity, item.inStock)
  }
}