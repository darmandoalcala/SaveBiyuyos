package com.fergodev.savebiyuyos

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Spinner


class AbonarDeudaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abonar_deuda)  // Ahora usando el nombre correcto del XML

        // Datos de ejemplo para el Spinner
        val deudasMock = listOf("Pago Coppel - $2500", "Prestamo vecino - $250")

        // Configuración del adaptador para el Spinner
        val spinnerDeudas = findViewById<Spinner>(R.id.spinnerDeudas)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deudasMock)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDeudas.adapter = adapter
    }
}
