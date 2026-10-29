package com.example.leguaalerta.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.ui.components.BarraInferior
import com.example.leguaalerta.ui.components.DialogoCerrarSesion
import com.example.leguaalerta.ui.components.DialogoDetalleReporte
import com.example.leguaalerta.ui.components.EncabezadoApp
import com.example.leguaalerta.ui.components.TarjetaAnuncio
import com.example.leguaalerta.ui.components.TarjetaContador
import com.example.leguaalerta.ui.components.TarjetaReporte
import com.example.leguaalerta.ui.components.opcionesVecino
import com.example.leguaalerta.ui.navigation.Rutas
import com.example.leguaalerta.ui.theme.EstadoCerradoTexto
import com.example.leguaalerta.viewmodel.ReportesViewModel

@Composable
fun PantallaInicioVecino(
    usuario: Usuario,
    reportesViewModel: ReportesViewModel,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val reportes by remember(usuario.id) { reportesViewModel.reportesDeVecino(usuario.id) }
        .collectAsState(initial = emptyList())
    val anuncios by reportesViewModel.anuncios.collectAsState()

    var mostrarDialogoSalir by remember { mutableStateOf(false) }
    var reporteSeleccionado by remember { mutableStateOf<Reporte?>(null) }

    val reportesActivos = reportes.count { it.estado != EstadoReporte.CERRADO }
    val reportesResueltos = reportes.count { it.estado == EstadoReporte.CERRADO }

    Scaffold(
        topBar = { EncabezadoApp(usuario = usuario, onCerrarSesion = { mostrarDialogoSalir = true }) },
        bottomBar = { BarraInferior(opcionesVecino, Rutas.VECINO_INICIO, onNavegar) },
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
                    Text(
                        text = "Hola, ${usuario.nombre.substringBefore(' ')}",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "¿Qué está pasando en tu barrio?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                TarjetaReportarIncidencia(onClick = { onNavegar(Rutas.NUEVO_REPORTE) })
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaContador(
                        valor = reportesActivos,
                        descripcion = "Reportes activos",
                        colorValor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    TarjetaContador(
                        valor = reportesResueltos,
                        descripcion = "Casos resueltos",
                        colorValor = EstadoCerradoTexto,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            if (anuncios.isNotEmpty()) {
                item { Text(text = "Noticias del barrio", style = MaterialTheme.typography.titleMedium) }
                items(anuncios.take(2), key = { anuncio -> anuncio.id }) { anuncio ->
                    TarjetaAnuncio(anuncio = anuncio)
                }
            }
            item { Text(text = "Últimos reportes", style = MaterialTheme.typography.titleMedium) }
            if (reportes.isEmpty()) {
                item {
                    Text(
                        text = "Aún no has enviado reportes. ¡Tu barrio te lo agradecerá!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(reportes.take(3), key = { reporte -> reporte.id }) { reporte ->
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

@Composable
private fun TarjetaReportarIncidencia(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.18f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.NotificationsActive,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Reportar incidencia",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "Seguridad, alumbrado, espacios públicos…",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
