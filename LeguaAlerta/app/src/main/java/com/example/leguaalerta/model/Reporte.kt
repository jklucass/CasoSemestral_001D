package com.example.leguaalerta.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Locale

@Entity(tableName = "reportes")
data class Reporte(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vecinoId: Int,
    val tipo: TipoIncidencia,
    val descripcion: String,
    val ubicacionReferencial: String,
    val prioridad: Prioridad,
    val estado: EstadoReporte = EstadoReporte.RECIBIDO,
    val rutaFoto: String? = null,
    val observacionMunicipal: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)

fun Reporte.codigoVisible(): String {
    val anio = Calendar.getInstance().apply { timeInMillis = fechaCreacion }.get(Calendar.YEAR)
    return String.format(Locale.ROOT, "SJ-%d-%04d", anio, id)
}
