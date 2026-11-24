package com.example.leguaalerta.repository

import com.example.leguaalerta.model.ApiService
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.ReporteDao
import kotlinx.coroutines.flow.Flow

class ReporteRepository(
    private val reporteDao: ReporteDao,
    private val api: ApiService
) {

    fun observarReportesDeVecino(vecinoId: Int): Flow<List<Reporte>> =
        reporteDao.observarPorVecino(vecinoId)

    fun observarTodosLosReportes(): Flow<List<Reporte>> = reporteDao.observarTodos()

    suspend fun sincronizar(): Boolean = try {
        reporteDao.insertarTodos(api.obtenerReportes())
        true
    } catch (e: Exception) {
        false
    }

    suspend fun sincronizarDeVecino(vecinoId: Int): Boolean = try {
        reporteDao.insertarTodos(api.obtenerReportesDeVecino(vecinoId))
        true
    } catch (e: Exception) {
        false
    }

    suspend fun crear(reporte: Reporte): Boolean = try {
        reporteDao.insertar(api.crearReporte(reporte))
        true
    } catch (e: Exception) {
        reporteDao.insertar(reporte)
        false
    }

    suspend fun actualizarEstado(
        reporte: Reporte,
        nuevoEstado: EstadoReporte,
        observacion: String
    ): Boolean {
        val actualizado = reporte.copy(estado = nuevoEstado, observacionMunicipal = observacion)
        return try {
            reporteDao.actualizar(api.actualizarReporte(reporte.id, actualizado))
            true
        } catch (e: Exception) {
            reporteDao.actualizar(actualizado)
            false
        }
    }
}
