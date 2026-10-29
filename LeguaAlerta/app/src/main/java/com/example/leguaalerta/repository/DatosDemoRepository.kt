package com.example.leguaalerta.repository

import com.example.leguaalerta.model.Anuncio
import com.example.leguaalerta.model.AnuncioDao
import com.example.leguaalerta.model.EstadoReporte
import com.example.leguaalerta.model.Prioridad
import com.example.leguaalerta.model.Reporte
import com.example.leguaalerta.model.ReporteDao
import com.example.leguaalerta.model.Rol
import com.example.leguaalerta.model.TipoIncidencia
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.model.UsuarioDao

class DatosDemoRepository(
    private val usuarioDao: UsuarioDao,
    private val reporteDao: ReporteDao,
    private val anuncioDao: AnuncioDao
) {

    suspend fun cargarSiLaBaseEstaVacia() {
        if (usuarioDao.contarUsuarios() > 0) return

        usuarioDao.insertar(
            Usuario(
                nombre = "Funcionaria Municipal Demo",
                correo = CORREO_MUNICIPAL_DEMO,
                contrasenaCifrada = UsuarioRepository.cifrar(CONTRASENA_MUNICIPAL_DEMO),
                rol = Rol.MUNICIPAL
            )
        )
        val vecinaId = usuarioDao.insertar(
            Usuario(
                nombre = "Vecina Demo",
                correo = CORREO_VECINA_DEMO,
                contrasenaCifrada = UsuarioRepository.cifrar(CONTRASENA_VECINA_DEMO),
                rol = Rol.VECINO
            )
        ).toInt()

        reporteDao.insertar(
            Reporte(
                vecinoId = vecinaId,
                tipo = TipoIncidencia.ESPACIOS_PUBLICOS,
                descripcion = "Juegos infantiles con tornillos sueltos.",
                ubicacionReferencial = "Plaza Demo Central",
                prioridad = Prioridad.MEDIA,
                estado = EstadoReporte.CERRADO,
                observacionMunicipal = "Se reemplazaron los tornillos de los juegos."
            )
        )
        reporteDao.insertar(
            Reporte(
                vecinoId = vecinaId,
                tipo = TipoIncidencia.SEGURIDAD,
                descripcion = "Reuniones nocturnas en pasaje, vecinos inseguros.",
                ubicacionReferencial = "Pasaje referencial 4, sector centro",
                prioridad = Prioridad.ALTA,
                estado = EstadoReporte.DERIVADO,
                observacionMunicipal = "Derivado a Seguridad Comunal para rondas preventivas."
            )
        )
        reporteDao.insertar(
            Reporte(
                vecinoId = vecinaId,
                tipo = TipoIncidencia.ALUMBRADO,
                descripcion = "Luminaria apagada en la plazuela hace una semana.",
                ubicacionReferencial = "Plazuela Los Aromos (referencia)",
                prioridad = Prioridad.MEDIA,
                estado = EstadoReporte.EN_TERRENO
            )
        )
        anuncioDao.insertar(
            Anuncio(
                titulo = "Operativo de luminarias",
                mensaje = "Este sábado revisaremos el alumbrado público del sector centro."
            )
        )
    }

    companion object {
        const val CORREO_MUNICIPAL_DEMO = "municipal@leguaalerta.cl"
        const val CONTRASENA_MUNICIPAL_DEMO = "Municipal2026"
        const val CORREO_VECINA_DEMO = "vecina@leguaalerta.cl"
        const val CONTRASENA_VECINA_DEMO = "Vecina2026"
    }
}
