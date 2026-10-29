package com.example.leguaalerta.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE recordarSesion = 1 LIMIT 1")
    suspend fun buscarSesionRecordada(): Usuario?

    @Query("UPDATE usuarios SET recordarSesion = 0")
    suspend fun olvidarSesiones()

    @Query("UPDATE usuarios SET recordarSesion = 1 WHERE id = :usuarioId")
    suspend fun recordarSesion(usuarioId: Int)

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int
}
