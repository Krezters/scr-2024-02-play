package models.services

import com.google.inject.Inject
import models.dao.entities.{Product, ProductItem}
import models.dao.repositories.ProductRepository
import models.dto.{ProductDTO, ProductItemDTO}

class ProductService @Inject()(val productRepository: ProductRepository){
  def addProduct(product: ProductDTO): Unit = {
    productRepository.addProduct(ProductDTO.toModel(product))
    addProductItems(product)
  }

  private def addProductItems(product: ProductDTO): Unit = {
    val items = ProductItemDTO.toModel(product)
    productRepository.addProduct(ProductDTO.toModel(product), items)
  }

  def getAllProducts: List[ProductDTO] =
    productRepository.getAll.map(c => ProductDTO.fromModel(c._1, c._2))

  def getProductsByTitle(title: String): List[ProductDTO] =
    productRepository.findByTitle(title).map(c => ProductDTO.fromModel(c._1, c._2))

  def updateProduct(product: ProductDTO): Unit = {
    productRepository.updateProduct(ProductDTO.toModel(product))
    productRepository.updateItems(ProductItemDTO.toModel(product))
  }

  def deleteProduct(id: String): Unit = productRepository.deleteProduct(id)
}