package com.example.leguaalerta.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.ReportProblem
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.leguaalerta.model.TipoIncidencia

fun TipoIncidencia.icono(): ImageVector = when (this) {
    TipoIncidencia.ALUMBRADO -> Icons.Outlined.Lightbulb
    TipoIncidencia.SEGURIDAD -> Icons.Outlined.Shield
    TipoIncidencia.ESPACIOS_PUBLICOS -> Icons.Outlined.Park
    TipoIncidencia.INFRAESTRUCTURA -> Icons.Outlined.Build
    TipoIncidencia.OTRO -> Icons.Outlined.ReportProblem
}
