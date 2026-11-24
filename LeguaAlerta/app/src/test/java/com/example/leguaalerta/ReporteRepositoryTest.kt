package com.example.leguaalerta

import com.example.leguaalerta.model.ApiService
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Prioridad
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.ReporteDao
import com.example.leguaalerta.model.TipoIncidencia
import com.example.leguaalerta.repository.ReporteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ReporteRepositoryTest {

    private val dao = mockk<ReporteDao>(relaxed = true)
    private val api = mockk<ApiService>()
    private val repositorio = ReporteRepository(dao, api)

    private fun reporteBase(id: Int = 0) = Reporte(
        id = id,
        vecinoId = 1,
        tipo = TipoIncidencia.ALUMBRADO,
        descripcion = "Luminaria apagada hace una semana",
        ubicacionReferencial = "Plaza central",
        prioridad = Prioridad.MEDIA
    )

    @Test
    fun crear_guardaLaRespuestaDelServidor() = runTest {
        val local = reporteBase()
        val remoto = reporteBase(id = 7)
        coEvery { api.crearReporte(local) } returns remoto

        val ok = repositorio.crear(local)

        assertTrue(ok)
        coVerify { dao.insertar(remoto) }
    }

    @Test
    fun crear_sinConexion_guardaLocalYDevuelveFalse() = runTest {
        val local = reporteBase()
        coEvery { api.crearReporte(local) } throws RuntimeException("sin red")

        val ok = repositorio.crear(local)

        assertFalse(ok)
        coVerify { dao.insertar(local) }
    }

    @Test
    fun sincronizar_guardaLosReportesDescargados() = runTest {
        val lista = listOf(reporteBase(1), reporteBase(2))
        coEvery { api.obtenerReportes() } returns lista

        val ok = repositorio.sincronizar()

        assertTrue(ok)
        coVerify { dao.insertarTodos(lista) }
    }

    @Test
    fun actualizarEstado_enviaElNuevoEstadoAlServidor() = runTest {
        val original = reporteBase(3)
        val esperado = original.copy(estado = EstadoReporte.CERRADO, observacionMunicipal = "Listo")
        coEvery { api.actualizarReporte(3, esperado) } returns esperado

        val ok = repositorio.actualizarEstado(original, EstadoReporte.CERRADO, "Listo")

        assertTrue(ok)
        coVerify { dao.actualizar(esperado) }
    }
}
