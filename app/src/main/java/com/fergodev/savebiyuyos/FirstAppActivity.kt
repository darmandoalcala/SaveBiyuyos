package com.fergodev.savebiyuyos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class FirstAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_app)

        // Botones de la sección "Ingresos y Egresos"
        val ingresosButton = findViewById<Button>(R.id.ingresosButton)
        val egresosButton = findViewById<Button>(R.id.egresosButton)

        // Botones de la sección "Deudas"
        val crearDeudaButton = findViewById<Button>(R.id.crearDeudaButton)
        val abonarDeudaButton = findViewById<Button>(R.id.abonarDeudaButton)
        val eliminarDeudaButton = findViewById<Button>(R.id.eliminarDeudaButton)

        // Botón de la sección "Ahorros"
        val ahorrosButton = findViewById<Button>(R.id.AhorrosButton)

        // Botones de la sección "Más"
        val estadoFinancieroButton = findViewById<Button>(R.id.estadoFinancieroButton)
        val ayudaButton = findViewById<Button>(R.id.ayudaButton)

        // Configura los listeners para cada botón

        ingresosButton.setOnClickListener {
            val intent = Intent(this, IngresosActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        egresosButton.setOnClickListener {
            val intent = Intent(this, EgresosActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        crearDeudaButton.setOnClickListener {
            val intent = Intent(this, CrearDeudaActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        abonarDeudaButton.setOnClickListener {
            val intent = Intent(this, AbonarDeudaActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        eliminarDeudaButton.setOnClickListener {
            val intent = Intent(this, EliminarDeudaActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        ahorrosButton.setOnClickListener {
            val intent = Intent(this, AhorrosActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }

        estadoFinancieroButton.setOnClickListener {
            val intent = Intent(this, EstadoFinancieroActivity::class.java)
            startActivity(intent)
        }

        ayudaButton.setOnClickListener {
            val intent = Intent(this, AyudaActivity::class.java) // Reemplaza con la actividad correspondiente
            startActivity(intent)
        }
    }
}
