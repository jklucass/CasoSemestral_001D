# LeguaAlerta

## Integrantes

- Diego Mora
- Lucas Campos

Aplicación Android para la **gestión y el seguimiento de situaciones de seguridad comunal** en
San Joaquín, desarrollada para el caso semestral de **DSY1105 – Desarrollo de Aplicaciones
Móviles** (Duoc UC, sede San Joaquín). Es un MVP académico: todos los datos son ficticios.

## ¿Qué hace?

**Vista vecino**
- Registro e inicio de sesión, con opción **"Recordarme"**.
- Reportar una incidencia (alumbrado, seguridad, espacios públicos, infraestructura u otro)
  en un formulario por pasos, con ubicación referencial, prioridad y foto opcional.
- Ver el estado de sus reportes (Recibido, En terreno, Derivado, Cerrado) y la respuesta municipal.
- Leer las noticias del barrio que publica la municipalidad.

**Vista municipal (administrador)**
- Bandeja de casos filtrada por estado.
- Cambiar el estado de un caso y dejar una observación de seguimiento.
- Creador de anuncios para los vecinos.

## Cuentas de prueba

| Rol | Correo | Contraseña |
| --- | --- | --- |
| Vecino | vecina@leguaalerta.cl | Vecina2026 |
| Municipal | municipal@leguaalerta.cl | Municipal2026 |

Para registrar una cuenta municipal nueva se pide el código **SJ2026**.

## Requisitos de la evaluación

| Requisito | Dónde se cumple |
| --- | --- |
| Formulario con validación | Login, registro, nuevo reporte, anuncios y gestión de casos (`ValidadorFormularios`) |
| Imagen | Logo vectorial `logo_legua_alerta.xml` y foto del reporte |
| Animación | Logo que "late" en la pantalla de carga y mensajes con `AnimatedVisibility` |
| Modal | Confirmar envío, detalle del reporte, gestionar caso, eliminar anuncio y cerrar sesión |
| Persistencia con Room | Usuarios (con sesión recordada), reportes y anuncios |
| Navegación entre vistas | Navigation Compose con vistas separadas para vecino y municipal |
| Recurso nativo | Cámara del teléfono para adjuntar una foto al reporte |

## Arquitectura (MVVM)

```
app/src/main/java/com/example/leguaalerta/
├── model/        Entidades de Room, DAO y base de datos
├── repository/   Acceso a los datos (usuarios, reportes, anuncios, fotos y datos demo)
├── viewmodel/    Lógica de las pantallas, validaciones y fábrica de ViewModel
└── ui/           MainActivity, pantallas, componentes, navegación y tema
```

Cada capa solo conoce a la de abajo: `ui` → `viewmodel` → `repository` → `model`.

## Tecnologías

Kotlin · Jetpack Compose (Material 3) · Room · Retrofit + Gson · Navigation Compose · ViewModel + StateFlow ·
JUnit 4 + MockK · 
compileSdk/targetSdk 37 · minSdk 24.

## API REST consumida (Retrofit + Xano)

El backend (base de datos y API REST) está creado en **Xano** y la app lo consume con Retrofit.
La URL base del grupo de API está en `model/RetrofitClient.kt`.

| # | Método | Endpoint | Uso en la app |
| --- | --- | --- | --- |
| 1 | GET | `/api/reportes` | Descarga todos los reportes (bandeja municipal) |
| 2 | GET | `/api/reportes/vecino/{vecinoId}` | Descarga los reportes de un vecino |
| 3 | POST | `/api/reportes` | Crea un reporte desde el formulario por pasos |
| 4 | PUT | `/api/reportes/{id}` | Cambia el estado y la observación de un caso |
| 5 | GET | `/api/anuncios` | Descarga los anuncios para los vecinos |
| 6 | POST | `/api/anuncios` | Publica un anuncio (vista municipal) |
| 7 | DELETE | `/api/anuncios/{id}` | Elimina un anuncio |

Los datos que llegan de la API se guardan en Room, así la interfaz siempre lee de la base local
y la app sigue funcionando si el servidor no responde.

Ejemplo de JSON de un reporte:

```json
{
  "id": 1,
  "vecinoId": 2,
  "tipo": "ALUMBRADO",
  "descripcion": "Luminaria apagada hace una semana",
  "ubicacionReferencial": "Plaza central",
  "prioridad": "MEDIA",
  "estado": "RECIBIDO",
  "rutaFoto": null,
  "observacionMunicipal": "",
  "fechaCreacion": 1764000000000
}
```

## Pruebas unitarias

Están en `app/src/test/java/com/example/leguaalerta/` y usan JUnit 4, MockK y `kotlinx-coroutines-test`:

- `ReporteRepositoryTest`: crear con y sin conexión, sincronizar y actualizar estado (API simulada con MockK).
- `ValidadorFormulariosTest`: validaciones de correo, contraseña, código municipal y observación, y cifrado de contraseña.

Ejecutar: `./gradlew testDebugUnitTest` (o clic derecho sobre la carpeta `test` en Android Studio → *Run Tests*).

## APK firmado (release)

- Llave: `app/leguaalerta.jks` (alias `leguaalerta`, clave `legua2026`, solo con fines académicos).
- Configuración de firma: bloque `signingConfigs` en `app/build.gradle.kts`.
- Generar: `./gradlew assembleRelease`
- Resultado: `app/build/outputs/apk/release/app-release.apk` (copia en la carpeta `apk/` del repositorio).
- Ícono personalizado: escudo con casa sobre fondo verde (`res/drawable/ic_launcher_*.xml` y `mipmap-*`).

Captura del APK firmado y del `.jks`: _(agregar imagen aquí)_

## Cómo ejecutar

1. Abrir la carpeta `LeguaAlerta` en Android Studio.
2. Instalar el **SDK Platform 37** desde *Tools → SDK Manager* si no está instalado.
3. Poner en `RetrofitClient.kt` la URL base del grupo de API de Xano (termina en `/`).
4. Sincronizar Gradle y ejecutar en un emulador o teléfono con Android 7.0 o superior.

## Equipo

Proyecto desarrollado en equipo, con tablero de tareas en Trello y control de versiones en GitHub.
