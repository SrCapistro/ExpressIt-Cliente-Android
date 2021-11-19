package com.uv.expressit.DAO

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.Interfaces.VolleyCallback
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class DAOEntrada {
    companion object {
        var direccion: String = "http://192.168.100.4:4000/"
        fun obtenerEntradasDeSeguidos(
            idUsuario: Long?,
            idEntrada: Long?,
            context: Context,
            callback: VolleyCallback
        ) {
            val urlService = direccion+"feed/entradas_seguidores/" + idUsuario+"/"+idEntrada
            val queue = Volley.newRequestQueue(context)
            val parametros = HashMap<String, String>()
            val json = JSONObject(parametros as Map<*,*>)

            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener<String> { response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }


        fun obtenerEntradasPersonales(idUsuario: Long?, nombreUsuario: String?, context: Context, volleyCallback: VolleyCallback){
            val url = direccion+"feed/entradas/"+nombreUsuario+"/"+idUsuario
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String>{
                response ->
                volleyCallback.onSuccessResponse(response)
                return@Listener
            },
            Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }

        fun registrarEntrada(idUsuario: Long?, textoEntrada: String, context: Context) {
            val url = direccion+"feed/registrar_entrada/" // ruta del método de json al que va a mandar los datos
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
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }

        fun obtenerUltimaEntradaRegistrada(context: Context, callback: VolleyCallback) {
            val urlService = direccion+"feed/obtenerId_ultimaEntrada/"
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
            val url = direccion+"feed/registrar_hashtag/"
            val parametros = HashMap<String, String>()
            parametros["htg_nombre"] = hashtag
            val jsonObject = JSONObject(parametros as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject, //creación y manejo del request
                Response.Listener { response ->
                    println("Éxito: ${response}")
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        }

        fun obtenerUltimoIdHashtagRegistrado(context: Context, callback: VolleyCallback) {
            val urlService = direccion+"feed/obtenerId_ultimoHashtag/"
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
            val urlService = direccion+"feed/obtenerId_hashtags/"+idUltimoHashtag
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
            val url = direccion+"feed/asociar_hashtags/"
            val parametros = HashMap<String, String>()
            parametros["eh_idHashtag"] = idHastag.toString()
            parametros["eh_idEntrada"] = idEntrada.toString()
            val jsonObject = JSONObject(parametros as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject, //creación y manejo del request
                Response.Listener { response ->
                    println("Éxito: ${response}")
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)

        }

        fun likearEntrada(idEntrada: Long?, idUsuario: Long?, context: Context?){
            var url = direccion+"feed/likear_entrada"
            val parametros = HashMap<String, Any>()


            parametros["lk_idEntrada"] = idEntrada.toString().toLong()
            parametros["lk_idUsuario"] = idUsuario.toString().toLong()
            val jsonObject = JSONObject(parametros as Map<*,*>)

            val request = JsonObjectRequest(Request.Method.POST,
            url,
            jsonObject,
            Response.Listener { response ->
                println(response)
                return@Listener
            },
            Response.ErrorListener { println(it) })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }

        fun borrarLike(idEntrada: Long?, idUsuario: Long?, context: Context?){
            var url = direccion+"feed/entrada_borrarlike/"+idEntrada+"/"+idUsuario
            val request = StringRequest(Request.Method.DELETE,
            url,
            Response.Listener { response -> println(response)
                              return@Listener},
            Response.ErrorListener { println(it) })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }
    }
}