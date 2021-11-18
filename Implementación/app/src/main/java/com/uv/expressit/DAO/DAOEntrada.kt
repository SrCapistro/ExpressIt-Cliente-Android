package com.uv.expressit.DAO

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.Interfaces.VolleyCallback
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import java.lang.Exception as Exception2
import kotlin.Exception as Exception1

class DAOEntrada {
    companion object {
        fun obtenerEntradasDeSeguidos(
            idUsuario: Long?,
            context: Context,
            callback: VolleyCallback
        ) {
            val urlService = "http://26.191.102.84:4000/feed/entradas_seguidores/" + idUsuario
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener<String> { response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }

        fun registrarEntrada(idUsuario: Long?, textoEntrada: String, context: Context) {
            val url =
                "http://26.191.102.84:4000/feed/registrar_entrada/" // ruta del método de json al que va a mandar los datos
            val parametros =
                HashMap<String, String>() // creación del hashMap para la construcción del objeto json
            parametros["ent_idUsuario"] = idUsuario.toString()
            parametros["ent_textEntrada"] = textoEntrada
            val jsonObject =
                JSONObject(parametros as Map<*, *>) // asignación del hasmap al objeto json

            val request = JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject, //creación y manejo del request
                Response.Listener { response ->
                    println("Éxito: ${response}")
                },
                Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }

        fun obtenerUltimaEntradaRegistrada(context: Context, callback: VolleyCallback) {
            val urlService = "http://26.191.102.84:4000/feed/obtenerId_ultimaEntrada/"
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener { response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error: $it")
                })
            queue.add(stringRequest)
        }

        fun registrarHashTags(hashtag: String, context: Context) {
            val url = "http://26.191.102.84:4000/feed/registrar_hashtag/"
            val parametros = HashMap<String, String>()
            parametros["htg_nombre"] = hashtag
            println("hashtags: $hashtag")
            val jsonObject = JSONObject(parametros as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject, //creación y manejo del request
                Response.Listener { response ->
                    println("Éxito: ${response}")
                },
                Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        }

        fun obtenerUltimoIdHashtagRegistrado(context: Context, callback: VolleyCallback) {
            val urlService = "http://26.191.102.84:4000/feed/obtenerId_ultimoHashtag/"
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener { response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error: $it")
                })
            queue.add(stringRequest)
        }

        fun obtenerHashtagsRecienRegistrados(idUltimoHashtag: Long?, context: Context, callback: VolleyCallback) {
            val urlService = "http://26.191.102.84:4000/feed/obtenerId_hashtags/"+idUltimoHashtag
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener { response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error: $it")
                })
            queue.add(stringRequest)
        }

        fun asociarHashTags(idHastag: Long?, idEntrada: Long?, context: Context) {
            val url = "http://26.191.102.84:4000/feed/asociar_hashtags/"
            val parametros = HashMap<String, String>()
            parametros["eh_idHashtag"] = idHastag.toString()
            parametros["eh_idEntrada"] = idEntrada.toString()
            val jsonObject = JSONObject(parametros as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject, //creación y manejo del request
                Response.Listener { response ->
                    println("Éxito: ${response}")
                },
                Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        }

    }
}