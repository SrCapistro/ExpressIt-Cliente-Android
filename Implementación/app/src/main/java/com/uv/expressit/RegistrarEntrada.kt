package com.uv.expressit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.annotation.RequiresApi
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.DAO.FileDataPart
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.DAO.VolleyFileUploadRequest
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.*

class RegistrarEntrada : AppCompatActivity() {
    var hashtags: MutableList<String> = ArrayList()
    lateinit var imageView: ImageView
    private var imageData: ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_entrada)

        val bundle = intent.extras
        val idUsuarioPublicador = bundle?.getLong("idUsuario")

        imageView = findViewById<ImageView>(R.id.entradaFoto)
        val btnSubirArchivo = findViewById<Button>(R.id.btnAñadirArchivo)
        val btnPublicar = findViewById<Button>(R.id.btnPublicar)
        val txtEntrada = findViewById<EditText>(R.id.txtEntrada)
        val txtHashTagIngresado = findViewById<EditText>(R.id.txtHashTagEscrito)
        val txtHashTags = findViewById<TextView>(R.id.txtHashTags)
        val btnAgregarHashtag = findViewById<Button>(R.id.btnAgregarHashtag)

        btnAgregarHashtag.setOnClickListener(){
            val hashtag = txtHashTagIngresado.text.toString()
            txtHashTags.setText("${txtHashTags.text.toString()} $hashtag,")
            hashtags.add(hashtag)
            println("hashtags: ${hashtags[0]}")
        }

        btnSubirArchivo.setOnClickListener{
            var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*")
            startActivityForResult(intent, 1)

        }



        btnPublicar.setOnClickListener(){
            val entradaContenido = txtEntrada.text.toString()
            if(entradaContenido.isBlank()){
                Toast.makeText(this, "Debe escribir su entrada", Toast.LENGTH_LONG).show()
            }else{
                if(hashtags.size > 0) {
                    try {
                        DAOEntrada.registrarEntrada(idUsuarioPublicador, entradaContenido, this)
                        try {
                            Thread.sleep(500)
                            registrarHashTags()
                            this.finish()
                        } catch (exc: Exception) {
                            println("Error $exc")
                            Toast.makeText(
                                this,
                                "Ocurrió un error, intente después",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            "Ocurrió un error, intente más tarde",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    if(imageData != null){
                        DAOEntrada.obtenerUltimaEntradaRegistrada(this, object: VolleyCallback{
                            override fun onSuccessResponse(result: String) {
                                var entradaRegistrada = JSONObject(JSONUtils.parsearJson(result))
                                var entradaRecibida = Entrada()
                                entradaRecibida.idEntrada = entradaRegistrada.get("ent_idEntrada").toString().toLong()
                                subirFoto(entradaRecibida.idEntrada)
                            }

                        })
                    }
                }else{
                    DAOEntrada.registrarEntrada(idUsuarioPublicador, entradaContenido, this)
                    Toast.makeText(this@RegistrarEntrada, "Su entrada se publicó exitosamente", Toast.LENGTH_LONG).show()
                    if(imageData != null){
                        DAOEntrada.obtenerUltimaEntradaRegistrada(this, object: VolleyCallback{
                            override fun onSuccessResponse(result: String) {
                                var entradaRegistrada = JSONObject(JSONUtils.parsearJson(result))
                                var entradaRecibida = Entrada()
                                entradaRecibida.idEntrada = entradaRegistrada.get("ent_idEntrada").toString().toLong()
                                subirFoto(entradaRecibida.idEntrada)
                            }

                        })
                    }
                    this.finish()
                }
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            val uri = data?.data
            if (uri != null) {
                imageView.setImageURI(uri)
                crearDatosDeImagen(uri)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun subirFoto(idEntrada: Long?){
        imageData?: return
        val url = "http://expressit.ddns.net/files/media/entradas/"+idEntrada
        val request = object: VolleyFileUploadRequest(
            Method.POST, url, Response.Listener { println(it) },
            Response.ErrorListener {
                println("error is: $it")
            }
        ){
            override fun getByteData(): MutableMap<String, FileDataPart> {
                var params = HashMap<String, FileDataPart>()
                params["archivo"] = FileDataPart("archivo", imageData!!, "jpeg")
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

    fun registrarHashTags(){
        try{
            DAOEntrada.obtenerUltimaEntradaRegistrada(this,
                object : VolleyCallback{
                    override fun onSuccessResponse(result: String) {

                        var entradaRegistrada = JSONObject(JSONUtils.parsearJson(result))
                        var entradaRecibida = Entrada()
                        entradaRecibida.idEntrada = entradaRegistrada.get("ent_idEntrada").toString().toLong()
                        println("Entrada recibida: ${entradaRecibida.idEntrada}")
                        try{
                            DAOEntrada.obtenerUltimoIdHashtagRegistrado(this@RegistrarEntrada,
                                object : VolleyCallback{
                                    override fun onSuccessResponse(result: String) {
                                        var hashtagRegistrado = JSONObject(JSONUtils.parsearJson(result))
                                        var idHastag : Long = 0
                                        idHastag = hashtagRegistrado.get("idHashtag").toString().toLong()
                                        println("último hashtag registrado: ${idHastag}")

                                        for(i in 0 until hashtags.size){
                                            DAOEntrada.registrarHashTags(hashtags[i], this@RegistrarEntrada)
                                        }
                                        Thread.sleep(500)
                                        DAOEntrada.obtenerHashtagsRecienRegistrados(idHastag, this@RegistrarEntrada,
                                            object: VolleyCallback{
                                                override fun onSuccessResponse(result: String) {
                                                    val jsonArray = JSONArray(result)
                                                    val idHashtags : MutableList<Long> = ArrayList()
                                                    for (i in 0 until jsonArray.length()){
                                                        var entradaJson = jsonArray.getJSONObject(i)
                                                        var idHashtagRegistrado: Long = 0
                                                        idHashtagRegistrado = entradaJson.get("idHashtag").toString().toLong()
                                                        idHashtags.add(idHashtagRegistrado)
                                                        println("Hashtags registrados: ${idHashtags[i]}")
                                                    }
                                                    Thread.sleep(500)
                                                    try{
                                                        for(i in 0 until idHashtags.size){
                                                            DAOEntrada.asociarHashTags(idHashtags[i], entradaRecibida.idEntrada, this@RegistrarEntrada)
                                                            Toast.makeText(this@RegistrarEntrada, "Su entrada se publicó exitosamente", Toast.LENGTH_LONG).show()
                                                        }
                                                    }catch(exce: Exception){
                                                        println("Error al asociar hashtags: ${exce.message}")
                                                        Toast.makeText(this@RegistrarEntrada, "Error al publicar entrada", Toast.LENGTH_LONG).show()
                                                    }

                                                }
                                            })
                                    }
                                })




                        }catch(e:Exception){
                            Toast.makeText(this@RegistrarEntrada, "Ocurrió un error, intente después", Toast.LENGTH_LONG).show()
                            println("Error en hashtag: $e")
                        }
                    }
                })
        }catch(e: Exception){
            Toast.makeText(this, "Ocurrió un error, intente después", Toast.LENGTH_LONG).show()
            println("Error $e")
        }
    }
}