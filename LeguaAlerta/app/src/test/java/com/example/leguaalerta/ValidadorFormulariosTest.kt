package com.example.leguaalerta

import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.repository.UsuarioRepository
import com.example.leguaalerta.viewmodel.ValidadorFormularios
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadorFormulariosTest {

    @Test
    fun correoValido_noDevuelveError() {
        assertNull(ValidadorFormularios.validarCorreo("vecina@leguaalerta.cl"))
    }

    @Test
    fun correoInvalido_devuelveError() {
        assertNotNull(ValidadorFormularios.validarCorreo("correo-sin-arroba"))
    }

    @Test
    fun contrasenaNuevaSinNumero_devuelveError() {
        assertNotNull(ValidadorFormularios.validarContrasenaNueva("abcdefg"))
        assertNull(ValidadorFormularios.validarContrasenaNueva("abcdef1"))
    }

    @Test
    fun cerrarCasoSinObservacion_devuelveError() {
        assertNotNull(ValidadorFormularios.validarObservacion(EstadoReporte.CERRADO, ""))
        assertNull(ValidadorFormularios.validarObservacion(EstadoReporte.EN_TERRENO, ""))
    }

    @Test
    fun codigoMunicipalIncorrecto_devuelveError() {
        assertNotNull(ValidadorFormularios.validarCodigoMunicipal("XXXX"))
        assertNull(ValidadorFormularios.validarCodigoMunicipal("SJ2026"))
    }

    @Test
    fun cifrar_esDeterministaYNoDevuelveTextoPlano() {
        val a = UsuarioRepository.cifrar("Vecina2026")
        val b = UsuarioRepository.cifrar("Vecina2026")
        assertEquals(a, b)
        assertEquals(64, a.length)
    }
}
