package com.fergodev.savebiyuyos

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class EgresosActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var gastoDao: GastoDao
    private lateinit var adapterFijos: GastosAdapter
    private lateinit var adapterExtras: GastosAdapter
    private val gastosFijos = mutableListOf<Gasto>()
    private val gastosExtras = mutableListOf<Gasto>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_egresos)

        // Inicializar la base de datos y el DAO
        try {
            db = AppDatabase.getInstance(this)
            gastoDao = db.gastoDao()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al inicializar la base de datos: ${e.message}", Toast.LENGTH_LONG).show()
            return // Salimos del método si ocurre un error
        }

        // Inicialización de RecyclerView y adaptadores
        val recyclerFijos = findViewById<RecyclerView>(R.id.recyclerGastosFijos)
        val recyclerExtras = findViewById<RecyclerView>(R.id.recyclerGastosExtras)

        recyclerFijos.layoutManager = LinearLayoutManager(this)
        recyclerExtras.layoutManager = LinearLayoutManager(this)

        // Configuración de los adaptadores
        adapterFijos = GastosAdapter(gastosFijos) { gasto -> eliminarGasto(gasto) }
        adapterExtras = GastosAdapter(gastosExtras) { gasto -> eliminarGasto(gasto) }

        recyclerFijos.adapter = adapterFijos
        recyclerExtras.adapter = adapterExtras

        // Cargar los gastos fijos y extras
        cargarGastos()

        // Configurar botones para agregar gastos
        val btnAgregarGastoFijo = findViewById<AppCompatButton>(R.id.btnAgregarGastoFijo)
        val btnAgregarGastoExtra = findViewById<AppCompatButton>(R.id.btnAgregarGastoExtra)

        btnAgregarGastoFijo.setOnClickListener {
            mostrarDialogoAgregarGasto("Fijo")
        }

        btnAgregarGastoExtra.setOnClickListener {
            mostrarDialogoAgregarGasto("Extra")
        }
    }

    // Función para cargar los gastos de la base de datos
    private fun cargarGastos() {
        lifecycleScope.launch {
            try {
                // Cargar gastos fijos y extras desde la base de datos
                val gastosFijosDB = gastoDao.obtenerGastosPorTipo("Fijo")
                val gastosExtrasDB = gastoDao.obtenerGastosPorTipo("Extra")

                // Limpiar las listas antes de agregar los nuevos datos
                gastosFijos.clear()
                gastosExtras.clear()

                // Agregar los gastos a las listas
                gastosFijos.addAll(gastosFijosDB)
                gastosExtras.addAll(gastosExtrasDB)

                // Notificar a los adaptadores para actualizar la UI
                adapterFijos.notifyDataSetChanged()
                adapterExtras.notifyDataSetChanged()

            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("EgresosActivity", "Error al cargar los gastos: ${e.message}")
                Toast.makeText(this@EgresosActivity, "Error al cargar los gastos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para eliminar un gasto
    private fun eliminarGasto(gasto: Gasto) {
        lifecycleScope.launch {
            try {
                // Eliminar el gasto de la base de datos
                gastoDao.eliminarGasto(gasto)
                // Recargar los datos de los gastos
                cargarGastos()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@EgresosActivity, "Error al eliminar el gasto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para mostrar el diálogo y agregar un nuevo gasto
    private fun mostrarDialogoAgregarGasto(tipo: String) {
        val builder = AlertDialog.Builder(this)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val inputNombre = EditText(this).apply {
            hint = "Nombre del Gasto"
        }
        val inputMonto = EditText(this).apply {
            hint = "Monto"
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(inputNombre)
        layout.addView(inputMonto)

        builder.setTitle("Agregar Gasto $tipo")
        builder.setView(layout)

        builder.setPositiveButton("Agregar") { _, _ ->
            val nombre = inputNombre.text.toString()
            val monto = inputMonto.text.toString().toDouble()

            if (nombre.isNotEmpty() && monto != 0.0) {
                lifecycleScope.launch {
                    try {
                        // Crear un nuevo gasto y agregarlo a la base de datos
                        val nuevoGasto = Gasto(nombre = nombre, tipoGasto = tipo, monto = monto)
                        gastoDao.insertarGasto(nuevoGasto)
                        // Recargar los datos de los gastos
                        cargarGastos()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@EgresosActivity, "Error al agregar el gasto", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor ingresa un nombre y un monto", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}
