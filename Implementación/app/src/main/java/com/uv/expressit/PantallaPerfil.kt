package com.uv.expressit

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Usuario
import org.json.JSONObject

class PantallaPerfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_perfil)

        val txtNombreUsuario = findViewById<TextView>(R.id.txtNombreUsuario)
        val txtNombreCompleto = findViewById<TextView>(R.id.txtNombreCompleto)
        val txtFechaNacimiento = findViewById<TextView>(R.id.txtFechaNacimiento)
        val txtSeguidores = findViewById<TextView>(R.id.txtSeguidoresTotales)
        val txtEntradas = findViewById<TextView>(R.id.txtEntradasTotales)
        val txtDescripcion = findViewById<TextView>(R.id.txtDescripcionUsuario)
        val btnSeguir = findViewById<Button>(R.id.btnSeguir)
        val imageView = findViewById<ImageView>(R.id.fotoPerfil)
        val bundle = intent.extras
        val nombreUsuario = bundle?.getString("nombreUsuario")
        var perfilPersonal = bundle?.getBoolean("perfilPersonal")
        val usuarioIdInstancia = Usuario()
        if(perfilPersonal == true){
            //Aqui se muestran los datos del perfil personal
        }else{
            nombreUsuario?.let {
                DAOUsuario.obtenerUsuarioPorUserName(it, this, object: VolleyCallback {
                    override fun onSuccessResponse(result: String){
                        var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))
                        txtNombreCompleto.text = jsonObtenido.get("usr_nombre").toString()
                        txtNombreUsuario.text = "@"+nombreUsuario
                        txtFechaNacimiento.text = jsonObtenido.get("usr_fechaNacimiento").toString()
                        txtSeguidores.text = jsonObtenido.get("seguidores").toString()
                        txtEntradas.text = jsonObtenido.get("entradasTotales").toString()
                        txtDescripcion.text = jsonObtenido.get("usr_descripcion").toString()
                    }
                })
                val urlService = "http://192.168.100.4:4000/files/media/pictures/"+nombreUsuario
                val queue = Volley.newRequestQueue(this)
                var imageRequest = ImageRequest(urlService, Response.Listener<Bitmap>{ bitmap ->
                    imageView.setImageBitmap(bitmap)
                },0,0,null,null,
                    {error->
                    }
                )
                queue.add(imageRequest)
            }
        }


    }
}