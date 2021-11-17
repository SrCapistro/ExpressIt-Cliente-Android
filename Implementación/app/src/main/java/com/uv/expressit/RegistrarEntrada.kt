package com.uv.expressit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.uv.expressit.DAO.DAOEntrada

class RegistrarEntrada : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_entrada)

        val bundle = intent.extras
        val idUsuarioPublicador = bundle?.getLong("idUsuario")

        val btnSalir = findViewById<Button>(R.id.btnRegresar)
        val btnPublicar = findViewById<Button>(R.id.btnPublicar)
        val txtEntrada = findViewById<EditText>(R.id.txtEntrada)

        btnSalir.setOnClickListener(){
            finish()
        }

        btnPublicar.setOnClickListener(){
            val entradaContenido = txtEntrada.text.toString()
            if(entradaContenido.isBlank()){
                Toast.makeText(this, "Debe escribir su entrada", Toast.LENGTH_LONG).show()
            }else{
                try{
                    DAOEntrada.registrarEntrada(idUsuarioPublicador, entradaContenido, this)
                    Toast.makeText(this, "Su entrada se publicó exitosamente", Toast.LENGTH_LONG).show()
                    finish()
                }catch(e: Exception){
                    Toast.makeText(this, "Ocurrió un error, intente más tarde", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}