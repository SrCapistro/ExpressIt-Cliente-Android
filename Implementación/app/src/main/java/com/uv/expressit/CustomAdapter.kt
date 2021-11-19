package com.uv.expressit

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONObject

class CustomAdapter: RecyclerView.Adapter<CustomAdapter.ViewHolder>(){
    var listaEntradas: MutableList<Entrada> = ArrayList()
    var idUsuario: Long = 0
    var context: Context? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, @SuppressLint("RecyclerView") i: Int) {
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
                contadorMegusta--
                listaEntradas[i].usuarioLike = false
                viewHolder.contadorMeGusta.text = contadorMegusta.toString()
                viewHolder.btnMeGusta.setBackgroundColor(Color.parseColor("#DCDCDC"))
                viewHolder.btnMeGusta.setTextColor(Color.BLACK)
            }else{
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
            pantallaNombreUsuario.putExtra("idUsuarioLoggeado", idUsuario)
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

        init{
            itemNombreUsuario = itemView.findViewById(R.id.txtUsuarioPublicacion)
            itemEntradaUsuario = itemView.findViewById(R.id.txtEntrada)
            itemFechaEntrada = itemView.findViewById(R.id.txtFechaEntrada)
            btnMeGusta = itemView.findViewById(R.id.btnLike)
            contadorMeGusta = itemView.findViewById(R.id.txtContadorMeGusta)
        }
    }
}