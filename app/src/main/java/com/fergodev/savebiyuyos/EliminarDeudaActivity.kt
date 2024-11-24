package com.fergodev.savebiyuyos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class EliminarDeudaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_deuda)  // Ahora usando el nombre correcto del XML

        // Datos de ejemplo para el Spinner
        val deudasMock = listOf("Pago Coppel - $2400", "Prestamo vecino - $250")

        // Configuración del adaptador para el Spinner
        val spinnerDeudas = findViewById<Spinner>(R.id.spinnerDeudas)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deudasMock)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDeudas.adapter = adapter
    }
}

