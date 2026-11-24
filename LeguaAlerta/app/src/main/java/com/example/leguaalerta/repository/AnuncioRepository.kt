package com.example.leguaalerta.repository

import com.example.leguaalerta.model.Anuncio
import com.example.leguaalerta.model.AnuncioDao
import com.example.leguaalerta.model.ApiService
import kotlinx.coroutines.flow.Flow

class AnuncioRepository(
    private val anuncioDao: AnuncioDao,
    private val api: ApiService
) {

    fun observarAnuncios(): Flow<List<Anuncio>> = anuncioDao.observarTodos()

    suspend fun sincronizar(): Boolean = try {
        val remotos = api.obtenerAnuncios()
        anuncioDao.borrarTodos()
        anuncioDao.insertarTodos(remotos)
        true
    } catch (e: Exception) {
        false
    }

    suspend fun publicar(titulo: String, mensaje: String): Boolean {
        val nuevo = Anuncio(titulo = titulo, mensaje = mensaje)
        return try {
            anuncioDao.insertar(api.crearAnuncio(nuevo))
            true
        } catch (e: Exception) {
            anuncioDao.insertar(nuevo)
            false
        }
    }

    suspend fun eliminar(anuncio: Anuncio): Boolean {
        anuncioDao.eliminar(anuncio)
        return try {
            api.eliminarAnuncio(anuncio.id)
            true
        } catch (e: Exception) {
            false
        }
    }
}
