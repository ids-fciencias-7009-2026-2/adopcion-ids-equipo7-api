data class Usuario(
    val id: String,
    var nombre: String,
    var email: String,
    var codigoPostal: String,
    var password: String? = null
    var token: String? = null
