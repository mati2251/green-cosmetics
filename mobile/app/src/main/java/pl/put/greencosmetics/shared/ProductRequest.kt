package pl.put.greencosmetics.shared

data class ProductRequest(
    val id: String,
    val ingredients: List<String>,
    val name: String
)