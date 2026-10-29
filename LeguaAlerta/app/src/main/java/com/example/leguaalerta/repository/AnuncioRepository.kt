package com.example.leguaalerta.repository

import com.example.leguaalerta.model.Anuncio
import com.example.leguaalerta.model.AnuncioDao
import kotlinx.coroutines.flow.Flow

class AnuncioRepository(private val anuncioDao: AnuncioDao) {

    fun observarAnuncios(): Flow<List<Anuncio>> = anuncioDao.observarTodos()

    suspend fun publicar(titulo: String, mensaje: String) {
        anuncioDao.insertar(Anuncio(titulo = titulo, mensaje = mensaje))
    }

    suspend fun eliminar(anuncio: Anuncio) = anuncioDao.eliminar(anuncio)
}
