package com.example.leguaalerta.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.ui.components.textoDeError
import com.example.leguaalerta.viewmodel.SesionViewModel
import com.example.leguaalerta.viewmodel.ValidadorFormularios

@Composable
fun PantallaRegistro(sesionViewModel: SesionViewModel, onVolver: () -> Unit) {
    val mensajeError by sesionViewModel.mensajeError.collectAsState()

    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmacion by rememberSaveable { mutableStateOf("") }
    var esMunicipal by rememberSaveable { mutableStateOf(false) }
    var codigoMunicipal by rememberSaveable { mutableStateOf("") }
    var intentoRegistrar by rememberSaveable { mutableStateOf(false) }
    var mostrarDialogoExito by rememberSaveable { mutableStateOf(false) }

    val errorNombre = ValidadorFormularios.validarNombre(nombre)
    val errorCorreo = ValidadorFormularios.validarCorreo(correo)
    val errorContrasena = ValidadorFormularios.validarContrasenaNueva(contrasena)
    val errorConfirmacion = ValidadorFormularios.validarConfirmacion(contrasena, confirmacion)
    val errorCodigo = if (esMunicipal) ValidadorFormularios.validarCodigoMunicipal(codigoMunicipal) else null
    val formularioValido = listOf(errorNombre, errorCorreo, errorContrasena, errorConfirmacion, errorCodigo)
        .all { it == null }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { espacioInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterno)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            TextButton(onClick = onVolver) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                Text(text = "Volver")
            }
            Text(text = "Crear cuenta", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Completa tus datos para empezar a reportar.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text(text = "Nombre") },
                        isError = intentoRegistrar && errorNombre != null,
                        supportingText = textoDeError(if (intentoRegistrar) errorNombre else null),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            sesionViewModel.limpiarError()
                        },
                        label = { Text(text = "Correo") },
                        isError = intentoRegistrar && errorCorreo != null,
                        supportingText = textoDeError(if (intentoRegistrar) errorCorreo else null),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = { Text(text = "Contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = intentoRegistrar && errorContrasena != null,
                        supportingText = textoDeError(if (intentoRegistrar) errorContrasena else null),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmacion,
                        onValueChange = { confirmacion = it },
                        label = { Text(text = "Repite la contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        isError = intentoRegistrar && errorConfirmacion != null,
                        supportingText = textoDeError(if (intentoRegistrar) errorConfirmacion else null),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Soy funcionario municipal",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(checked = esMunicipal, onCheckedChange = { esMunicipal = it })
                    }
                    AnimatedVisibility(visible = esMunicipal) {
                        OutlinedTextField(
                            value = codigoMunicipal,
                            onValueChange = { codigoMunicipal = it },
                            label = { Text(text = "Código municipal") },
                            isError = intentoRegistrar && errorCodigo != null,
                            supportingText = textoDeError(if (intentoRegistrar) errorCodigo else null),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    AnimatedVisibility(visible = mensajeError != null) {
                        Text(text = mensajeError.orEmpty(), color = MaterialTheme.colorScheme.error)
                    }
                    Button(
                        onClick = {
                            intentoRegistrar = true
                            if (formularioValido) {
                                sesionViewModel.registrar(nombre, correo, contrasena, esMunicipal) { registrado ->
                                    mostrarDialogoExito = registrado
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Crear cuenta")
                    }
                }
            }
        }
    }

    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = onVolver,
            icon = { Icon(imageVector = Icons.Outlined.CheckCircle, contentDescription = null) },
            title = { Text(text = "¡Cuenta creada!") },
            text = { Text(text = "Ya puedes ingresar con tu correo y contraseña.") },
            confirmButton = { TextButton(onClick = onVolver) { Text(text = "Ir a iniciar sesión") } }
        )
    }
}
