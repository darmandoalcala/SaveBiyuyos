package com.fergodev.savebiyuyos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingresos")
data class Ingreso(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,   //TIPO "Fijo" o "Extra"
    val monto: Double
)

@Entity(tableName = "gastos")
data class Gasto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipoGasto: String,  // "Fijo" o "Extra"
    val monto: Double
)

@Entity(tableName = "ahorros")
data class Ahorro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val descripcion: String,  // Descripción del ahorro
    val montoMeta: Double,    // Monto objetivo
    val abonado: Double,      // Monto abonado hasta el momento
    val fechaMeta: String     // Fecha límite para alcanzar la meta
)

@Entity(tableName = "deudas")
data class Deuda(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,       // Nombre de la deuda
    val monto: Double,        // Monto de la deuda
    val abonado: Double       // Monto abonado hasta el momento
)
/*
@Entity(tableName = "perfil")
data class Perfil(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,    // Nombre del perfil o del usuario
    val email: String,     // Correo electrónico
    val telefono: String?  // Teléfono (opcional)
)
*/
