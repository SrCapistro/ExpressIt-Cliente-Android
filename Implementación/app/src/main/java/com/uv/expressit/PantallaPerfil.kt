package com.uv.expressit

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
        var idUsuarioLoggeado: Long? = bundle?.getLong("idUsuarioLoggeado")
        println("Usuario: $idUsuarioLoggeado")
        val usuarioIdInstancia = Usuario()
        if(perfilPersonal == true){
            //Aqui se muestran los datos del perfil personal
            //se debe ocultar el boton de seguir
        }else{
            var idUsuarioObtenido: Long? = 0
            nombreUsuario?.let {
                DAOUsuario.obtenerUsuarioPorUserName(it, this, object: VolleyCallback {
                    override fun onSuccessResponse(result: String){
                        var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))
                        idUsuarioObtenido = jsonObtenido.get("usr_idUsuario").toString().toLong()
                        println("Usuario Obtenido; $idUsuarioObtenido")
                        txtNombreCompleto.text = jsonObtenido.get("usr_nombre").toString()
                        txtNombreUsuario.text = "@"+nombreUsuario
                        txtFechaNacimiento.text = jsonObtenido.get("usr_fechaNacimiento").toString()
                        txtSeguidores.text = jsonObtenido.get("seguidores").toString()
                        txtEntradas.text = jsonObtenido.get("entradasTotales").toString()
                        txtDescripcion.text = jsonObtenido.get("usr_descripcion").toString()

                        //verificar si se sigue al usuario
                        DAOUsuario.obtenerSeguidor(idUsuarioObtenido, idUsuarioLoggeado, this@PantallaPerfil,
                            object : VolleyCallback{
                                override fun onSuccessResponse(result: String) {
                                    try {
                                        var idRegistrado = JSONObject(JSONUtils.parsearJson(result))
                                        var idObtenido : Long? = 0
                                        idObtenido = idRegistrado.get("sg_idSeguidor").toString().toLong()
                                        btnSeguir.text = "Siguiendo"
                                        btnSeguir.setBackgroundColor(Color.parseColor("#00749E"))
                                        btnSeguir.setTextColor(Color.WHITE)
                                    }catch (e:Exception){
                                        btnSeguir.text = "Seguir"
                                        btnSeguir.setBackgroundColor(Color.parseColor("#DCDCDC"))
                                        btnSeguir.setTextColor(Color.BLACK)
                                        println("Usuario no seguido: ${e.message}")
                                    }

                                    btnSeguir.setOnClickListener(){
                                        if(btnSeguir.text.toString() == "Siguiendo"){
                                            try{
                                                Thread.sleep(400)
                                                DAOUsuario.dejarDeSeguir(idUsuarioLoggeado, idUsuarioObtenido, this@PantallaPerfil)
                                                btnSeguir.text = "Seguir"
                                                btnSeguir.setBackgroundColor(Color.parseColor("#DCDCDC"))
                                                btnSeguir.setTextColor(Color.BLACK)
                                            }catch(exc:Exception){
                                                Toast.makeText(this@PantallaPerfil, "Error al dejar de seguir", Toast.LENGTH_LONG).show()
                                                println("Error al dejar de seguir: ${exc.message}")
                                            }
                                        }else{
                                            try{
                                                Thread.sleep(400)
                                                DAOUsuario.seguir(idUsuarioLoggeado, idUsuarioObtenido, this@PantallaPerfil)
                                                btnSeguir.text = "Siguiendo"
                                                btnSeguir.setBackgroundColor(Color.parseColor("#00749E"))
                                                btnSeguir.setTextColor(Color.WHITE)
                                            }catch(exce:Exception){
                                                Toast.makeText(this@PantallaPerfil, "Error al seguir", Toast.LENGTH_LONG).show()
                                                println("Error al dejar de seguir: ${exce.message}")
                                            }
                                        }
                                    }

                                }
                            })

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