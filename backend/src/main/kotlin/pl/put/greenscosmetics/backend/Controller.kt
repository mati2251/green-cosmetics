package pl.put.greenscosmetics.backend

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@Controller
class Controller {

  @Autowired private lateinit var productRepository: ProductRepository

  @GetMapping("/product/{id}/exsists")
  fun productExists(@PathVariable id: String): ResponseEntity<Void> {
    if (productRepository.existsById(id)) {
      return ResponseEntity.ok().build()
    }
    return ResponseEntity.notFound().build()
  }

  @PostMapping("/product")
  fun addProduct(@RequestBody product: Product): ResponseEntity<Void> {
    productRepository.save(product)
    return ResponseEntity.ok().build()
  }
}
