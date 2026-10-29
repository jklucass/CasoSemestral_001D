package com.example.leguaalerta.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leguaalerta.model.Anuncio
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Prioridad
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.TipoIncidencia
import com.example.leguaalerta.repository.AnuncioRepository
import com.example.leguaalerta.repository.FotoRepository
import com.example.leguaalerta.repository.ReporteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportesViewModel(
    private val reporteRepository: ReporteRepository,
    private val anuncioRepository: AnuncioRepository,
    private val fotoRepository: FotoRepository
) : ViewModel() {

    val todosLosReportes: StateFlow<List<Reporte>> = reporteRepository.observarTodosLosReportes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIEMPO_ESPERA_MS), emptyList())

    val anuncios: StateFlow<List<Anuncio>> = anuncioRepository.observarAnuncios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIEMPO_ESPERA_MS), emptyList())

    fun reportesDeVecino(vecinoId: Int): Flow<List<Reporte>> =
        reporteRepository.observarReportesDeVecino(vecinoId)

    fun enviarReporte(
        vecinoId: Int,
        tipo: TipoIncidencia,
        descripcion: String,
        ubicacion: String,
        prioridad: Prioridad,
        foto: Bitmap?,
        alTerminar: () -> Unit
    ) {
        viewModelScope.launch {
            
            val rutaFoto = foto?.let { fotoTomada ->
                withContext(Dispatchers.IO) { fotoRepository.guardar(fotoTomada) }
            }
            reporteRepository.crear(
                Reporte(
                    vecinoId = vecinoId,
                    tipo = tipo,
                    descripcion = descripcion.trim(),
                    ubicacionReferencial = ubicacion.trim(),
                    prioridad = prioridad,
                    rutaFoto = rutaFoto
                )
            )
            alTerminar()
        }
    }

    fun cambiarEstado(reporte: Reporte, nuevoEstado: EstadoReporte, observacion: String) {
        viewModelScope.launch {
            reporteRepository.actualizarEstado(reporte, nuevoEstado, observacion.trim())
        }
    }

    fun publicarAnuncio(titulo: String, mensaje: String) {
        viewModelScope.launch {
            anuncioRepository.publicar(titulo.trim(), mensaje.trim())
        }
    }

    fun eliminarAnuncio(anuncio: Anuncio) {
        viewModelScope.launch {
            anuncioRepository.eliminar(anuncio)
        }
    }

    private companion object {
        const val TIEMPO_ESPERA_MS = 5_000L
    }
}
