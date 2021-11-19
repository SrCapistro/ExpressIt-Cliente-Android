package com.uv.expressit

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import com.uv.expressit.POJO.Usuario
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.sql.SQLOutput

class PantallaPerfil : AppCompatActivity() {
    private var idUsuario: Long? = null
    private var nombreUsuario: String? = ""
    private var tipoUsuario: String? = ""
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
        val recyclerView = findViewById<RecyclerView>(R.id.entradasUsuario)
        recyclerView.layoutManager = LinearLayoutManager(this@PantallaPerfil)


        val bundle = intent.extras
        tipoUsuario = bundle?.getString("tipoUsuario")
        nombreUsuario = bundle?.getString("nombreUsuario")
        var perfilPersonal = bundle?.getBoolean("perfilPersonal")
        idUsuario = bundle?.getLong("idUsuario")



        val usuarioIdInstancia = Usuario()
        if(perfilPersonal == true){
            //Aqui se muestran los datos del perfil personal
        }else{
            nombreUsuario?.let {
                DAOUsuario.obtenerUsuarioPorUserName(it, this, object: VolleyCallback {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccessResponse(result: String){
                        var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))
                        txtNombreCompleto.text = jsonObtenido.get("usr_nombre").toString()
                        txtNombreUsuario.text = "@"+nombreUsuario
                        txtFechaNacimiento.text = jsonObtenido.get("usr_fechaNacimiento").toString()
                        txtSeguidores.text = "Seguidores: "+jsonObtenido.get("seguidores").toString()
                        txtEntradas.text = "Entradas totales: "+jsonObtenido.get("entradasTotales").toString()
                        txtDescripcion.text = jsonObtenido.get("usr_descripcion").toString()
                    }
                })
                val urlService = "http://192.168.100.4:4000/files/media/pictures/"+nombreUsuario
                val queue = Volley.newRequestQueue(this)
                var imageRequest = ImageRequest(urlService, Response.Listener<Bitmap>{ bitmap ->
                    imageView.setImageBitmap(bitmap)
                },0,0,null,null,
                    {error->
                        Toast.makeText(this, "Error al cargar la foto de perfil", Toast.LENGTH_SHORT).show()
                    }
                )
                queue.add(imageRequest)
            }
            obtenerEntradas(idUsuario, nombreUsuario, recyclerView)
        }

    }

    private fun obtenerEntradas(idUsuario: Long?, nombreUsuario: String?, vistaRecycler: RecyclerView?){
        println("idusuario: "+idUsuario)
        println("nombreUsuario: "+ nombreUsuario)
        DAOEntrada.obtenerEntradasPersonales(idUsuario, nombreUsuario,this@PantallaPerfil,  object : VolleyCallback{
                override fun onSuccessResponse(result: String) {
                    val jsonArray = JSONArray(result)
                    val listaEntradas: MutableList<Entrada> = ArrayList()
                    for (i in 0 until jsonArray.length()){
                        var entradaJson = jsonArray.getJSONObject(i)
                        var entradaRecibida = Entrada()
                        entradaRecibida.idEntrada = entradaJson.get("ent_idEntrada").toString().toLong()
                        entradaRecibida.fechaEntrada = entradaJson.get("ent_fechaEntrada").toString()
                        entradaRecibida.textoEntrada = entradaJson.get("ent_textEntrada").toString()
                        entradaRecibida.nombreUsuario = entradaJson.get("usr_nombreUsuario").toString()
                        entradaRecibida.likesEntrada = entradaJson.get("likes_totales").toString().toInt()
                        try{
                            if(entradaJson.getLong("tuLike") > -1){
                                entradaRecibida.usuarioLike = true
                            }
                        }catch (exception: Exception){
                            entradaRecibida.usuarioLike = false
                        }
                        listaEntradas.add(entradaRecibida)
                        val adapter = CustomAdapter()
                        adapter.idUsuario = idUsuario!!
                        adapter.listaEntradas = listaEntradas
                        adapter.context = this@PantallaPerfil
                        adapter.tipoUsuario = tipoUsuario!!
                        vistaRecycler?.adapter = adapter
                    }
                }
            })
    }
}