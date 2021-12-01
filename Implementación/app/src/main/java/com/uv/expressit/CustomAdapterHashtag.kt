package com.uv.expressit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
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
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONArray

class CustomAdapterHashtag: RecyclerView.Adapter<CustomAdapterHashtag.ViewHolder>(){

    var listaEntradas: MutableList<Entrada> = ArrayList()
    var idUsuario: Long = 0
    var context: Context? = null
    var tipoUsuario: String = ""

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {
        var contadorMegusta = listaEntradas[i].likesEntrada

        viewHolder.btnBorrarEntrada.visibility = View.INVISIBLE

        var fechaEntrada = listaEntradas[i].fechaEntrada.replace("T", " a las: ")
        fechaEntrada = fechaEntrada.replace("Z", "")

        viewHolder.contadorMeGusta.text = contadorMegusta.toString()+" Me gusta"
        viewHolder.itemNombreUsuario.text = listaEntradas[i].nombreUsuario
        viewHolder.itemFechaEntrada.text = "Publicado el dia: "+fechaEntrada
        viewHolder.itemEntradaUsuario.text = listaEntradas[i].textoEntrada
        viewHolder.hashtagsEntrada.text = listaEntradas[i].hashtagEntrada

        if(listaEntradas[i].usuarioLike == true){
            viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#00749E"))
            viewHolder.btnMeGusta.setTextColor(Color.WHITE)
        }

        viewHolder.btnMeGusta.setOnClickListener{
            if(listaEntradas[i].usuarioLike){
                DAOEntrada.borrarLike(listaEntradas[i].idEntrada, idUsuario, context)
                contadorMegusta--
                listaEntradas[i].usuarioLike = false
                viewHolder.contadorMeGusta.text = contadorMegusta.toString()+" Me gusta"
                viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#DCDCDC"))
                viewHolder.btnMeGusta.setTextColor(Color.BLACK)
            }else{
                DAOEntrada.likearEntrada(listaEntradas[i].idEntrada, idUsuario, context)
                contadorMegusta++
                listaEntradas[i].usuarioLike = true
                viewHolder.contadorMeGusta.text = contadorMegusta.toString()+" Me gusta"
                viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#00749E"))
                viewHolder.btnMeGusta.setTextColor(Color.WHITE)
            }
        }

        viewHolder.itemNombreUsuario.setOnClickListener{
            val pantallaNombreUsuario = Intent(context, PantallaPerfil::class.java)
            pantallaNombreUsuario.putExtra("nombreUsuario", listaEntradas[i].nombreUsuario)
            pantallaNombreUsuario.putExtra("perfilPersonal", false)
            pantallaNombreUsuario.putExtra("idUsuario", idUsuario)
            pantallaNombreUsuario.putExtra("tipoUsuario", tipoUsuario)
            context?.startActivity(pantallaNombreUsuario)
        }

        var urlService = "http://192.168.100.4:4000/files/media/"+listaEntradas[i].idEntrada // -> Capi
        //val urlService = "http://26.191.102.84:4000/files/media/"+listaEntradas[i].idEntrada
        //val urlService = "http://192.168.0.21:4000/files/media/"+listaEntradas[i].idEntrada  // -> Zuriel

        val queue = Volley.newRequestQueue(context)
        var imageRequest = ImageRequest(urlService, Response.Listener<Bitmap>{ bitmap ->
            viewHolder.imagenEntrada.setImageBitmap(bitmap)
        },0,0,null,null,
            {error->
                println(error   )
            }
        )
        queue.add(imageRequest)

    }

    override fun getItemCount(): Int {
        return listaEntradas.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemNombreUsuario: TextView
        var itemEntradaUsuario: TextView
        var itemFechaEntrada: TextView
        var btnMeGusta: Button
        var contadorMeGusta: TextView
        var btnBorrarEntrada: Button
        var hashtagsEntrada: TextView
        var imagenEntrada: ImageView

        init{
            imagenEntrada = itemView.findViewById(R.id.imageEntrada)
            itemNombreUsuario = itemView.findViewById(R.id.txtUsuarioPublicacion)
            itemEntradaUsuario = itemView.findViewById(R.id.txtEntrada)
            itemFechaEntrada = itemView.findViewById(R.id.txtFechaEntrada)
            btnMeGusta = itemView.findViewById(R.id.btnLike)
            hashtagsEntrada = itemView.findViewById(R.id.txtHashtagsEntrada)
            contadorMeGusta = itemView.findViewById(R.id.txtContadorMeGusta)
            btnBorrarEntrada = itemView.findViewById(R.id.btnBorrarEntrada)
        }
    }
}