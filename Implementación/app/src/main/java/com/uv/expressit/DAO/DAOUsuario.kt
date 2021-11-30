package com.uv.expressit.DAO

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.Interfaces.VolleyCallback
import org.json.JSONObject

class DAOUsuario {
    companion object{
        var direccion: String = "http://192.168.100.4:4000/"
        fun obtenerLoginUsuario(nombreUsuario: String, contraseña: String, context: Context, callback: VolleyCallback){
            val urlService = direccion+"auth/login/"+nombreUsuario+"/"+contraseña
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET,urlService, Response.Listener<String> {
                        response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }

        fun obtenerUsuarioPorUserName(
            nombreUsuario: String,
            context: Context,
            callback: VolleyCallback
        ){
            val urlService = direccion+"users/"+nombreUsuario
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET,urlService, Response.Listener<String> {
                        response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }

        fun obtenerSeguidor(idUsuarioSeguido: Long?, idUsuarioSeguidor:Long?, context: Context, callback: VolleyCallback) {
            val urlService = DAOEntrada.direccion +"users/obtener_seguidor/"+idUsuarioSeguido+"/"+idUsuarioSeguidor
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

        fun seguir(idUsuarioSeguidor: Long?, idUsuarioSeguido: Long?, context:Context){
            val url = DAOEntrada.direccion +"users/seguir/"
            val parametros = HashMap<String, String>()
            parametros["sg_idSeguidor"] = idUsuarioSeguidor.toString()
            parametros["sg_idSeguido"] = idUsuarioSeguido.toString()
            val jsonObject = JSONObject(parametros as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject, //creación y manejo del request
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

        fun dejarDeSeguir(idUsuarioSeguidor: Long?, idUsuarioSeguido:Long?, context: Context) {
            val urlService = DAOEntrada.direccion +"users/dejar_seguir/"+idUsuarioSeguidor+"/"+idUsuarioSeguido
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.DELETE, urlService, Response.Listener { response ->
                    println(response)
                    return@Listener
                },
                Response.ErrorListener {
                    println("Error al dejar de seguir: $it")
                })
            queue.add(stringRequest)
        }

        fun registrarNuevoUsuario(nombreUsuario:String, descripcion:String, estatus:Int, nombreCompleto:String,
                                  correo:String, tipoUsuario:String, contra:String, nacimiento:String, context: Context){

            val url = direccion +"users/registrar_nuevo_usuario/"
            val parametros = HashMap<String, String>()
            parametros["usr_nombreUsuario"] = nombreUsuario
            parametros["usr_descripcion"] = descripcion
            parametros["usr_estatus"] = estatus.toString()
            parametros["usr_nombre"] = nombreCompleto
            parametros["usr_correo"] = correo
            parametros["usr_tipoUsuario"] = tipoUsuario
            parametros["usr_contraseña"] = contra
            parametros["usr_fechaNacimiento"] = nacimiento

            val jsonObject = JSONObject(parametros as Map<*, *>)

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener { response ->
                    println("Éxito: ${response}")
                }, Response.ErrorListener {
                    println("Error: ${it}")
                })
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }

        //metodo para ver si ya hay un usuario con ese nombre
        fun obtenerNombresUsuarios(nombreUsuario: String, context: Context, callback: VolleyCallback){
            val urlService = direccion +"users/obtener_Usuarios/"+nombreUsuario
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET,urlService, Response.Listener<String> {
                        response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener { print("Error") })
            queue.add(stringRequest)
        }


    }
}