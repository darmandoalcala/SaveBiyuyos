package com.fergodev.savebiyuyos

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class IngresosActivity : AppCompatActivity() {

    private var totalIngresoFijo: Double = 0.0
    private var totalIngresoExtra: Double = 0.0

    // Instancia de la base de datos
    private lateinit var db: AppDatabase
    private lateinit var ingresoDao: IngresoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingresos)

        // Inicializar la base de datos y el DAO
        try {
            db = AppDatabase.getInstance(this)
            ingresoDao = db.ingresoDao()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Err: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Referencias a los TextViews
        val textIngresoFijoTotal = findViewById<TextView>(R.id.textIngresoFijoTotal)
        val textIngresoExtraTotal = findViewById<TextView>(R.id.textIngresoExtraTotal)
        val textTotalTotal = findViewById<TextView>(R.id.textTotalTotal)

        // Cargar datos existentes desde la base de datos
        cargarIngresos(textIngresoFijoTotal, textIngresoExtraTotal, textTotalTotal)

        // Botón para agregar ingreso fijo
        val btnAgregarIngresoFijo = findViewById<AppCompatButton>(R.id.btnAgregarIngresoFijo)
        btnAgregarIngresoFijo.setOnClickListener {
            mostrarDialogoAgregarIngreso("Ingreso Fijo", textIngresoFijoTotal)
        }

        // Botón para agregar ingreso extra
        val btnAgregarIngresoExtra = findViewById<AppCompatButton>(R.id.btnAgregarIngresoExtra)
        btnAgregarIngresoExtra.setOnClickListener {
            mostrarDialogoAgregarIngreso("Ingreso Extra", textIngresoExtraTotal)
        }
    }

    private fun cargarIngresos(textIngresoFijoTotal: TextView, textIngresoExtraTotal: TextView, textTotalTotal: TextView) {
        // Usar coroutines para realizar operaciones de base de datos en segundo plano
        lifecycleScope.launch {
            // Obtener los ingresos desde la base de datos
            val ingresosFijos = ingresoDao.obtenerIngresosPorTipo("Ingreso Fijo")
            val ingresosExtras = ingresoDao.obtenerIngresosPorTipo("Ingreso Extra")

            // Calcular los totales
            totalIngresoFijo = ingresosFijos.sumOf { it.monto }
            totalIngresoExtra = ingresosExtras.sumOf { it.monto }
            val total = totalIngresoFijo + totalIngresoExtra

            // Actualizar los TextViews
            textIngresoFijoTotal.text = "Total Ingreso Fijo: $$totalIngresoFijo"
            textIngresoExtraTotal.text = "Total Ingreso Extra: $$totalIngresoExtra"
            textTotalTotal.text = "Total Ingresos de este mes: $$total"
        }
    }

    private fun mostrarDialogoAgregarIngreso(tipoIngreso: String, textView: TextView) {
        val builder = AlertDialog.Builder(this)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val inputMonto = EditText(this).apply {
            hint = "Monto"
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(inputMonto)

        builder.setTitle("Agregar $tipoIngreso")
        builder.setView(layout)

        builder.setPositiveButton("Agregar") { _, _ ->
            val monto = inputMonto.text.toString()

            if (monto.isNotEmpty()) {
                val montoDouble = monto.toDouble()

                // Guardar en la base de datos y actualizar la vista
                lifecycleScope.launch {
                    ingresoDao.insertarIngreso(Ingreso(tipo = tipoIngreso, monto = montoDouble))

                    // Actualizar el total y el TextView
                    if (tipoIngreso == "Ingreso Fijo") {
                        totalIngresoFijo += montoDouble
                        textView.text = "Total Ingreso Fijo: $$totalIngresoFijo"
                    } else {
                        totalIngresoExtra += montoDouble
                        textView.text = "Total Ingreso Extra: $$totalIngresoExtra"
                    }
                }
            } else {
                Toast.makeText(this, "Por favor ingresa un monto", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}
