package pl.put.greenscosmetics.backend

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "products")
class Product(
    @Id
    val id: String,
    val name: String,
    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val ingredients: List<Ingredient>,
)

@Entity
@Table(name = "ingredients")
class Ingredient(
    @Id 
    val name: String,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    val product: Product,
    val isNatural: Boolean,
)

data class ProductRequestBody(
  val id: String,
  val name: String,
  val ingredients: List<String>,
)

