package com.example.leguaalerta.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.leguaalerta.model.LeguaAlertaDatabase
import com.example.leguaalerta.repository.AnuncioRepository
import com.example.leguaalerta.repository.DatosDemoRepository
import com.example.leguaalerta.repository.FotoRepository
import com.example.leguaalerta.repository.ReporteRepository
import com.example.leguaalerta.repository.UsuarioRepository
import java.io.File

class LeguaAlertaViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val baseDeDatos = LeguaAlertaDatabase.obtener(context)
    private val carpetaFotos = File(context.filesDir, "fotos_reportes")

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(
            usuarioRepository = UsuarioRepository(baseDeDatos.usuarioDao()),
            datosDemoRepository = DatosDemoRepository(
                baseDeDatos.usuarioDao(),
                baseDeDatos.reporteDao(),
                baseDeDatos.anuncioDao()
            )
        ) as T

        modelClass.isAssignableFrom(ReportesViewModel::class.java) -> ReportesViewModel(
            reporteRepository = ReporteRepository(baseDeDatos.reporteDao()),
            anuncioRepository = AnuncioRepository(baseDeDatos.anuncioDao()),
            fotoRepository = FotoRepository(carpetaFotos)
        ) as T

        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
