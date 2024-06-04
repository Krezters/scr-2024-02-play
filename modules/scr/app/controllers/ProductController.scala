package controllers

import com.google.inject.Inject
import models.Product
import models.dto.ProductDTO
import models.services.ProductService
import models.services.LogService
import play.api.mvc.{Action, AnyContent}
import play.api.libs.json.Json

class ProductController @Inject()(
                                   val logService: LogService,
                                   val productService: ProductService
) extends Authorization {

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
      Ok(Json.toJson(productService.getProductsByTitle(title)))
    }.getOrElse{
      Ok(Json.toJson(productService.getAllProducts))
    }
  }

  def addProduct(): Action[ProductDTO] = Action(parse.json[ProductDTO]){ request =>
    productService.addProduct(request.body)
    Ok(Json.toJson(productService.getAllProducts))
  }

  def updateProduct(): Action[ProductDTO] = Action(parse.json[ProductDTO]){ request =>
    productService.updateProduct(request.body)
    Ok(Json.toJson(productService.getAllProducts))
  }

  def deleteProduct(id: String): Action[AnyContent] = Action{
    productService.deleteProduct(id)
    Ok(Json.toJson(productService.getAllProducts))
  }
}
