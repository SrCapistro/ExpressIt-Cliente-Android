package com.uv.expressit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.uv.expressit.POJO.Entrada

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    var listaEntradas: MutableList<Entrada> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemNombreUsuario.text = listaEntradas[i].nombreUsuario
        viewHolder.itemFechaEntrada.text = listaEntradas[i].fechaEntrada
        viewHolder.itemEntradaUsuario.text = listaEntradas[i].textoEntrada
        viewHolder.contadorMeGusta.text = listaEntradas[i].likesEntrada.toString()
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

        init{
            itemNombreUsuario = itemView.findViewById(R.id.txtUsuarioPublicacion)
            itemEntradaUsuario = itemView.findViewById(R.id.txtEntradaContenido)
            itemFechaEntrada = itemView.findViewById(R.id.txtFechaEntrada)
            btnMeGusta = itemView.findViewById(R.id.btnLike)
            contadorMeGusta = itemView.findViewById(R.id.txtContadorMeGusta)
        }
    }
}