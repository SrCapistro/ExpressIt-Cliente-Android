package com.uv.expressit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Usuario
import org.json.JSONArray
import org.json.JSONObject

class BuscarUsuarioHashtag : AppCompatActivity() {

    private var idUsuario: Long? = null
    private var tipoUsuario: String? = ""
    private var usuarioTipo: String = ""
    private var idUsuarioInt: Int = -1
    private var cadenaString: String = ""
    private var valorLong: Long = 0
    var usuariosTuiter: MutableList<Usuario> = ArrayList()
    var usuariosTuiterAuxiliar: MutableList<Usuario> = ArrayList()

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
                            ////////////////////////No jala el filtro :V
                            cargarListaUsuarios(valorLong, idUsuarioInt, recyclerView)

                        }else{
                            Toast.makeText(this@BuscarUsuarioHashtag, "No se encontraron resultados", Toast.LENGTH_LONG).show()
                        }
                    }
                    if(buscarHastag.isChecked){

                        if(!cadenaString[0].equals("#")){
                            //buscar por hashtag

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