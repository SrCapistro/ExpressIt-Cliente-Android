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
        //var direccion: String = "http://26.191.102.84:4000/"
        var direccion: String = "http://192.168.100.4:4000/" // -> Capi
        //var direccion: String = "http://192.168.0.21:4000/" //-> Zuriel

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

        fun obtenerEntradasPorRelevancia(idUsuario: Long?, idEntrada: Long?,context: Context, callback: VolleyCallback){
            val urlService = direccion+"feed/entradas_seguidores_likes/" + idUsuario+"/"+idEntrada
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


        fun obtenerHashtagEntrada(idEntrada: Long?, context: Context?, volleyCallback: VolleyCallback){
            val url = direccion+"feed/hashtags/"+idEntrada
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String>{ response ->
                volleyCallback.onSuccessResponse(response)
                return@Listener
            }, Response.ErrorListener { print("Error") })
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
            val url = direccion+"feed/registrar_entrada/" // ruta del m??todo de json al que va a mandar los datos
            val parametros =
                HashMap<String, String>() // creaci??n del hashMap para la construcci??n del objeto json
            parametros["ent_idUsuario"] = idUsuario.toString()
            parametros["ent_textEntrada"] = textoEntrada
            val jsonObject =
                JSONObject(parametros as Map<*, *>) // asignaci??n del hasmap al objeto json

            val request = JsonObjectRequest(Request.Method.POST,
                url,
                jsonObject, //creaci??n y manejo del request
                Response.Listener { response ->
                    println("??xito: ${response}")
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
                jsonObject, //creaci??n y manejo del request
                Response.Listener { response ->
                    println("??xito: ${response}")
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
                jsonObject, //creaci??n y manejo del request
                Response.Listener { response ->
                    println("??xito: ${response}")
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

        fun borrarEntradaModerador(idEntrada: Long?, context: Context?) {
            val urlService = DAOEntrada.direccion +"feed/borrarEntrada_moderador/"+idEntrada
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.DELETE, urlService, Response.Listener { response ->
                    println(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error al borrar entrada: $it")
                })
            queue.add(stringRequest)
        }

        fun obtenerIdHashtagDeEntradaBorrada(idEntrada: Long?, context: Context?, callback: VolleyCallback) {
            val urlService = direccion+"feed/obtenerId_hashtags_entradaBorrada/"+idEntrada
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener { response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error al obtenerHashtags: $it")
                })
            queue.add(stringRequest)
        }

        fun desAsociarEntradaDeHashtagsModerador(idEntrada: Long?, context: Context?) {
            val urlService = DAOEntrada.direccion +"feed/desAsociarEntrada_moderador/"+idEntrada
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.DELETE, urlService, Response.Listener { response ->
                    println(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error al des asociar entrada: $it")
                })
            queue.add(stringRequest)
        }

        fun borrarHashtagsModerador(idHashtag: Long?, context: Context?) {
            val urlService = DAOEntrada.direccion +"feed/borrarHashtag_moderador/"+idHashtag
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.DELETE, urlService, Response.Listener { response ->
                    println(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error al borrar hashtag: $it")
                })
            queue.add(stringRequest)
        }

        fun obtenerEntradasHashtag(hashtagEntrada: String, context: Context?, volleyCallback: VolleyCallback){
            val url = direccion+"feed/busqueda/hashtag/"+hashtagEntrada
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String>{ response ->
                volleyCallback.onSuccessResponse(response)
                return@Listener
            }, Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }

    }
}