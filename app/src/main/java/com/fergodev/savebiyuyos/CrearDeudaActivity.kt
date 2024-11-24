package com.fergodev.savebiyuyos

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fergodev.savebiyuyos.databinding.ActivityCrearDeudaBinding
import kotlinx.coroutines.launch

class CrearDeudaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearDeudaBinding
    private lateinit var db: AppDatabase
    private lateinit var deudaDao: DeudaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearDeudaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            db = AppDatabase.getInstance(this)
            deudaDao = db.deudaDao()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Err: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Configurar el botón "Crear Deuda"
        binding.btnCrear.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val monto = binding.etMonto.text.toString().toDoubleOrNull()?: 0.0
            val abonado = binding.etAbono.text.toString().toDoubleOrNull()?: 0.0

            if (nombre.isBlank() || monto == 0.0) {
                Toast.makeText(this, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear y guardar la deuda
            val deuda = Deuda(nombre = nombre, monto = monto, abonado = abonado)
            lifecycleScope.launch {
                try {
                    deudaDao.insertarDeuda(deuda)
                    Toast.makeText(this@CrearDeudaActivity, "Deuda creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Finalizar la actividad
                } catch (e: Exception) {
                    Toast.makeText(this@CrearDeudaActivity, "Error al guardar la deuda", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
