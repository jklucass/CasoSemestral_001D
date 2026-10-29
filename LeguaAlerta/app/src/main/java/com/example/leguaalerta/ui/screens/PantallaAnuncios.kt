package com.example.leguaalerta.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.model.Anuncio
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.ui.components.BarraInferior
import com.example.leguaalerta.ui.components.DialogoCerrarSesion
import com.example.leguaalerta.ui.components.EncabezadoApp
import com.example.leguaalerta.ui.components.TarjetaAnuncio
import com.example.leguaalerta.ui.components.opcionesMunicipal
import com.example.leguaalerta.ui.components.textoDeError
import com.example.leguaalerta.ui.navigation.Rutas
import com.example.leguaalerta.viewmodel.ReportesViewModel
import com.example.leguaalerta.viewmodel.ValidadorFormularios

@Composable
fun PantallaAnuncios(
    usuario: Usuario,
    reportesViewModel: ReportesViewModel,
    onNavegar: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val anuncios by reportesViewModel.anuncios.collectAsState()

    var titulo by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf("") }
    var intentoPublicar by rememberSaveable { mutableStateOf(false) }
    var anuncioPublicado by remember { mutableStateOf(false) }
    var anuncioAEliminar by remember { mutableStateOf<Anuncio?>(null) }
    var mostrarDialogoSalir by remember { mutableStateOf(false) }

    val errorTitulo = if (intentoPublicar) ValidadorFormularios.validarTituloAnuncio(titulo) else null
    val errorMensaje = if (intentoPublicar) ValidadorFormularios.validarMensajeAnuncio(mensaje) else null

    Scaffold(
        topBar = { EncabezadoApp(usuario = usuario, onCerrarSesion = { mostrarDialogoSalir = true }) },
        bottomBar = { BarraInferior(opcionesMunicipal, Rutas.ANUNCIOS, onNavegar) },
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
                    Text(text = "Creador de anuncios", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Publica noticias y avisos para los vecinos.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = titulo,
                            onValueChange = {
                                titulo = it
                                anuncioPublicado = false
                            },
                            label = { Text(text = "Título") },
                            isError = errorTitulo != null,
                            supportingText = textoDeError(errorTitulo),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = mensaje,
                            onValueChange = {
                                mensaje = it
                                anuncioPublicado = false
                            },
                            label = { Text(text = "Mensaje para los vecinos") },
                            isError = errorMensaje != null,
                            supportingText = textoDeError(errorMensaje),
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth()
                        )
                        AnimatedVisibility(visible = anuncioPublicado) {
                            Text(text = "¡Anuncio publicado!", color = MaterialTheme.colorScheme.primary)
                        }
                        Button(
                            onClick = {
                                intentoPublicar = true
                                val formularioValido = ValidadorFormularios.validarTituloAnuncio(titulo) == null &&
                                    ValidadorFormularios.validarMensajeAnuncio(mensaje) == null
                                if (formularioValido) {
                                    reportesViewModel.publicarAnuncio(titulo, mensaje)
                                    titulo = ""
                                    mensaje = ""
                                    intentoPublicar = false
                                    anuncioPublicado = true
                                }
                            },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Publicar anuncio")
                        }
                    }
                }
            }
            item { Text(text = "Anuncios publicados", style = MaterialTheme.typography.titleMedium) }
            if (anuncios.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no hay anuncios publicados.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(anuncios, key = { anuncio -> anuncio.id }) { anuncio ->
                    TarjetaAnuncio(anuncio = anuncio, onEliminar = { anuncioAEliminar = anuncio })
                }
            }
        }
    }

    anuncioAEliminar?.let { anuncio ->
        AlertDialog(
            onDismissRequest = { anuncioAEliminar = null },
            title = { Text(text = "¿Eliminar anuncio?") },
            text = { Text(text = "\"${anuncio.titulo}\" dejará de verse en el inicio de los vecinos.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        reportesViewModel.eliminarAnuncio(anuncio)
                        anuncioAEliminar = null
                    }
                ) { Text(text = "Eliminar") }
            },
            dismissButton = { TextButton(onClick = { anuncioAEliminar = null }) { Text(text = "Cancelar") } }
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
