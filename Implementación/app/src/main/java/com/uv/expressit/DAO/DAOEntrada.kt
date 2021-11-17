package com.uv.expressit.DAO

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.Interfaces.VolleyCallback

class DAOEntrada {
    companion object{
        fun obtenerEntradasDeSeguidos(idUsuario: Long?, context: Context, callback: VolleyCallback){
            val urlService = "http://192.168.100.4:4000/feed/entradas_seguidores/"+idUsuario
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


        fun obtenerLikesEntrada(idEntrada: Long?, context: Context?, callback: VolleyCallback){
            val urlService = "http://localhost:4000/feed/entrada_likes/"+idEntrada
            val queue = Volley.newRequestQueue(context)
            val stringRequest = StringRequest(
                Request.Method.GET, urlService, Response.Listener<String>{
                    response ->
                    callback.onSuccessResponse(response)
                    return@Listener
                },
                Response.ErrorListener { println("Error") })
            queue.add(stringRequest)
        }
    }
}