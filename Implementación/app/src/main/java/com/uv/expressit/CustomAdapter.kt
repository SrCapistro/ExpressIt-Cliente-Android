package com.uv.expressit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONArray
import kotlin.collections.ArrayList

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    var listaEntradas: MutableList<Entrada> = ArrayList()
    var idUsuario: Long = 0
    var context: Context? = null
    var tipoUsuario: String = ""
    var listaHashtags: MutableList<String> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {
        var contadorMegusta = listaEntradas[i].likesEntrada
        if(tipoUsuario.equals("Comun")){
            viewHolder.btnBorrarLike.visibility = View.INVISIBLE
        }

        var fechaEntrada = listaEntradas[i].fechaEntrada.replace("T", " a las: ")
        fechaEntrada = fechaEntrada.replace("Z", "")

        viewHolder.contadorMeGusta.text = contadorMegusta.toString()+" Me gusta"
        viewHolder.itemNombreUsuario.text = listaEntradas[i].nombreUsuario
        viewHolder.itemFechaEntrada.text = "Publicado el dia: "+fechaEntrada
        viewHolder.itemEntradaUsuario.text = listaEntradas[i].textoEntrada



        var cadenaHashtags: String = ""
        for(j in 0 until listaEntradas[i].listaHashtags.size){
            cadenaHashtags = cadenaHashtags +", "+listaEntradas[i].listaHashtags[j]
        }
        var hashtags = cadenaHashtags.replaceFirstChar { "" }
        viewHolder.hashtagsEntrada.text = hashtags

        if(listaEntradas[i].usuarioLike == true){
            viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#00749E"))
            viewHolder.btnMeGusta.setTextColor(Color.WHITE)
        }
        println(listaEntradas[i].usuarioLike)

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
            val pantallaNombreUsuario = Intent(context,PantallaPerfil::class.java)
            pantallaNombreUsuario.putExtra("nombreUsuario", listaEntradas[i].nombreUsuario)
            pantallaNombreUsuario.putExtra("perfilPersonal", false)
            pantallaNombreUsuario.putExtra("idUsuario", idUsuario)
            pantallaNombreUsuario.putExtra("tipoUsuario", tipoUsuario)
            context?.startActivity(pantallaNombreUsuario)
        }
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
        var btnBorrarLike: Button
        var hashtagsEntrada: TextView

        init{
            itemNombreUsuario = itemView.findViewById(R.id.txtUsuarioPublicacion)
            itemEntradaUsuario = itemView.findViewById(R.id.txtEntrada)
            itemFechaEntrada = itemView.findViewById(R.id.txtFechaEntrada)
            btnMeGusta = itemView.findViewById(R.id.btnLike)
            hashtagsEntrada = itemView.findViewById(R.id.txtHashtagsEntrada)
            contadorMeGusta = itemView.findViewById(R.id.txtContadorMeGusta)
            btnBorrarLike = itemView.findViewById(R.id.btnBorrarEntrada)
        }
    }
}