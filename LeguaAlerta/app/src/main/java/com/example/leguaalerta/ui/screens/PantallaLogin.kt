package com.example.leguaalerta.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.leguaalerta.R
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.ui.components.textoDeError
import com.example.leguaalerta.viewmodel.SesionViewModel
import com.example.leguaalerta.viewmodel.ValidadorFormularios

@Composable
fun PantallaLogin(
    sesionViewModel: SesionViewModel,
    onIrARegistro: () -> Unit,
    onIngresoExitoso: (Usuario) -> Unit
) {
    val usuarioActual by sesionViewModel.usuarioActual.collectAsState()
    val mensajeError by sesionViewModel.mensajeError.collectAsState()

    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var recordarSesion by rememberSaveable { mutableStateOf(true) }
    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }
    var intentoIngresar by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(usuarioActual) {
        usuarioActual?.let(onIngresoExitoso)
    }

    val errorCorreo = if (intentoIngresar) ValidadorFormularios.validarCorreo(correo) else null
    val errorContrasena = if (intentoIngresar) ValidadorFormularios.validarContrasenaIngreso(contrasena) else null

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { espacioInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacioInterno)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(R.drawable.logo_legua_alerta),
                contentDescription = "Logo de LeguaAlerta",
                modifier = Modifier.size(96.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Bienvenido a LeguaAlerta", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Reporta y sigue lo que pasa en tu barrio.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = correo,
                        onValueChange = {
                            correo = it
                            sesionViewModel.limpiarError()
                        },
                        label = { Text(text = "Correo") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Email, contentDescription = null) },
                        isError = errorCorreo != null,
                        supportingText = textoDeError(errorCorreo),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            sesionViewModel.limpiarError()
                        },
                        label = { Text(text = "Contraseña") },
                        leadingIcon = { Icon(imageVector = Icons.Outlined.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                Icon(
                                    imageVector = if (mostrarContrasena) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = if (mostrarContrasena) "Ocultar contraseña" else "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (mostrarContrasena) VisualTransformation.None else PasswordVisualTransformation(),
                        isError = errorContrasena != null,
                        supportingText = textoDeError(errorContrasena),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = recordarSesion, onCheckedChange = { recordarSesion = it })
                        Text(text = "Recordarme en este teléfono", style = MaterialTheme.typography.bodyMedium)
                    }

                    AnimatedVisibility(visible = mensajeError != null) {
                        Text(
                            text = mensajeError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Button(
                        onClick = {
                            intentoIngresar = true
                            val formularioValido = ValidadorFormularios.validarCorreo(correo) == null &&
                                ValidadorFormularios.validarContrasenaIngreso(contrasena) == null
                            if (formularioValido) {
                                sesionViewModel.iniciarSesion(correo, contrasena, recordarSesion)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        Text(text = "Ingresar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            TextButton(onClick = onIrARegistro) {
                Text(text = "¿No tienes cuenta? Regístrate")
            }
        }
    }
}
