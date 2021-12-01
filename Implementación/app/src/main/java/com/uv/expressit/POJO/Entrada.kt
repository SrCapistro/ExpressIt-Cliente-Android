package com.uv.expressit.POJO

import android.graphics.Bitmap

class Entrada {
    var idEntrada: Long = 0
    var nombreUsuario: String = ""
    var fechaEntrada: String = ""
    var textoEntrada: String = ""

    var likesEntrada: Int = 0

    var usuarioLike: Boolean = false
    var hashtagEntrada: String = ""
    var listaHashtags: MutableList<String> = ArrayList()
    var imagen: Bitmap? = null
    var idUsuario: Long = 0
}