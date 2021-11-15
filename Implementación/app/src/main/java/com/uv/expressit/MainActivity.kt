package com.uv.expressit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Usuario
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val txtUsuario = findViewById<EditText>(R.id.txtNombreUsuario)
        val txtContraseña = findViewById<EditText>(R.id.txtContraseña)
        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)


        btnIniciarSesion.setOnClickListener{
            var nombreUsuario = txtUsuario.text.toString()
            var contraseña = txtContraseña.text.toString()
            if(nombreUsuario.isBlank() || contraseña.isBlank()){
                Toast.makeText(this, "Debe ingresar un usuario y contraseña validos", Toast.LENGTH_SHORT).show()
            }else{
                obtnerLogin(nombreUsuario, contraseña)
            }
        }

    }

    fun obtnerLogin(nombreUsuairo: String, contraseña: String){
        try{
            DAOUsuario.obtenerLoginUsuario(nombreUsuairo, contraseña, this,
                object : VolleyCallback {
                    override fun onSuccessResponse(result: String) {
                        try{
                            var usuarioLogeado = JSONObject(JSONUtils.parsearJson(result))
                            var usuarioIngresado = Usuario()
                            usuarioIngresado.nombreCompletoUsuario = usuarioLogeado.get("usr_nombre").toString()
                            usuarioIngresado.idUsuario = usuarioLogeado.getLong("usr_idUsuario")
                            usuarioIngresado.nombreUsuario = usuarioLogeado.get("usr_nombreUsuario").toString()
                            usuarioIngresado.correoUsuario = usuarioLogeado.get("usr_correo").toString()
                            usuarioIngresado.descripcionUsuario = usuarioLogeado.get("usr_descripcion").toString()
                            usuarioIngresado.tipoUsuario = usuarioLogeado.getString("usr_tipoUsuario")
                            usuarioIngresado.fechaNacimiento = usuarioLogeado.getString("usr_fechaNacimiento")
                            println(usuarioIngresado.nombreCompletoUsuario)
                            //Codigo para abrir la nueva pantalla y pasarle el usuario

                        }catch(jex: JSONException){
                           jex.printStackTrace()
                        }
                    }
                })
        }catch (exception: Exception){
            Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
        }
    }
}