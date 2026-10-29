package com.example.leguaalerta.ui.screens

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.model.Prioridad
import com.example.leguaalerta.model.TipoIncidencia
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.ui.components.BarraInferior
import com.example.leguaalerta.ui.components.icono
import com.example.leguaalerta.ui.components.opcionesVecino
import com.example.leguaalerta.ui.components.textoDeError
import com.example.leguaalerta.ui.navigation.Rutas
import com.example.leguaalerta.viewmodel.ReportesViewModel
import com.example.leguaalerta.viewmodel.ValidadorFormularios

@Composable
fun PantallaNuevoReporte(
    usuario: Usuario,
    reportesViewModel: ReportesViewModel,
    onNavegar: (String) -> Unit,
    onVolver: () -> Unit,
    onReporteEnviado: () -> Unit
) {
    var tipoSeleccionado by rememberSaveable { mutableStateOf<TipoIncidencia?>(null) }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var ubicacion by rememberSaveable { mutableStateOf("") }
    var prioridad by rememberSaveable { mutableStateOf(Prioridad.MEDIA) }
    var foto by remember { mutableStateOf<Bitmap?>(null) }
    var errorCamara by remember { mutableStateOf<String?>(null) }
    var intentoEnviar by rememberSaveable { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrarExito by remember { mutableStateOf(false) }
    var enviando by remember { mutableStateOf(false) }

    val lanzadorCamara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { fotoTomada ->
        if (fotoTomada != null) foto = fotoTomada
    }

    val errorTipo = if (intentoEnviar) ValidadorFormularios.validarTipo(tipoSeleccionado) else null
    val errorDescripcion = if (intentoEnviar) ValidadorFormularios.validarDescripcion(descripcion) else null
    val errorUbicacion = if (intentoEnviar) ValidadorFormularios.validarUbicacion(ubicacion) else null

    Scaffold(
        bottomBar = { BarraInferior(opcionesVecino, Rutas.NUEVO_REPORTE, onNavegar) },
        containerColor = MaterialTheme.colorScheme.background
    ) { espacioInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterno)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onVolver) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                Text(text = "Volver")
            }
            Column {
                Text(text = "Nuevo reporte", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Cuéntanos qué ocurre y dónde (de forma referencial).",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            SeccionPaso(numero = 1, titulo = "Tipo de incidencia") {
                TipoIncidencia.entries.chunked(2).forEach { filaDeTipos ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        filaDeTipos.forEach { tipo ->
                            FilterChip(
                                selected = tipoSeleccionado == tipo,
                                onClick = { tipoSeleccionado = tipo },
                                label = { Text(text = tipo.etiqueta) },
                                leadingIcon = {
                                    Icon(imageVector = tipo.icono(), contentDescription = null, modifier = Modifier.size(18.dp))
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (filaDeTipos.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
                if (errorTipo != null) {
                    Text(text = errorTipo, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }

            SeccionPaso(numero = 2, titulo = "Descripción") {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    placeholder = { Text(text = "¿Qué está pasando?") },
                    isError = errorDescripcion != null,
                    supportingText = textoDeError(errorDescripcion),
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            SeccionPaso(numero = 3, titulo = "Ubicación referencial") {
                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    placeholder = { Text(text = "Ej: Plaza Los Aromos, pasaje 4") },
                    leadingIcon = { Icon(imageVector = Icons.Outlined.Place, contentDescription = null) },
                    isError = errorUbicacion != null,
                    supportingText = textoDeError(errorUbicacion),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                MapaReferencialSimulado()
            }

            SeccionPaso(numero = 4, titulo = "Prioridad") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Prioridad.entries.forEach { opcion ->
                        FilterChip(
                            selected = prioridad == opcion,
                            onClick = { prioridad = opcion },
                            label = { Text(text = opcion.etiqueta) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            SeccionPaso(numero = 5, titulo = "Foto (opcional)") {
                OutlinedButton(
                    onClick = {
                        try {
                            errorCamara = null
                            lanzadorCamara.launch(null)
                        } catch (excepcion: ActivityNotFoundException) {
                            errorCamara = "No encontramos una cámara en este teléfono."
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Outlined.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (foto == null) "Tomar foto" else "Tomar otra foto")
                }
                errorCamara?.let { mensaje ->
                    Text(text = mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
                foto?.let { fotoTomada ->
                    Image(
                        bitmap = fotoTomada.asImageBitmap(),
                        contentDescription = "Foto del reporte",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    TextButton(onClick = { foto = null }) { Text(text = "Quitar foto") }
                }
            }

            Button(
                onClick = {
                    intentoEnviar = true
                    val formularioValido = ValidadorFormularios.validarTipo(tipoSeleccionado) == null &&
                        ValidadorFormularios.validarDescripcion(descripcion) == null &&
                        ValidadorFormularios.validarUbicacion(ubicacion) == null
                    if (formularioValido) mostrarConfirmacion = true
                },
                enabled = !enviando,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text(text = if (enviando) "Enviando…" else "Enviar reporte")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    val tipoAEnviar = tipoSeleccionado
    if (mostrarConfirmacion && tipoAEnviar != null) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text(text = "¿Enviar reporte?") },
            text = {
                Text(text = "${tipoAEnviar.etiqueta} en \"${ubicacion.trim()}\" con prioridad ${prioridad.etiqueta.lowercase()}.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        enviando = true
                        reportesViewModel.enviarReporte(
                            vecinoId = usuario.id,
                            tipo = tipoAEnviar,
                            descripcion = descripcion,
                            ubicacion = ubicacion,
                            prioridad = prioridad,
                            foto = foto
                        ) {
                            enviando = false
                            mostrarExito = true
                        }
                    }
                ) { Text(text = "Enviar") }
            },
            dismissButton = { TextButton(onClick = { mostrarConfirmacion = false }) { Text(text = "Revisar") } }
        )
    }

    if (mostrarExito) {
        AlertDialog(
            onDismissRequest = onReporteEnviado,
            icon = { Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = null) },
            title = { Text(text = "¡Reporte enviado!") },
            text = { Text(text = "La municipalidad revisará tu caso. Puedes seguir su estado en \"Mis reportes\".") },
            confirmButton = { TextButton(onClick = onReporteEnviado) { Text(text = "Ver mis reportes") } }
        )
    }
}

@Composable
private fun SeccionPaso(numero: Int, titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "PASO $numero",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(text = titulo, style = MaterialTheme.typography.titleMedium)
            contenido()
        }
    }
}

@Composable
private fun MapaReferencialSimulado() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Outlined.Map,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = "Mapa referencial (simulado)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
