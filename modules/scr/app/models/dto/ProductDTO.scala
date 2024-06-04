package models.dto

import models.dao.entities.{Product, ProductItem}
import play.api.libs.json.{Json, Reads, Writes}

case class ProductDTO(id: String, title: String, description: String, items: List[ProductItemDTO] = List.empty)

object ProductDTO {
  implicit val reads: Reads[ProductDTO] = Json.reads[ProductDTO]
  implicit val writes: Writes[ProductDTO] = Json.writes[ProductDTO]

  def toModel(productDTO: ProductDTO): Product = {
    Product(productDTO.id, productDTO.title, productDTO.description)
  }

  def fromModel(product: Product, productItems: List[ProductItem]): ProductDTO = {
    ProductDTO(
      product.id,
      product.title,
      product.description,
      productItems.map(i => ProductItemDTO.fromModel(i)))
  }
}