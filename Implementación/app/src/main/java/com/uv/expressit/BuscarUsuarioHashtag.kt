package com.uv.expressit

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import com.uv.expressit.POJO.Usuario
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class BuscarUsuarioHashtag : AppCompatActivity() {

    private var idUsuario: Long? = null
    private var tipoUsuario: String? = ""
    private var usuarioTipo: String = ""
    private var idUsuarioInt: Int = -1
    private var cadenaString: String = ""
    private var valorLong: Long = 0
    var usuariosTuiter: MutableList<Usuario> = ArrayList()
    var usuariosTuiterAuxiliar: MutableList<Usuario> = ArrayList()
    var listaEntradasBD: MutableList<Entrada> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_usuario_hashtag)

        val recyclerView = findViewById<RecyclerView>(R.id.rvUsuarios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val bundle = intent.extras

        idUsuario = bundle?.getLong("idUsuario")
        tipoUsuario = bundle?.getString("tipoUsuario")

        if (idUsuario != null){
            valorLong = idUsuario as Long;
            idUsuarioInt =  valorLong.toInt()
        }
        if (tipoUsuario != null){
            usuarioTipo = tipoUsuario as String;
        }

        val buscarUsuarios = findViewById<RadioButton>(R.id.rbUsuario)
        val buscarHastag = findViewById<RadioButton>(R.id.rbHashtag)
        val buscarInformacion = findViewById<SearchView>(R.id.svBuscar)

        buscarUsuarios.isChecked = true;
        usuariosTuiter.clear()
        usuariosTuiter = obtenerUsuarios()

        buscarInformacion.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                usuariosTuiterAuxiliar.clear()
                listaEntradasBD.clear()
                recyclerView.removeAllViewsInLayout()

                if (query != null) {
                    cadenaString = query
                }
                if(!cadenaString.isEmpty()){
                    if(buscarUsuarios.isChecked){

                        for(i in 0 until usuariosTuiter.size){
                            var encontroUsuario = usuariosTuiter[i].nombreUsuario.contains(cadenaString,ignoreCase = true)
                            var encontroNombre = usuariosTuiter[i].nombreCompletoUsuario.contains(cadenaString,ignoreCase = true)

                            if(encontroUsuario || encontroNombre){
                                usuariosTuiterAuxiliar.add(usuariosTuiter[i])

                            }
                        }
                        if(usuariosTuiterAuxiliar.size > 0) {

                            val adapter = CustomAdapterUsuarios()
                            adapter.context = this@BuscarUsuarioHashtag
                            adapter.idUsuarioLong = valorLong
                            adapter.idUsuarioInt = idUsuarioInt
                            adapter.listaUsuarios = usuariosTuiterAuxiliar

                            println("Usuario encontrado de el coustomer; ${adapter.idUsuarioLong}")

                            recyclerView.adapter = adapter
                        }else{
                            Toast.makeText(this@BuscarUsuarioHashtag, "No se encontraron resultados", Toast.LENGTH_LONG).show()
                        }
                    }
                    if(buscarHastag.isChecked){

                        if(cadenaString[0].equals('#')){

                            var cadenaStringAuxiliar = cadenaString.subSequence(1,cadenaString.length)
                            var hashtagBuscado = cadenaStringAuxiliar as String

                            DAOEntrada.obtenerEntradasHashtag(hashtagBuscado, this@BuscarUsuarioHashtag, object: VolleyCallback{
                                override fun onSuccessResponse(result: String) {
                                    val jsonArray = JSONArray(result)
                                    for (i in 0 until jsonArray.length()){
                                        var listaEntradasJson = jsonArray.getJSONObject(i)

                                        var entradaRecibida = Entrada()

                                        var idEntrada = listaEntradasJson.get("ent_idEntrada").toString()

                                        if(!idEntrada.equals("null")){

                                            entradaRecibida.idEntrada = listaEntradasJson.get("ent_idEntrada").toString().toLong()
                                            entradaRecibida.fechaEntrada = listaEntradasJson.get("ent_fechaEntrada").toString()
                                            entradaRecibida.textoEntrada = listaEntradasJson.get("ent_textEntrada").toString()
                                            entradaRecibida.idUsuario = listaEntradasJson.get("ent_idUsuario").toString().toLong()
                                            entradaRecibida.nombreUsuario = listaEntradasJson.get("usr_nombreUsuario").toString()
                                            entradaRecibida.likesEntrada = listaEntradasJson.get("ent_idUsuario").toString().toInt()
                                            entradaRecibida.hashtagEntrada = listaEntradasJson.get("htg_nombre").toString()

                                            try{
                                                if(listaEntradasJson.getLong("tu_Like") > -1){
                                                    entradaRecibida.usuarioLike = true
                                                }
                                            }catch (exception: Exception){
                                                entradaRecibida.usuarioLike = false
                                            }

                                            listaEntradasBD.add(entradaRecibida)
                                        }

                                    }
                                    if(listaEntradasBD.size > 0){
                                        val adapter = CustomAdapterHashtag()
                                        adapter.idUsuario = valorLong
                                        adapter.listaEntradas = listaEntradasBD
                                        adapter.context = this@BuscarUsuarioHashtag
                                        adapter.tipoUsuario = tipoUsuario.toString()

                                        recyclerView.adapter = adapter
                                    }else{
                                        Toast.makeText(this@BuscarUsuarioHashtag, "No se encontro el Hashtag", Toast.LENGTH_LONG).show()
                                    }
                                }
                            })
                        }else{
                            Toast.makeText(this@BuscarUsuarioHashtag, "Lo que escribio no es un Hashtag", Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(this@BuscarUsuarioHashtag, "Debe escribir una cadena de texto en el buscador", Toast.LENGTH_LONG).show()
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    fun obtenerUsuarios():MutableList<Usuario>{
        var listaUsuarios: MutableList<Usuario> = ArrayList()

        DAOUsuario.obtenerUsuariosBuscador(this, object: VolleyCallback {
            override fun onSuccessResponse(result: String) {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()){
                    var listaUsuariosJson = jsonArray.getJSONObject(i)
                    var usuario = Usuario();

                    usuario.idUsuario = listaUsuariosJson.get("usr_idUsuario").toString().toLong()
                    usuario.nombreUsuario = listaUsuariosJson.get("usr_nombreUsuario").toString()
                    usuario.nombreCompletoUsuario = listaUsuariosJson.get("usr_nombreUsuario").toString()
                    usuario.tipoUsuario = listaUsuariosJson.get("usr_tipoUsuario").toString()

                    listaUsuarios.add(usuario)
                }
            }
        })
        return  listaUsuarios
    }

    fun cargarListaUsuarios( idUsuarioLong: Long, idUsuarioInt: Int, vistaRecycler: RecyclerView?){
        val adapter = CustomAdapterUsuarios()
        adapter.context = this
        adapter.idUsuarioLong = idUsuarioLong
        adapter.idUsuarioInt = idUsuarioInt
        adapter.listaUsuarios = usuariosTuiterAuxiliar

        println("Usuario encontrado de el coustomer; ${adapter.idUsuarioLong}")

        vistaRecycler?.adapter = adapter
    }
}
