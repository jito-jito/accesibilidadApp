# Accesibilidad App

## Alcance
Esta es una aplicación nativa de Android desarrollada en Kotlin y Jetpack Compose, diseñada para romper las barreras de comunicación e información que enfrentan las personas con discapacidad visual, auditiva y del habla.

**El Problema:** La falta de accesibilidad en aplicaciones estándar y la dependencia de terceros para tareas cotidianas limitan la autonomía de los usuarios.

**La Solución:** Un "Asistente Multimodal" que utiliza características del teléfono para transformar la información del entorno (OCR, TTS, STT).

**Enfoque Técnico:** El proyecto prioriza la Accesibilidad Universal (A11y), implementando Material Design 3 bajo una arquitectura de software limpia (Clean Architecture), separando las reglas de negocio de la interfaz de usuario.

---

## Estructura del Proyecto

La aplicación se organiza siguiendo patrones de arquitectura limpia para asegurar escalabilidad y testeabilidad:

- **data**: Implementa el `AuthRepository`, que actúa como base de datos en memoria de ejecución.
- **domain**: Contiene las Entidades (`User`) y Casos de Uso (`LoginUseCase`, `RegisterUseCase`) que rigen las reglas de negocio puras.
- **ui**: Organizado en `screens`, `viewmodels` y `components`, manejando el estado de forma reactiva con StateFlow.
- **util**: Contiene utilidades genéricas, funciones inline y extensiones de sistema.

---

## Funcionalidades y Conceptos Aplicados

A continuación se detallan los **9 conceptos trabajados en la asignatura** integrados en el flujo de autenticación:

### 1. Funciones de orden superior

**Ubicación:** `MainActivity.kt`, `ui/screens/Login.kt`

**Descripción:** Se utilizan para gestionar la navegación mediante lambdas (como `onLoginSuccess`) para las vistas de Login y Registro de la aplicación.

### 2. Filter
**Ubicación:** `ui/viewmodels/RegisterViewModel.kt` y `data/repository/AuthRepository.kt`.

**Descripción:** Empleado en el repositorio(AuthRepository) para buscar usuarios existentes por email y en el ViewModel(RegisterViewModel, por medio del método `onNombreChanged`) para sanitizar el nombre del usuario, eliminando caracteres no permitidos en tiempo real.

### 3. Funciones inline
**Ubicación:** `util/SafeRun.kt`.

**Descripción:** La función `safeRun` se declara como `inline` para optimizar el rendimiento al ejecutar bloques de código (lambdas) de validación y persistencia, evitando la sobrecarga de memoria en llamadas repetitivas.

### 4. Lambdas
**Ubicación:** `ui/screens/Login.kt` y `ui/screens/Register.kt`.

**Descripción:** Cruciales en Jetpack Compose para el manejo de eventos (clics) y el paso de funciones de actualización de estado desde la UI hacia los ViewModels.

### 5. Lambda con etiqueta
**Ubicación:** `ui/viewmodels/RegisterViewModel.kt`.

**Descripción:** Utilizada dentro de la función `filter` para el campo nombre(`onNombreChanged`). La etiqueta `@filter` permite un retorno no local explícito, controlando qué iteración del filtrado debe descartarse sin salir de la función principal.

### 6. Funciones de extensión
**Ubicación:** `util/DebugUtils.kt`.

**Descripción:** Se extiende la clase `Any` con el método `logClick()` para facilitar la depuración de eventos en toda la aplicación sin modificar las clases base del sistema.

### 7. Propiedades de extensión
**Ubicación:** `domain/model/User.kt`.

**Descripción:** Se implementa la propiedad `User.esValido`, que permite al dominio validar la integridad de una entidad de forma dinámica sin almacenar estados adicionales en la clase de datos.

### 8. Excepciones
**Ubicación:** `LoginUseCase.kt`.

**Descripción:** Se utilizan para el control de flujo de negocio, lanzando errores específicos cuando las credenciales son incorrectas, garantizando que la app no entre en un estado inconsistente.

### 9. Try / Catch
**Ubicación:** `util/SafeRun.kt`.

**Descripción:** Encapsulado en la función `safeRun`, este bloque captura las excepciones lanzadas por la lógica de negocio y las transforma en objetos `Result`, permitiendo a la UI mostrar mensajes de error amigables sin interrumpir la ejecución.