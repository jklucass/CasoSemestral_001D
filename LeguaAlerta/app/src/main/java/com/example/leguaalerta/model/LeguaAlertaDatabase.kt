package com.example.leguaalerta.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class, Reporte::class, Anuncio::class],
    version = 1,
    exportSchema = false
)
abstract class LeguaAlertaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun reporteDao(): ReporteDao
    abstract fun anuncioDao(): AnuncioDao

    companion object {
        @Volatile
        private var instancia: LeguaAlertaDatabase? = null

        fun obtener(context: Context): LeguaAlertaDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    LeguaAlertaDatabase::class.java,
                    "legua_alerta.db"
                ).build().also { instancia = it }
            }
        }
    }
}
