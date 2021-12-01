package com.uv.expressit

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.toolbox.ImageRequest
import com.uv.expressit.DAO.DAOUsuario
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import org.json.JSONObject
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.uv.expressit.DAO.FileDataPart
import com.uv.expressit.DAO.VolleyFileUploadRequest
import org.json.JSONException


class ModificarUsuario : AppCompatActivity() {

    private var idUsuario: Long? = null
    private var idUsuarioInt: Int = -1
    lateinit var imageView: ImageView
    private var imageData: ByteArray? = null
    private var cadenaUrl: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_usuario)

        val bundle = intent.extras

        idUsuario = bundle?.getLong("idUsuario")

        if (idUsuario != null){
            var valorLong: Long = idUsuario as Long;
            idUsuarioInt =  valorLong.toInt()
        }

        val btnEliminarCuenta = findViewById<Button>(R.id.btnEliminarCuenta)
        imageView = findViewById<ImageView>(R.id.imgPerfilModificar)
        val txtNombreCompleto = findViewById<EditText>(R.id.txtNombreCompletoModificar)
        val fechaNac = findViewById<EditText>(R.id.txtDateModificar)
        val btnSubirImagen = findViewById<Button>(R.id.btnSubirImagen)
        val txtCorreo = findViewById<EditText>(R.id.txtCorreoModificar)
        val txtNombreUsuario = findViewById<EditText>(R.id.txtNombreUsuarioModificar)
        val txtContraUno = findViewById<EditText>(R.id.txtContraseñaUnoModificar)
        val txtContraDos = findViewById<EditText>(R.id.txtContraseñaDosModificar)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcionModificar)
        val btnActualizarCuenta = findViewById<Button>(R.id.btnFinalizarModificacion)

        var nombreCompleto = "";
        var fechaNacimiento = "";
        var correoElectronico = "";
        var nombreUsuario = "";
        var contrasenia = "";
        var descripcion = "";

        btnEliminarCuenta.setOnClickListener{

            AlertDialog.Builder(this)
                .setTitle("Eliminación")
                .setMessage("Esta seguro en dar de baja la cuenta")
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->
                        DAOUsuario.darDeBajaUsuario(idUsuarioInt,this)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        this.finish()

                    })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which ->
                        //botón cancel pulsado
                    })
                .show()
        }

        fechaNac.setOnClickListener{ mostrarDatePicker() }

        if (idUsuarioInt != -1){
            DAOUsuario.obtenerInformacionUsuario(idUsuarioInt, this, object: VolleyCallback{
                override fun onSuccessResponse(result: String) {
                    var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))

                    nombreCompleto = jsonObtenido.get("usr_nombre").toString()
                    if(nombreCompleto.equals("null")) {
                        nombreCompleto = ""
                    }
                    fechaNacimiento = jsonObtenido.get("usr_fechaNacimiento").toString()
                    if(fechaNacimiento.equals("null")) {
                        fechaNacimiento = ""
                    }
                    correoElectronico = jsonObtenido.get("usr_correo").toString()
                    if(correoElectronico.equals("null")) {
                        correoElectronico = ""
                    }
                    nombreUsuario = jsonObtenido.get("usr_nombreUsuario").toString()
                    if(nombreUsuario.equals("null")) {
                        nombreUsuario = ""
                    }
                    contrasenia = jsonObtenido.get("usr_contraseña").toString()
                    if(contrasenia.equals("null")) {
                        contrasenia = ""
                    }
                    descripcion = jsonObtenido.get("usr_descripcion").toString()
                    if(descripcion.equals("null")) {
                        descripcion = ""
                    }
                    txtNombreCompleto.setText(nombreCompleto)
                    fechaNac.setText(fechaNacimiento)
                    txtCorreo.setText(correoElectronico)
                    txtNombreUsuario.setText(nombreUsuario)
                    txtDescripcion.setText(descripcion)
                }
            })
        }

        DAOUsuario.obtenerNumeroDeImagenes(idUsuarioInt, this, object: VolleyCallback{
            override fun onSuccessResponse(result: String) {
                var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))

                var numero: Int = jsonObtenido.get("numeroFoto").toString().toInt()

                println("Usuario Obtenido; $numero")

                if( numero > 0){
                    cargarImagenUsuario(imageView, idUsuarioInt)
                }
            }
        })


        btnSubirImagen.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*")
            startActivityForResult(intent, 1)
        }

        btnActualizarCuenta.setOnClickListener{

            val nombreCompleto = txtNombreCompleto.text.toString()
            val correo = txtCorreo.text.toString()
            val nacimiento = fechaNac.text.toString()
            val nombreUsuario = txtNombreUsuario.text.toString()
            var contraUno = txtContraUno.text.toString()
            val contraDos = txtContraDos.text.toString()
            val descripcion = txtDescripcion.text.toString()

            AlertDialog.Builder(this)
                .setTitle("Modificacion")
                .setMessage("Esta seguro de modificar la cuenta")
                .setPositiveButton(android.R.string.ok,
                    DialogInterface.OnClickListener { dialog, which ->

                        DAOUsuario.existeUsuarioRepetido(idUsuarioInt, nombreUsuario, this, object: VolleyCallback{
                            override fun onSuccessResponse(result: String) {
                                var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))

                                var numero: Int = jsonObtenido.get("cantidad").toString().toInt()

                                if(numero == 0){

                                    if(nombreCompleto.isEmpty()){
                                        Toast.makeText(this@ModificarUsuario, "No puede dejar el campo 'Nombre Completo' vacio!!!", Toast.LENGTH_SHORT).show()
                                    } else if(nacimiento.isEmpty()){
                                        Toast.makeText(this@ModificarUsuario, "No puede dejar el campo 'Fecha de Nacimiento' vacio!!!", Toast.LENGTH_SHORT).show()
                                    } else if(correo.isEmpty()){
                                        Toast.makeText(this@ModificarUsuario, "No puede dejar el campo 'Correo Electronico' vacio!!!", Toast.LENGTH_SHORT).show()
                                    } else if(!(contraUno.equals(contraDos))){
                                            val txtPrimeraContra = findViewById<EditText>(R.id.txtContraseñaUnoModificar)
                                            val txtSegundaContra = findViewById<EditText>(R.id.txtContraseñaDosModificar)
                                            txtPrimeraContra.setText("")
                                            txtSegundaContra.setText("")

                                            Toast.makeText(this@ModificarUsuario, "La contraseña debe coincidir!!!", Toast.LENGTH_SHORT).show()
                                    } else{
                                        try{

                                            if(contraUno.isEmpty()){
                                                contraUno = contrasenia
                                            }
                                            println("contraseñaVacia; $contraUno")
                                            DAOUsuario.modificarUsuario(idUsuarioInt,nombreUsuario, descripcion, nombreCompleto, correo, contraUno, nacimiento, this@ModificarUsuario)

                                            if(!cadenaUrl.equals("")){
                                                DAOUsuario.eliminarFotoPerfil(idUsuarioInt, this@ModificarUsuario,object: VolleyCallback{
                                                    override fun onSuccessResponse(result: String) {
                                                        var jsonObtenido = JSONObject(JSONUtils.parsearJson(result))

                                                        var numero: Int = jsonObtenido.get("respuesta").toString().toInt()

                                                        println("eliminacionFotoPerfil; $numero")
                                                        Thread.sleep(500)

                                                         if(numero == 200){
                                                            subirFotoPerfil(idUsuarioInt)
                                                         }else{
                                                            subirFotoPerfil(idUsuarioInt)
                                                         }
                                                    }

                                                })
                                            }

                                        } catch(e: Exception){
                                            Toast.makeText(this@ModificarUsuario, "Ocurrio un error al modificar. Intente más tarde", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }else{
                                    Toast.makeText(this@ModificarUsuario, "El nombre de usuario ya existe. Intente con otro!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        })
                        if(!contraUno.isEmpty() && !contraDos.isEmpty()){
                            val inicioSesion = Intent(this, MainActivity::class.java)
                            startActivity(inicioSesion)
                            this.finish()
                            Toast.makeText(this@ModificarUsuario, "Cuenta modificada exitosamente!!!. Inicie sesión nuevamente", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ModificarUsuario, "Cuenta modificada exitosamente!", Toast.LENGTH_SHORT).show()
                        }
                    })
                .setNegativeButton(android.R.string.cancel,
                    DialogInterface.OnClickListener { dialog, which ->
                        //botón cancel pulsado
                    })
                .show()
        }
    }
    fun subirFotoPerfil(idUsuario: Int) {
        imageData?: return

        var format = validarFormato(cadenaUrl);

        var url = "http://192.168.100.4:4000/files/media/usuarios/"+idUsuario+"/"+format // -> Capi
        //var url = "http://26.191.102.84:4000/files/media/usuarios/"+ idUsuario +"/"+ format // Brandon
        //val url = "http://192.168.0.21:4000/files/media/usuarios/"+ idUsuario +"/"+ format  //-> Zuriel

        println("eliminacionFotoPerfil; $idUsuario")
        println("FomatoSubido; $format")

        val request = object : VolleyFileUploadRequest(Method.POST, url, Response.Listener {
                println("response is: $it")
            },
            Response.ErrorListener {
                println("error is: $it")
            }
        ) {
            override fun getByteData(): MutableMap<String, FileDataPart> {
                var params = HashMap<String, FileDataPart>()
                params["imgFile"] = FileDataPart("imgFile", imageData!!, "jpeg")
                params["format"] =
                return params
            }
        }
        Volley.newRequestQueue(this).add(request)
    }

    fun crearDatosDeImagen(uri: Uri) {
        val inputStream = contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val uri = data?.data
            cadenaUrl = uri.toString()
            println("eliminacionFotoPerfil; $cadenaUrl")
            if (uri != null) {
                imageView.setImageURI(uri)
                crearDatosDeImagen(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun mostrarDatePicker() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    fun onDateSelected(day:Int, month:Int, year:Int){
        val fechaNac = findViewById<EditText>(R.id.txtDateModificar)
        val mesAux = month + 1
        fechaNac.setText("$day/$mesAux/$year")
    }
    fun cargarImagenUsuario(imageView: ImageView, idUsuarioInt: Int){
        var urlService = "http://192.168.100.4:4000/files/media/profile_pictures/"+idUsuarioInt // -> Capi
        //val urlService = "http://26.191.102.84:4000/files/profile_pictures/"+idUsuarioInt
        //val urlService = "http://192.168.0.21:4000/files/profile_pictures/"+idUsuarioInt //-> Zuriel
        println("Usuario Obtenido; $idUsuarioInt")

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

    fun validarFormato(cadena: String): String{
        var formato = "1"

        if(cadena.contains("jpeg", ignoreCase = true)){
            formato = "jpeg"
        }
        if(cadena.contains("jpg", ignoreCase = true)){
            formato = "jpg"
        }
        if(cadena.contains("png", ignoreCase = true)){
            formato = "png"
        }
        if(cadena.contains("bmp", ignoreCase = true)){
            formato = "bmp"
        }
        if(cadena.contains("gif", ignoreCase = true)){
            formato = "gif"
        }
        if(cadena.contains("svg", ignoreCase = true)){
            formato = "svg"
        }
        if(cadena.contains("false", ignoreCase = true)){
            formato = "jpg"
        }
        return formato;
    }
}