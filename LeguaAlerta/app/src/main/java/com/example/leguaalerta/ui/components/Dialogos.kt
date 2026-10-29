package com.example.leguaalerta.ui.components

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.codigoVisible

@Composable
fun DialogoCerrarSesion(onConfirmar: () -> Unit, onCancelar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCancelar,
        icon = { Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = null) },
        title = { Text(text = "¿Cerrar sesión?") },
        text = { Text(text = "La próxima vez tendrás que ingresar tu correo y contraseña.") },
        confirmButton = { TextButton(onClick = onConfirmar) { Text(text = "Cerrar sesión") } },
        dismissButton = { TextButton(onClick = onCancelar) { Text(text = "Cancelar") } }
    )
}

@Composable
fun DialogoDetalleReporte(reporte: Reporte, onCerrar: () -> Unit) {
    AlertDialog(
        onDismissRequest = onCerrar,
        icon = { Icon(imageVector = reporte.tipo.icono(), contentDescription = null) },
        title = { Text(text = "${reporte.tipo.etiqueta} · ${reporte.codigoVisible()}") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                EtiquetaEstado(estado = reporte.estado)
                Text(text = reporte.descripcion, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Ubicación: ${reporte.ubicacionReferencial}")
                Text(text = "Prioridad: ${reporte.prioridad.etiqueta}")
                Text(text = "Enviado el ${formatearFecha(reporte.fechaCreacion)}")

                reporte.rutaFoto?.let { ruta ->
                    val foto = remember(ruta) { BitmapFactory.decodeFile(ruta) }
                    if (foto != null) {
                        Image(
                            bitmap = foto.asImageBitmap(),
                            contentDescription = "Foto adjunta al reporte",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }
                }

                if (reporte.observacionMunicipal.isNotBlank()) {
                    Text(
                        text = "Respuesta municipal: ${reporte.observacionMunicipal}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        confirmButton = { TextButton(onClick = onCerrar) { Text(text = "Cerrar") } }
    )
}
