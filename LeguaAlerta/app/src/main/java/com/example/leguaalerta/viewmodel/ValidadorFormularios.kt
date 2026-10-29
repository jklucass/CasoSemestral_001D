package com.example.leguaalerta.viewmodel

import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.TipoIncidencia

object ValidadorFormularios {

    const val CODIGO_MUNICIPAL = "SJ2026"

    private val patronCorreo = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")

    fun validarNombre(nombre: String): String? = when {
        nombre.isBlank() -> "Ingresa tu nombre."
        nombre.trim().length < 3 -> "El nombre debe tener al menos 3 letras."
        else -> null
    }

    fun validarCorreo(correo: String): String? = when {
        correo.isBlank() -> "Ingresa tu correo."
        !patronCorreo.matches(correo.trim()) -> "Ingresa un correo válido (ej: nombre@correo.cl)."
        else -> null
    }

    fun validarContrasenaIngreso(contrasena: String): String? =
        if (contrasena.isBlank()) "Ingresa tu contraseña." else null

    fun validarContrasenaNueva(contrasena: String): String? = when {
        contrasena.length < 6 -> "Debe tener al menos 6 caracteres."
        contrasena.none { it.isDigit() } -> "Debe incluir al menos un número."
        else -> null
    }

    fun validarConfirmacion(contrasena: String, confirmacion: String): String? =
        if (contrasena != confirmacion) "Las contraseñas no coinciden." else null

    fun validarCodigoMunicipal(codigo: String): String? =
        if (codigo.trim() != CODIGO_MUNICIPAL) "Código municipal incorrecto." else null

    fun validarTipo(tipo: TipoIncidencia?): String? =
        if (tipo == null) "Elige un tipo de incidencia." else null

    fun validarDescripcion(descripcion: String): String? = when {
        descripcion.isBlank() -> "Describe lo que está pasando."
        descripcion.trim().length < 10 -> "Cuéntanos un poco más (mínimo 10 caracteres)."
        else -> null
    }

    fun validarUbicacion(ubicacion: String): String? =
        if (ubicacion.isBlank()) "Indica una referencia (ej: plaza, pasaje o esquina)." else null

    fun validarTituloAnuncio(titulo: String): String? = when {
        titulo.isBlank() -> "Escribe un título."
        titulo.trim().length < 5 -> "El título debe tener al menos 5 caracteres."
        else -> null
    }

    fun validarMensajeAnuncio(mensaje: String): String? = when {
        mensaje.isBlank() -> "Escribe el mensaje del anuncio."
        mensaje.trim().length < 10 -> "El mensaje debe tener al menos 10 caracteres."
        else -> null
    }

    fun validarObservacion(nuevoEstado: EstadoReporte, observacion: String): String? =
        if (nuevoEstado == EstadoReporte.CERRADO && observacion.isBlank()) {
            "Agrega una observación para cerrar el caso."
        } else {
            null
        }
}
