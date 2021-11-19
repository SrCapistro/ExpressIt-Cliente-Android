package com.uv.expressit.DAO

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.Interfaces.VolleyCallback

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

    }
}