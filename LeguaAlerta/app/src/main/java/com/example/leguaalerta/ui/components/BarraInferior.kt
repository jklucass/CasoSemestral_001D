package com.example.leguaalerta.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.leguaalerta.ui.navigation.Rutas

data class OpcionNavegacion(val ruta: String, val etiqueta: String, val icono: ImageVector)

val opcionesVecino = listOf(
    OpcionNavegacion(Rutas.VECINO_INICIO, "Inicio", Icons.Outlined.Home),
    OpcionNavegacion(Rutas.NUEVO_REPORTE, "Reportar", Icons.Outlined.AddCircle),
    OpcionNavegacion(Rutas.MIS_REPORTES, "Mis reportes", Icons.Outlined.Description)
)

val opcionesMunicipal = listOf(
    OpcionNavegacion(Rutas.BANDEJA, "Bandeja de casos", Icons.Outlined.Inbox),
    OpcionNavegacion(Rutas.ANUNCIOS, "Anuncios", Icons.Outlined.Campaign)
)

@Composable
fun BarraInferior(opciones: List<OpcionNavegacion>, rutaActual: String, onNavegar: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        opciones.forEach { opcion ->
            NavigationBarItem(
                selected = rutaActual == opcion.ruta,
                onClick = { if (rutaActual != opcion.ruta) onNavegar(opcion.ruta) },
                icon = { Icon(imageVector = opcion.icono, contentDescription = null) },
                label = { Text(text = opcion.etiqueta) }
            )
        }
    }
}
