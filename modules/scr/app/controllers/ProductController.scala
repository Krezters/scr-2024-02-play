package controllers

import com.google.inject.Inject
import models.Product
import models.dto.ProductDTO
import models.services.ProductService
import models.services.LogService
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.json.Json

class ProductController @Inject()(val logService: LogService) extends Authorization {

  def list = authorize{ rc =>
    logService.log("Hello from ProductController")
    Ok(views.html.products.list(List(
      Product("1", "product1", "20"),
      Product("2", "product2", "30"),
      Product("3", "product3", "40"),
      Product("4", "product4", "50"),
      Product("5", "product4", "60")
    )))
  }

  def getProducts(titleOpt: Option[String]): Action[AnyContent] = Action{
    titleOpt.map{ title =>
      Ok(Json.toJson(ProductService.getProductsByTitle(title)))
    }.getOrElse{
      Ok(Json.toJson(ProductService.getAllProducts))
    }
  }

  def addProduct(): Action[ProductDTO] = Action(parse.json[ProductDTO]){ request =>
    ProductService.addProduct(request.body)
    Ok(Json.toJson(ProductService.getAllProducts))
  }

  def updateProduct(): Action[ProductDTO] = Action(parse.json[ProductDTO]){ request =>
    ProductService.updateProduct(request.body)
    Ok(Json.toJson(ProductService.getAllProducts))
  }

  def deleteProduct(id: String): Action[AnyContent] = Action{
    ProductService.deleteProduct(id)
    Ok(Json.toJson(ProductService.getAllProducts))
  }
}
