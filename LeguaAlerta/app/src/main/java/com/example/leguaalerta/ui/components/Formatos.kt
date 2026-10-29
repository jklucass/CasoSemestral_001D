package com.example.leguaalerta.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val formatoFecha = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.forLanguageTag("es-CL"))

fun formatearFecha(milisegundos: Long): String = formatoFecha.format(Date(milisegundos))

fun textoDeError(error: String?): (@Composable () -> Unit)? {
    if (error == null) return null
    return { Text(error) }
}
