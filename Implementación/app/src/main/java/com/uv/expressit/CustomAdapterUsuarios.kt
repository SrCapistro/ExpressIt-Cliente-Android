package com.uv.expressit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Usuario
import org.json.JSONObject

class CustomAdapterUsuarios(): RecyclerView.Adapter<CustomAdapterUsuarios.ViewHolder>() {
    var context : Context? = null
    var idUsuarioLong : Long = 0
    var idUsuarioInt : Int = 0
    var listaUsuarios : MutableList<Usuario> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomAdapterUsuarios.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.lista_usuarios, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: CustomAdapterUsuarios.ViewHolder, @SuppressLint("RecyclerView") i: Int){
        viewHolder.nombreUsuario.setText(listaUsuarios[i].nombreUsuario)
        viewHolder.nombrecompleto.setText("")
        var idUsuarioLista: Int = (listaUsuarios[i].idUsuario.toInt())

        DAOUsuario.obtenerNumeroDeImagenes( idUsuarioLista, context!!, object: VolleyCallback {
            override fun onSuccessResponse(result: String) {
                var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))
                var numero: Int = jsonObtenido.get("numeroFoto").toString().toInt()

                if( numero > 0){

                    val urlService = "http://192.168.100.4:4000/files/media/profile_pictures/"+listaUsuarios[i].idUsuario //-> Capi
                    //val urlService = "http://26.191.102.84:4000/files/media/profile_pictures/"+listaUsuarios[i].idUsuario
                    //val urlService = "http://192.168.100.4:4000/files/media/profile_pictures/"+listaUsuarios[i].idUsuario
                    //var urlService = "http://26.191.102.84:4000/files/media/profile_pictures/"+listaUsuarios[i].idUsuario // Brandon
                    //val urlService = "http://192.168.0.21:4000/files/media/profile_pictures/"+listaUsuarios[i].idUsuario //-> Zuriel

                    val queue = Volley.newRequestQueue(context)
                    var imageRequest = ImageRequest(urlService, Response.Listener<Bitmap>{ bitmap ->
                        viewHolder.imagenPerfil.setImageBitmap(bitmap)
                    },0,0,null,null,
                        {error->
                            Toast.makeText(context, "Error al cargar la foto de perfil", Toast.LENGTH_SHORT).show()
                        }
                    )
                    queue.add(imageRequest)
                }
            }
        })

        viewHolder.verPerfil.setOnClickListener{
            val pantallaNombreUsuario = Intent(context,PantallaPerfil::class.java)
            pantallaNombreUsuario.putExtra("perfilPersonal", false)
            pantallaNombreUsuario.putExtra("nombreUsuario", listaUsuarios[i].nombreUsuario)
            pantallaNombreUsuario.putExtra("tipoUsuario", listaUsuarios[i].tipoUsuario)
            pantallaNombreUsuario.putExtra("idUsuario", idUsuarioLong)
            context?.startActivity(pantallaNombreUsuario)
        }
    }

    override fun getItemCount() = listaUsuarios.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var imagenPerfil : ImageView
        var nombreUsuario : TextView
        var nombrecompleto: TextView
        var verPerfil: Button

        init{
            imagenPerfil = itemView.findViewById(R.id.imgPerfil)
            nombreUsuario = itemView.findViewById(R.id.tvUsername)
            nombrecompleto = itemView.findViewById(R.id.tvNombreCompleto)
            verPerfil = itemView.findViewById(R.id.btnVisualizarPerfil)
        }
    }
}

