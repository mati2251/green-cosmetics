package pl.put.greenscosmetics.backend

import jakarta.persistence.*
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository

@Repository
interface ProductRepository : JpaRepository<Product, String> {
}


@Repository
interface IngredientRepository: JpaRepository<Ingredient, Long> {
}

