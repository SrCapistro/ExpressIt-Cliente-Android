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
import com.uv.expressit.POJO.Entrada

class CustomAdapterPerfil: RecyclerView.Adapter<CustomAdapterPerfil.ViewHolder>(){

    var idUsuario: Long = 0
    var tipoUsuario: String = ""
    var context: Context? = null
    var listaEntradas: MutableList<Entrada> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var contadorMegusta = listaEntradas[i].likesEntrada

        viewHolder.contadorMeGusta.text = contadorMegusta.toString()
        viewHolder.itemNombreUsuario.text = listaEntradas[i].nombreUsuario
        viewHolder.itemFechaEntrada.text = listaEntradas[i].fechaEntrada
        viewHolder.itemEntradaUsuario.text = listaEntradas[i].textoEntrada

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
                viewHolder.contadorMeGusta.text = contadorMegusta.toString()
                viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#DCDCDC"))
                viewHolder.btnMeGusta.setTextColor(Color.BLACK)
            }else{
                DAOEntrada.likearEntrada(listaEntradas[i].idEntrada, idUsuario, context)
                contadorMegusta++
                listaEntradas[i].usuarioLike = true
                viewHolder.contadorMeGusta.text = contadorMegusta.toString()
                viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#00749E"))
                viewHolder.btnMeGusta.setTextColor(Color.WHITE)
            }
        }

        viewHolder.itemNombreUsuario.setOnClickListener{
            val pantallaNombreUsuario = Intent(context,PantallaPerfil::class.java)
            pantallaNombreUsuario.putExtra("nombreUsuario", listaEntradas[i].nombreUsuario)
            pantallaNombreUsuario.putExtra("perfilPersonal", false)
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

        init{
            itemNombreUsuario = itemView.findViewById(R.id.txtUsuarioPublicacion)
            itemEntradaUsuario = itemView.findViewById(R.id.txtEntrada)
            itemFechaEntrada = itemView.findViewById(R.id.txtFechaEntrada)
            btnMeGusta = itemView.findViewById(R.id.btnLike)
            contadorMeGusta = itemView.findViewById(R.id.txtContadorMeGusta)
            btnBorrarLike = itemView.findViewById(R.id.btnBorrarEntrada)
        }
    }
}