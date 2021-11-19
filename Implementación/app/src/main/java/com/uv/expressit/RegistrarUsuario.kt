package com.uv.expressit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Usuario
import org.json.JSONException
import org.json.JSONObject

class RegistrarUsuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        val fechaNac = findViewById<EditText>(R.id.txtDate)
        val btnRegistro = findViewById<Button>(R.id.btnFinalizarRegistro)
        val txtNombreCompleto = findViewById<EditText>(R.id.txtNombreCompleto)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreo)
        val txtNacimiento = findViewById<EditText>(R.id.txtDate)
        val txtNombreUsuario = findViewById<EditText>(R.id.txtNombreUsuario)
        val txtContraUno = findViewById<EditText>(R.id.txtContraseñaUno)
        val txtContraDos = findViewById<EditText>(R.id.txtContraseñaDos)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)


        fechaNac.setOnClickListener{ mostrarDatePicker() }

        btnRegistro.setOnClickListener{

            val nombreCompleto = txtNombreCompleto.text.toString()
            val correo = txtCorreo.text.toString()
            val nacimiento = txtNacimiento.text.toString()
            val nombreUsuario = txtNombreUsuario.text.toString()
            val contraUno = txtContraUno.text.toString()
            val contraDos = txtContraDos.text.toString()
            val descripcion = txtDescripcion.text.toString()
            val estatus = 1
            val tipoUsuario = "Comun"

            //Comprobacion existencia nombre usuario
            try{
                DAOUsuario.obtenerNombresUsuarios(nombreUsuario, this,
                    object : VolleyCallback {
                        override fun onSuccessResponse(result: String) {
                            try{

                                var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))
                                var usuarioObtenido = ""
                                usuarioObtenido = jsonObtenido.get("usr_nombreUsuario").toString()
                                println("usuario obtenido; $usuarioObtenido")

                                if(nombreUsuario.equals(usuarioObtenido)){
                                    Toast.makeText(this@RegistrarUsuario, "Este nombre de usuario ya se encuentra en uso. Prueba con otro!", Toast.LENGTH_LONG).show()
                                    txtNombreUsuario.setText("")
                                }

                            }catch(jex: JSONException){
                                //Catch utilizado de esta manera, debido a que al obtener un nombreUsuario si no esta en uso regresa un null
                                //por ende ingresa a este catch

                                //Verificar coincidencia en contraseñas
                                if(!(contraUno.equals(contraDos))){
                                    val txtPrimeraContra = findViewById<EditText>(R.id.txtContraseñaUno)
                                    val txtSegundaContra = findViewById<EditText>(R.id.txtContraseñaDos)
                                    txtPrimeraContra.setText("")
                                    txtSegundaContra.setText("")

                                    Toast.makeText(this@RegistrarUsuario, "La contraseña debe coincidir!", Toast.LENGTH_SHORT).show()
                                } else{

                                    //Comprobacion campos vacios
                                    if(!(nombreCompleto.isEmpty() || correo.isEmpty() || nacimiento.isEmpty() || nombreUsuario.isEmpty() ||
                                                contraUno.isEmpty() || contraDos.isEmpty() || descripcion.isEmpty())){

                                        try{
                                            DAOUsuario.registrarNuevoUsuario(nombreUsuario, descripcion, estatus, nombreCompleto, correo, tipoUsuario, contraUno, nacimiento, this@RegistrarUsuario)
                                            Toast.makeText(this@RegistrarUsuario, "Cuenta creada exitosamente!", Toast.LENGTH_SHORT).show()
                                            finish()
                                        } catch(e: Exception){
                                            Toast.makeText(this@RegistrarUsuario, "Ocurrio un error al registrar. Intente más tarde", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    else{
                                        Toast.makeText(this@RegistrarUsuario, "Favor de no dejar campos vacíos!", Toast.LENGTH_SHORT).show()
                                    }
                                }

                            }
                        }
                    })
            }catch (exception: Exception){
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        val fechaNac = findViewById<EditText>(R.id.txtDate)
        val mesAux = month + 1
        fechaNac.setText("$day/$mesAux/$year")
    }


}