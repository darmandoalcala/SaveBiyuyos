package com.fergodev.savebiyuyos

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update

@Database(entities = [Ingreso::class, Gasto::class, Deuda::class, Ahorro::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingresoDao(): IngresoDao
    abstract fun gastoDao(): GastoDao
    abstract fun deudaDao(): DeudaDao
    abstract fun ahorroDao(): AhorroDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

@Dao
interface IngresoDao {
    @Insert
    suspend fun insertarIngreso(ingreso: Ingreso)

    @Query("SELECT SUM(monto) FROM ingresos WHERE tipo = :tipoIngreso")
    suspend fun obtenerTotalIngresos(tipoIngreso: String): Double

    @Query("SELECT * FROM ingresos")
    suspend fun getAllIngresos(): List<Ingreso>

    @Query("SELECT * FROM ingresos WHERE tipo = :tipo")
    suspend fun obtenerIngresosPorTipo(tipo: String): List<Ingreso>
}

@Dao
interface GastoDao {
    @Insert
    suspend fun insertarGasto(gasto: Gasto)

    @Update
    suspend fun updateGasto(gasto: Gasto)

    @Query("SELECT * FROM gastos WHERE tipoGasto = :tipoGasto")
    suspend fun obtenerGastosPorTipo(tipoGasto: String): List<Gasto>

    @Query("SELECT SUM(monto) FROM gastos WHERE tipoGasto = :tipoGasto")
    suspend fun obtenerTotalGastos(tipoGasto: String): Double

    @Delete
    suspend fun eliminarGasto(gasto: Gasto)

}

@Dao
interface DeudaDao {
    @Insert
    suspend fun insertarDeuda(deuda: Deuda)

    @Query("SELECT * FROM deudas WHERE id = :id")
    suspend fun obtenerDeuda(id: Int): Deuda

    @Query("UPDATE deudas SET abonado = abonado + :abono WHERE id = :id")
    suspend fun abonarDeuda(id: Int, abono: Double)

    @Query("SELECT * FROM deudas")
    suspend fun obtenerDeudas(): List<Deuda>

    @Delete
    suspend fun eliminarDeuda(deuda: Deuda)
}

@Dao
interface AhorroDao {

    @Insert
    suspend fun insertarAhorro(ahorro: Ahorro)

    @Update
    suspend fun actualizarAhorro(ahorro: Ahorro)

    @Query("SELECT * FROM ahorros")
    suspend fun obtenerAhorros(): List<Ahorro>

    @Query("SELECT * FROM ahorros WHERE id = :id")
    suspend fun obtenerAhorro(id: Int): Ahorro?

    @Query("UPDATE ahorros SET abonado = abonado + :monto WHERE id = :id")
    fun abonarAhorro(id: Int, monto: Double)

    @Delete
    suspend fun eliminarAhorro(ahorro: Ahorro)
}


/*
@Dao
interface PerfilDao {
    @Insert
    suspend fun insertarPerfil(perfil: Perfil)

    @Query("SELECT * FROM perfil LIMIT 1")
    suspend fun obtenerPerfil(): Perfil
}
*/
