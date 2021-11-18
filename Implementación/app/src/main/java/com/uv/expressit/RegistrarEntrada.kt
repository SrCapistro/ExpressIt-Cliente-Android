package com.uv.expressit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.uv.expressit.DAO.DAOEntrada
import com.uv.expressit.DAO.JSONUtils
import com.uv.expressit.Interfaces.VolleyCallback
import com.uv.expressit.POJO.Entrada
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.*

class RegistrarEntrada : AppCompatActivity() {
    var hashtags: MutableList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_entrada)

        val bundle = intent.extras
        val idUsuarioPublicador = bundle?.getLong("idUsuario")

        val btnSalir = findViewById<Button>(R.id.btnRegresar)
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

        btnSalir.setOnClickListener(){
            finish()
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
                }else{
                    DAOEntrada.registrarEntrada(idUsuarioPublicador, entradaContenido, this)
                }
            }
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