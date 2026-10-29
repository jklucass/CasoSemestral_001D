package com.example.leguaalerta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.leguaalerta.model.Rol
import com.example.leguaalerta.model.Usuario
import com.example.leguaalerta.ui.screens.PantallaAnuncios
import com.example.leguaalerta.ui.screens.PantallaBandejaMunicipal
import com.example.leguaalerta.ui.screens.PantallaCarga
import com.example.leguaalerta.ui.screens.PantallaInicioVecino
import com.example.leguaalerta.ui.screens.PantallaLogin
import com.example.leguaalerta.ui.screens.PantallaMisReportes
import com.example.leguaalerta.ui.screens.PantallaNuevoReporte
import com.example.leguaalerta.ui.screens.PantallaRegistro
import com.example.leguaalerta.viewmodel.LeguaAlertaViewModelFactory
import com.example.leguaalerta.viewmodel.ReportesViewModel
import com.example.leguaalerta.viewmodel.SesionViewModel

@Composable
fun AppNavegacion(fabrica: LeguaAlertaViewModelFactory) {
    val navController = rememberNavController()
    val sesionViewModel: SesionViewModel = viewModel(factory = fabrica)
    val reportesViewModel: ReportesViewModel = viewModel(factory = fabrica)

    val usuarioActual by sesionViewModel.usuarioActual.collectAsState()
    val cargando by sesionViewModel.cargando.collectAsState()

    val cerrarSesion = {
        sesionViewModel.cerrarSesion()
        navController.irAlInicioSegunRol(null)
    }

    NavHost(navController = navController, startDestination = Rutas.CARGA) {
        composable(Rutas.CARGA) {
            PantallaCarga(cargando = cargando) {
                navController.irAlInicioSegunRol(sesionViewModel.usuarioActual.value)
            }
        }
        composable(Rutas.LOGIN) {
            PantallaLogin(
                sesionViewModel = sesionViewModel,
                onIrARegistro = {
                    sesionViewModel.limpiarError()
                    navController.navigate(Rutas.REGISTRO)
                },
                onIngresoExitoso = { usuario -> navController.irAlInicioSegunRol(usuario) }
            )
        }
        composable(Rutas.REGISTRO) {
            PantallaRegistro(
                sesionViewModel = sesionViewModel,
                onVolver = {
                    sesionViewModel.limpiarError()
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.VECINO_INICIO) {
            usuarioActual?.let { usuario ->
                PantallaInicioVecino(
                    usuario = usuario,
                    reportesViewModel = reportesViewModel,
                    onNavegar = { ruta -> navController.navegarEntrePestanas(ruta, Rutas.VECINO_INICIO) },
                    onCerrarSesion = cerrarSesion
                )
            }
        }
        composable(Rutas.NUEVO_REPORTE) {
            usuarioActual?.let { usuario ->
                PantallaNuevoReporte(
                    usuario = usuario,
                    reportesViewModel = reportesViewModel,
                    onNavegar = { ruta -> navController.navegarEntrePestanas(ruta, Rutas.VECINO_INICIO) },
                    onVolver = { navController.popBackStack() },
                    onReporteEnviado = { navController.navegarEntrePestanas(Rutas.MIS_REPORTES, Rutas.VECINO_INICIO) }
                )
            }
        }
        composable(Rutas.MIS_REPORTES) {
            usuarioActual?.let { usuario ->
                PantallaMisReportes(
                    usuario = usuario,
                    reportesViewModel = reportesViewModel,
                    onNavegar = { ruta -> navController.navegarEntrePestanas(ruta, Rutas.VECINO_INICIO) },
                    onCerrarSesion = cerrarSesion
                )
            }
        }

        composable(Rutas.BANDEJA) {
            usuarioActual?.let { usuario ->
                PantallaBandejaMunicipal(
                    usuario = usuario,
                    reportesViewModel = reportesViewModel,
                    onNavegar = { ruta -> navController.navegarEntrePestanas(ruta, Rutas.BANDEJA) },
                    onCerrarSesion = cerrarSesion
                )
            }
        }
        composable(Rutas.ANUNCIOS) {
            usuarioActual?.let { usuario ->
                PantallaAnuncios(
                    usuario = usuario,
                    reportesViewModel = reportesViewModel,
                    onNavegar = { ruta -> navController.navegarEntrePestanas(ruta, Rutas.BANDEJA) },
                    onCerrarSesion = cerrarSesion
                )
            }
        }
    }
}

private fun NavHostController.irAlInicioSegunRol(usuario: Usuario?) {
    val destino = when (usuario?.rol) {
        Rol.VECINO -> Rutas.VECINO_INICIO
        Rol.MUNICIPAL -> Rutas.BANDEJA
        null -> Rutas.LOGIN
    }
    navigate(destino) {
        popUpTo(graph.id) { inclusive = true }
    }
}

private fun NavHostController.navegarEntrePestanas(ruta: String, rutaInicial: String) {
    navigate(ruta) {
        popUpTo(rutaInicial)
        launchSingleTop = true
    }
}
