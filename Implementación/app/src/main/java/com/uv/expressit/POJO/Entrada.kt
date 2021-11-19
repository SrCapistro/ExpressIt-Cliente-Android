package com.uv.expressit.POJO

class Entrada {
    var idEntrada: Long = 0
    var nombreUsuario: String = ""
    var fechaEntrada: String = ""
    var textoEntrada: String = ""
    var likesEntrada: Int = 0
    var usuarioLike: Boolean = false
    var listaHashtags: MutableList<String> = ArrayList()
}