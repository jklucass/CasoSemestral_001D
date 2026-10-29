package com.example.leguaalerta.repository

import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.ReporteDao
import kotlinx.coroutines.flow.Flow

class ReporteRepository(private val reporteDao: ReporteDao) {

    fun observarReportesDeVecino(vecinoId: Int): Flow<List<Reporte>> =
        reporteDao.observarPorVecino(vecinoId)

    fun observarTodosLosReportes(): Flow<List<Reporte>> = reporteDao.observarTodos()

    suspend fun crear(reporte: Reporte) {
        reporteDao.insertar(reporte)
    }

    suspend fun actualizarEstado(reporte: Reporte, nuevoEstado: EstadoReporte, observacion: String) {
        reporteDao.actualizar(reporte.copy(estado = nuevoEstado, observacionMunicipal = observacion))
    }
}
