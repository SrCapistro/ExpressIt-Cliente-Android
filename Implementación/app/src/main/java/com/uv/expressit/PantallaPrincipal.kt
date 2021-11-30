package com.uv.expressit

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.gson.JsonArray
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PantallaPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var pantallaInicio: Intent
    private lateinit var pantallaPerfil: Intent
    private lateinit var pantallaConfiguration: Intent
    private var idUsuarioLogeado: Long? = null
    private var tipoUsuario: String? = ""
    private var idUltimaEntrada: Long? = 0
    private var nombreUsuario: String? = ""
    private var listaEntradas: MutableList<Entrada> = ArrayList()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)


        val navView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val imageView = headerView.findViewById<ImageView>(R.id.nav_header_imageView)
        val lbNombreUsuario = headerView.findViewById<TextView>(R.id.nav_header_textView)


        val bundle = intent.extras
        idUsuarioLogeado = bundle?.getLong("idUsuarioLogeado")
        tipoUsuario = bundle?.getString("tipoUsuario")
        nombreUsuario = bundle?.getString("nombreUsuario")

        lbNombreUsuario.text = "@"+nombreUsuario
        cargarImagenUsuario(imageView, nombreUsuario)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        val recyclerView = findViewById<RecyclerView>(R.id.vistaEntradas)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        val date = String.format(currentDate)


        obtenerEntradas(idUsuarioLogeado,10000000000,recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)){
                    obtenerEntradas(idUsuarioLogeado, idUltimaEntrada, recyclerView)
                }
            }

        })

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)



        val btnPublicarEntrada = findViewById<Button>(R.id.btnExpressIt)
        btnPublicarEntrada.setOnClickListener(){
            val pantallaEntrada = Intent(this, RegistrarEntrada::class.java)
            pantallaEntrada.putExtra("idUsuario", idUsuarioLogeado)
            startActivity(pantallaEntrada)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

    }

    fun cargarImagenUsuario(imageView: ImageView, nombreUsuario: String?){
        var urlService = "http://192.168.100.4:4000/files/media/pictures/"+nombreUsuario // -> Capi
        //val urlService = "http://26.191.102.84:4000/files/media/pictures/"+nombreUsuario
        //val urlService = "http://192.168.0.21:4000/files/media/pictures/"+nombreUsuario  //-> Zuriel

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


    fun obtenerEntradas(idUsuario: Long?,idEntrada: Long?, vistaRecycler: RecyclerView?){
        DAOEntrada.obtenerEntradasDeSeguidos(idUsuario, idEntrada, this@PantallaPrincipal,  object : VolleyCallback{
            override fun onSuccessResponse(result: String) {
                val jsonArray = JSONArray(result)
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
                    idUltimaEntrada = entradaRecibida.idEntrada
                    listaEntradas.add(entradaRecibida)

                    //listaEntradas.sortBy { entradaRecibida.fechaEntrada }

                }
                for(i in 0 until listaEntradas.size){
                    DAOEntrada.obtenerHashtagEntrada(listaEntradas[i].idEntrada, this@PantallaPrincipal, object: VolleyCallback{
                        override fun onSuccessResponse(result: String) {
                            listaEntradas[i].listaHashtags.clear()
                            var listaHashtags = JSONArray(result)

                            for(j in 0 until listaHashtags.length()){
                                var json = listaHashtags.getJSONObject(j)
                                listaEntradas[i].listaHashtags.add(json.get("htg_nombre").toString())
                            }
                            val adapter = CustomAdapter()
                            adapter.idUsuario = idUsuarioLogeado!!
                            adapter.listaEntradas = listaEntradas
                            adapter.context = this@PantallaPrincipal
                            adapter.tipoUsuario = tipoUsuario.toString()
                            vistaRecycler?.adapter = adapter
                        }
                    })
                }

            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_Inicio -> {
                this.recreate()
            }

            R.id.nav_item_buscar ->{
                val pantallaBuscar = Intent(this, BuscarUsuarioHashtag::class.java)
                pantallaBuscar.putExtra("idUsuario", idUsuarioLogeado)
                pantallaBuscar.putExtra("tipoUsuario", tipoUsuario)
                startActivity(pantallaBuscar)
            }
            R.id.nav_item_Perfil -> {
                val pantallaPerfil = Intent(this, PantallaPerfil::class.java)
                pantallaPerfil.putExtra("idUsuario", idUsuarioLogeado)
                pantallaPerfil.putExtra("tipoUsuario", tipoUsuario)
                pantallaPerfil.putExtra("nombreUsuario", nombreUsuario)
                pantallaPerfil.putExtra("perfilPersonal", true)
                startActivity(pantallaPerfil)
            }
            R.id.nav_item_configuración -> {
                val pantallaEditarUsuario = Intent(this, ModificarUsuario::class.java)
                pantallaEditarUsuario.putExtra("idUsuario", idUsuarioLogeado)
                startActivity(pantallaEditarUsuario)
            }
            R.id.nav_item_CerrarSesion -> {
                this.finish()
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?){
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
         super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}