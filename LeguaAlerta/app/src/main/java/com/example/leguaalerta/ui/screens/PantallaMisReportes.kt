package com.example.leguaalerta.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.ui.components.BarraInferior
import com.example.leguaalerta.ui.components.DialogoCerrarSesion
import com.example.leguaalerta.ui.components.DialogoDetalleReporte
import com.example.leguaalerta.ui.components.EncabezadoApp
import com.example.leguaalerta.ui.components.TarjetaReporte
import com.example.leguaalerta.ui.components.opcionesVecino
import com.example.leguaalerta.ui.navigation.Rutas
import com.example.leguaalerta.viewmodel.ReportesViewModel

@Composable
fun PantallaMisReportes(
    usuario: Usuario,
    reportesViewModel: ReportesViewModel,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val reportes by remember(usuario.id) { reportesViewModel.reportesDeVecino(usuario.id) }
        .collectAsState(initial = emptyList())

    var estadoFiltrado by remember { mutableStateOf<EstadoReporte?>(null) }
    var reporteSeleccionado by remember { mutableStateOf<Reporte?>(null) }
    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    val reportesVisibles = reportes.filter { estadoFiltrado == null || it.estado == estadoFiltrado }

    Scaffold(
        topBar = { EncabezadoApp(usuario = usuario, onCerrarSesion = { mostrarDialogoSalir = true }) },
        bottomBar = { BarraInferior(opcionesVecino, Rutas.MIS_REPORTES, onNavegar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { espacioInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterno),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column {
                    Text(text = "Mis reportes", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Toca un reporte para ver su detalle y la respuesta municipal.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = estadoFiltrado == null,
                            onClick = { estadoFiltrado = null },
                            label = { Text(text = "Todos (${reportes.size})") }
                        )
                    }
                    items(EstadoReporte.entries) { estado ->
                        FilterChip(
                            selected = estadoFiltrado == estado,
                            onClick = { estadoFiltrado = estado },
                            label = { Text(text = "${estado.etiqueta} (${reportes.count { it.estado == estado }})") }
                        )
                    }
                }
            }
            if (reportesVisibles.isEmpty()) {
                item {
                    Text(
                        text = "No hay reportes en este estado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(reportesVisibles, key = { reporte -> reporte.id }) { reporte ->
                    TarjetaReporte(reporte = reporte, onClick = { reporteSeleccionado = reporte })
                }
            }
        }
    }

    reporteSeleccionado?.let { reporte ->
        DialogoDetalleReporte(reporte = reporte, onCerrar = { reporteSeleccionado = null })
    }
    if (mostrarDialogoSalir) {
        DialogoCerrarSesion(
            onConfirmar = {
                mostrarDialogoSalir = false
                onCerrarSesion()
            },
            onCancelar = { mostrarDialogoSalir = false }
        )
    }
}
