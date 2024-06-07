package pl.put.greenscosmetics.backend

import jakarta.persistence.*
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository

@Repository
interface ProductRepository : JpaRepository<Product, String> {
}


@Repository
interface IngredientRepository: CrudRepository<Ingredient, String> {
}

