package pl.put.greenscosmetics.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.slf4j.LoggerFactory

@Controller
class Controller {

  @Autowired private lateinit var productRepository: ProductRepository
  @Autowired private lateinit var ingredientRepository: IngredientRepository

  @GetMapping("/product/{id}/exists")
  fun productExists(@PathVariable id: String): ResponseEntity<Void> {
    LOGGER.info("Checking if product with id: $id exists")
    if (productRepository.existsById(id)) {
      return ResponseEntity.ok().build()
    }
    return ResponseEntity.notFound().build()
  }

  @PostMapping("/product")
  fun addProduct(@RequestBody product: ProductRequestBody): ResponseEntity<Product> {
    LOGGER.info("Adding product: ${product.name}")
    val pro = productRepository.save(Product(product.id, product.name, emptyList() ))
    product.ingredients.forEach {
      if (!ingredientRepository.existsById(it)) {
        val natural = it.hashCode() % 2 == 0
        ingredientRepository.save(Ingredient(it, pro, natural))
      }
    }
    return ResponseEntity.ok(pro)
  }

  @GetMapping("/product/{id}")
  fun getProduct(@PathVariable id: String): ResponseEntity<Product> {
    LOGGER.info("Getting product with id: $id")
    return productRepository.findById(id).map { ResponseEntity.ok(it) }.orElse(ResponseEntity.notFound().build())
  }

  companion object {
    val LOGGER = LoggerFactory.getLogger(Controller::class.java)
  }
}
