package pl.put.greenscosmetics.backend

import jakarta.persistence.*
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "products")
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: String,
    val name: String,
    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val ingredients: List<Ingredient>,
)

@Entity
@Table(name = "ingredients")
class Ingredient(
    @Id val id: String,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    val product: Product,
    val name: String,
    val description: String,
)
