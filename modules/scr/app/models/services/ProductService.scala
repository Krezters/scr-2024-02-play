package models.services

import models.dto.{ProductDTO, ProductItemDTO}
import models.{Product, ProductItem}

import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success, Try}

object ProductService {
  private val products: ArrayBuffer[Product] = ArrayBuffer[Product]()
  private val productItems: ArrayBuffer[ProductItem] = ArrayBuffer[ProductItem]()

  def addProduct(productDTO: ProductDTO): Unit = {
    if (!products.exists(_.id == productDTO.id)) {
      products += Product(productDTO.id, productDTO.title, productDTO.description)
    }

    productDTO.items.map(item => addProductItem(productDTO.id, item))
  }

  private def addProductItem(productId: String, itemDTO: ProductItemDTO): Try[ProductItem] = {
    val item = ProductItem(productId, itemDTO.price, itemDTO.quantity, itemDTO.inStock)
    if (productItems.contains(item)) {
      Failure(new Throwable("Product item exists"))
    } else {
      productItems += item
      Success(item)
    }
  }

  def getAllProducts: List[ProductDTO] = {
    getProducts(products)
  }

  def getProductsByTitle(title: String): List[ProductDTO] = {
    getProducts(products.filter(_.title == title))
  }

  private def getProducts(productList: ArrayBuffer[Product]): List[ProductDTO] = {
    productList.map(product => {
      val items = productItems.filter(_.productId == product.id).map(item => {
        ProductItemDTO(item.price, item.quantity, item.inStock)
      }).toSet
      ProductDTO(product.id, product.title, product.description, items)
    }).toList
  }

  def updateProduct(productDTO: ProductDTO): Unit = {
    products.find(_.id == productDTO.id) match {
      case Some(product) =>
        deleteProduct(product.id)
        Success(addProduct(productDTO))
      case None => ()
    }
  }

  def deleteProduct(id: String): Unit = {
    products.find(_.id == id) match {
      case Some(product) =>
        products -= product
        productItems.filter(_.productId == product.id).foreach(item => {
          productItems -= item
        })
      case None => ()
    }
  }
}