package com.example.leguaalerta.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.ui.theme.EstadoCerradoFondo
import com.example.leguaalerta.ui.theme.EstadoCerradoTexto
import com.example.leguaalerta.ui.theme.EstadoDerivadoFondo
import com.example.leguaalerta.ui.theme.EstadoDerivadoTexto
import com.example.leguaalerta.ui.theme.EstadoEnTerrenoFondo
import com.example.leguaalerta.ui.theme.EstadoEnTerrenoTexto
import com.example.leguaalerta.ui.theme.EstadoRecibidoFondo
import com.example.leguaalerta.ui.theme.EstadoRecibidoTexto

@Composable
fun EtiquetaEstado(estado: EstadoReporte) {
    val (colorFondo, colorTexto) = when (estado) {
        EstadoReporte.RECIBIDO -> EstadoRecibidoFondo to EstadoRecibidoTexto
        EstadoReporte.EN_TERRENO -> EstadoEnTerrenoFondo to EstadoEnTerrenoTexto
        EstadoReporte.DERIVADO -> EstadoDerivadoFondo to EstadoDerivadoTexto
        EstadoReporte.CERRADO -> EstadoCerradoFondo to EstadoCerradoTexto
    }
    Surface(color = colorFondo, shape = RoundedCornerShape(50)) {
        Text(
            text = estado.etiqueta,
            color = colorTexto,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
