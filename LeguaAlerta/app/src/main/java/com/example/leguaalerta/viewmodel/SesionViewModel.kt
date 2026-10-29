package com.example.leguaalerta.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leguaalerta.model.Rol
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.repository.DatosDemoRepository
import com.example.leguaalerta.repository.UsuarioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SesionViewModel(
    private val usuarioRepository: UsuarioRepository,
    private val datosDemoRepository: DatosDemoRepository
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    init {
        viewModelScope.launch {
            datosDemoRepository.cargarSiLaBaseEstaVacia()
            _usuarioActual.value = usuarioRepository.obtenerSesionRecordada()
            delay(DURACION_MINIMA_CARGA_MS)
            _cargando.value = false
        }
    }

    fun iniciarSesion(correo: String, contrasena: String, recordar: Boolean) {
        viewModelScope.launch {
            val usuario = usuarioRepository.iniciarSesion(correo.trim().lowercase(), contrasena, recordar)
            if (usuario == null) {
                _mensajeError.value = "Correo o contraseña incorrectos."
            } else {
                _mensajeError.value = null
                _usuarioActual.value = usuario
            }
        }
    }

    fun registrar(
        nombre: String,
        correo: String,
        contrasena: String,
        esMunicipal: Boolean,
        alTerminar: (registrado: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                nombre = nombre.trim(),
                correo = correo.trim().lowercase(),
                contrasenaCifrada = UsuarioRepository.cifrar(contrasena),
                rol = if (esMunicipal) Rol.MUNICIPAL else Rol.VECINO
            )
            val registrado = usuarioRepository.registrar(nuevoUsuario)
            _mensajeError.value = if (registrado) null else "Ya existe una cuenta con ese correo."
            alTerminar(registrado)
        }
    }

    fun cerrarSesion() {
        _usuarioActual.value = null
        viewModelScope.launch { usuarioRepository.cerrarSesion() }
    }

    fun limpiarError() {
        _mensajeError.value = null
    }

    private companion object {
        const val DURACION_MINIMA_CARGA_MS = 1500L
    }
}
