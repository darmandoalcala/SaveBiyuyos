package com.fergodev.savebiyuyos

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AhorrosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ahorros)

        val ahorrosList = listOf(
            "Vacaciones -> $35000 -> abonado: $0 -> 25/01/2025",
        )

        val btnVer = findViewById<Button>(R.id.btnVer)
        val spinnerAhorros: Spinner = findViewById(R.id.spinnerAhorros)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ahorrosList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAhorros.adapter = adapter

        btnVer.setOnClickListener {
            spinnerAhorros.performClick() // Opcional: muestra la lista automáticamente
        }
    }
}




