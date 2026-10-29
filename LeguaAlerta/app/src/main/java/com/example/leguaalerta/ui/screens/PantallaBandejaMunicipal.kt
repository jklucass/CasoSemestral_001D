package com.example.leguaalerta.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.leguaalerta.model.codigoVisible
import com.example.leguaalerta.ui.components.BarraInferior
import com.example.leguaalerta.ui.components.DialogoCerrarSesion
import com.example.leguaalerta.ui.components.EncabezadoApp
import com.example.leguaalerta.ui.components.TarjetaReporte
import com.example.leguaalerta.ui.components.opcionesMunicipal
import com.example.leguaalerta.ui.components.textoDeError
import com.example.leguaalerta.ui.navigation.Rutas
import com.example.leguaalerta.viewmodel.ReportesViewModel
import com.example.leguaalerta.viewmodel.ValidadorFormularios

@Composable
fun PantallaBandejaMunicipal(
    usuario: Usuario,
    reportesViewModel: ReportesViewModel,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val reportes by reportesViewModel.todosLosReportes.collectAsState()

    var estadoFiltrado by remember { mutableStateOf(EstadoReporte.RECIBIDO) }
    var reporteAGestionar by remember { mutableStateOf<Reporte?>(null) }
    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    val reportesVisibles = reportes.filter { it.estado == estadoFiltrado }

    Scaffold(
        topBar = { EncabezadoApp(usuario = usuario, onCerrarSesion = { mostrarDialogoSalir = true }) },
        bottomBar = { BarraInferior(opcionesMunicipal, Rutas.BANDEJA, onNavegar) },
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
                    Text(text = "Bandeja municipal", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Casos recibidos del barrio. Toca uno para gestionarlo.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                        text = "No hay casos en estado \"${estadoFiltrado.etiqueta}\".",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(reportesVisibles, key = { reporte -> reporte.id }) { reporte ->
                    TarjetaReporte(reporte = reporte, onClick = { reporteAGestionar = reporte })
                }
            }
        }
    }

    reporteAGestionar?.let { reporte ->
        DialogoGestionarCaso(
            reporte = reporte,
            onGuardar = { nuevoEstado, observacion ->
                reportesViewModel.cambiarEstado(reporte, nuevoEstado, observacion)
                reporteAGestionar = null
            },
            onCancelar = { reporteAGestionar = null }
        )
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

@Composable
private fun DialogoGestionarCaso(
    reporte: Reporte,
    onGuardar: (EstadoReporte, String) -> Unit,
    onCancelar: () -> Unit
) {
    var nuevoEstado by remember { mutableStateOf(reporte.estado) }
    var observacion by remember { mutableStateOf(reporte.observacionMunicipal) }
    var intentoGuardar by remember { mutableStateOf(false) }
    val errorObservacion = ValidadorFormularios.validarObservacion(nuevoEstado, observacion)

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text(text = "Gestionar ${reporte.codigoVisible()}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "${reporte.tipo.etiqueta}: ${reporte.descripcion}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Nuevo estado", style = MaterialTheme.typography.titleMedium)
                EstadoReporte.entries.chunked(2).forEach { filaDeEstados ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        filaDeEstados.forEach { estado ->
                            FilterChip(
                                selected = nuevoEstado == estado,
                                onClick = { nuevoEstado = estado },
                                label = { Text(text = estado.etiqueta) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = observacion,
                    onValueChange = { observacion = it },
                    label = { Text(text = "Observación de seguimiento") },
                    isError = intentoGuardar && errorObservacion != null,
                    supportingText = textoDeError(if (intentoGuardar) errorObservacion else null),
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    intentoGuardar = true
                    if (errorObservacion == null) onGuardar(nuevoEstado, observacion)
                }
            ) { Text(text = "Guardar") }
        },
        dismissButton = { TextButton(onClick = onCancelar) { Text(text = "Cancelar") } }
    )
}
