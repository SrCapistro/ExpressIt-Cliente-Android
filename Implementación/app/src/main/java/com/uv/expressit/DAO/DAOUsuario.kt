package com.uv.expressit.DAO

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Usuario
import org.json.JSONObject
import javax.security.auth.callback.Callback

class DAOUsuario {
    companion object{
        fun obtenerLoginUsuario(nombreUsuario: String, contraseña: String, context: Context, callback: VolleyCallback){
            val urlService = "http://192.168.100.4:4000/auth/login/"+nombreUsuario+"/"+contraseña
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