package com.example.leguaalerta.model

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("reportes")
    suspend fun obtenerReportes(): List<Reporte>

    @GET("reportes/vecino/{vecinoId}")
    suspend fun obtenerReportesDeVecino(@Path("vecinoId") vecinoId: Int): List<Reporte>

    @POST("reportes")
    suspend fun crearReporte(@Body reporte: Reporte): Reporte

    @PUT("reportes/{id}")
    suspend fun actualizarReporte(@Path("id") id: Int, @Body reporte: Reporte): Reporte

    @GET("anuncios")
    suspend fun obtenerAnuncios(): List<Anuncio>

    @POST("anuncios")
    suspend fun crearAnuncio(@Body anuncio: Anuncio): Anuncio

    @DELETE("anuncios/{id}")
    suspend fun eliminarAnuncio(@Path("id") id: Int)
}
