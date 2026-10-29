package com.example.leguaalerta.repository

import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.model.UsuarioDao
import java.security.MessageDigest

class UsuarioRepository(private val usuarioDao: UsuarioDao) {


    suspend fun registrar(usuario: Usuario): Boolean {
        if (usuarioDao.buscarPorCorreo(usuario.correo) != null) return false
        usuarioDao.insertar(usuario)
        return true
    }


    suspend fun iniciarSesion(correo: String, contrasena: String, recordar: Boolean): Usuario? {
        val usuario = usuarioDao.buscarPorCorreo(correo) ?: return null
        if (usuario.contrasenaCifrada != cifrar(contrasena)) return null
        usuarioDao.olvidarSesiones()
        if (recordar) usuarioDao.recordarSesion(usuario.id)
        return usuario
    }

    suspend fun obtenerSesionRecordada(): Usuario? = usuarioDao.buscarSesionRecordada()

    suspend fun cerrarSesion() = usuarioDao.olvidarSesiones()

    companion object {
        fun cifrar(contrasena: String): String =
            MessageDigest.getInstance("SHA-256")
                .digest(contrasena.toByteArray())
                .joinToString("") { byte -> "%02x".format(byte) }
    }
}
