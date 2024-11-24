package com.fergodev.savebiyuyos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fergodev.savebiyuyos.Gasto
import com.fergodev.savebiyuyos.R

class GastosAdapter(
    private val gastos: MutableList<Gasto>,
    private val onDeleteGasto: (Gasto) -> Unit // Callback para eliminar un gasto
) : RecyclerView.Adapter<GastosAdapter.GastoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GastoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gasto, parent, false)
        return GastoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GastoViewHolder, position: Int) {
        val gasto = gastos[position]
        holder.nombreTextView.text = gasto.nombre
        holder.montoTextView.text = "$${gasto.monto}"

        // Manejar el clic en el botón de eliminar
        holder.eliminarButton.setOnClickListener {
            onDeleteGasto(gasto) // Ejecutar la función de eliminación
        }
    }

    override fun getItemCount(): Int = gastos.size

    class GastoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.textNombreGasto)
        val montoTextView: TextView = view.findViewById(R.id.textMontoGasto)
        val eliminarButton: Button = view.findViewById(R.id.btnEliminarGasto)
    }

    // Método para eliminar el gasto de la lista
    fun eliminarGasto(gasto: Gasto) {
        val position = gastos.indexOf(gasto)
        if (position != -1) {
            gastos.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
