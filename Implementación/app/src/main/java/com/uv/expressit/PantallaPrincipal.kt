package com.uv.expressit

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONArray
import org.json.JSONObject

class PantallaPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var pantallaInicio: Intent
    private lateinit var pantallaPerfil: Intent
    private lateinit var pantallaConfiguration: Intent
    private var idUsuarioLogeado: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_principal)


        val bundle = intent.extras
        idUsuarioLogeado = bundle?.getLong("idUsuarioLogeado")
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)


        val recyclerView = findViewById<RecyclerView>(R.id.vistaEntradas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        obtenerEntradas(idUsuarioLogeado, recyclerView)
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

    fun obtenerEntradas(idUsuario: Long?, vistaRecycler: RecyclerView){
        DAOEntrada.obtenerEntradasDeSeguidos(idUsuario,this,  object : VolleyCallback{
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
                    println(entradaRecibida.nombreUsuario)
                    listaEntradas.add(entradaRecibida)
                    val adapter = CustomAdapter()
                    adapter.listaEntradas = listaEntradas
                    vistaRecycler.adapter = adapter
                }

            }

        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_item_Inicio -> {
                println(idUsuarioLogeado)
            }
            R.id.nav_item_Perfil -> {
                val pantallaPerfil = Intent(this, PantallaPerfil::class.java)
                intent.putExtra("idUsuario", idUsuarioLogeado)
                startActivity(pantallaPerfil)
            }
            R.id.nav_item_configuración -> {

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