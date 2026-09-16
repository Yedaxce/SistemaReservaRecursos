# Sistema de Reserva de Recursos

Proyecto 1 de **EIF206 - Programación 3 (2026-II)**. 
Es una aplicación en Java para administrar recursos y las reservas.

## Descripción

El sistema permite que los funcionarios ingresen, describan una actividad, seleccionen la fecha y el horario, indiquen las categorías de recursos requeridas y registren una reserva. La aplicación verifica la disponibilidad de al menos un recurso de cada categoría solicitada y asigna el primer recurso disponible de cada una. Cuando una categoría no tiene disponibilidad, se informa cuáles categorías impiden registrar la reserva.

También se puede utilizar el módulo de asistencia con IA para extraer los datos de una reserva a partir de una frase en lenguaje natural. Los datos extraídos se cargan en el formulario y pueden modificarse antes de confirmar la reserva.

## Funcionalidades

### Ingreso y usuarios

- Inicio de sesión para usuarios administradores y funcionarios.
- Cambio de contraseña.
- Control de acceso por rol.

### Reservas
Exclusivo de funcionarios
- Consulta de las reservas del funcionario autenticado.
- Creación de reservas con actividad, fecha, hora inicial, hora final y categorías de recursos.
- Validación de disponibilidad y detección de conflictos de horario.
- Asignación automática de recursos concretos.
- Cancelación de reservas.
- Carga de datos de reserva mediante IA.

### Administración

Disponible para usuarios con rol administrador:

- Gestión de funcionarios: búsqueda por ID o nombre, creación, modificación y eliminación.
- Gestión de categorías de recursos: búsqueda, creación, modificación y eliminación.
- Gestión de recursos: filtrado por categoría, creación, modificación y eliminación.

### Consultas y reportes
Habilitado a ambos roles de usuario
- **Calendarización de recursos:** matriz por fecha y categoría; las filas representan horas y las columnas representan recursos. Las celdas ocupadas muestran la actividad y el funcionario responsable.
- **Programación de actividades:** matriz semanal con las horas en las filas y los días de la semana en las columnas.
- **Estadísticas:** cantidad de recursos utilizados por categoría y cantidad de actividades por semana dentro de un rango de fechas.
- Gráficos de barras para las estadísticas.
- Generación de reportes PDF en los módulos correspondientes.

## Arquitectura

El proyecto utiliza arquitectura por capas y el patrón **Modelo-Vista-Controlador (MVC)**:

```text
src/main/java/reservas/
├── dao/                 Persistencia y acceso a datos XML
├── logic/               Entidades y reglas básicas del dominio
├── services/            Lógica de negocio y servicios
└── presentation/        Vistas, modelos y controladores Swing
```

Los datos se almacenan en `datos.xml` mediante JAXB. Las fechas y horas se serializan utilizando adaptadores para `LocalDate` y `LocalTime`.

## Tecnologías

- Java 26
- Swing
- Maven
- JAXB
- LGoodDatePicker
- JFreeChart
- iText 7
- JUnit Jupiter 5

## Requisitos

- JDK compatible con la versión configurada en `pom.xml`.
- Maven instalado y disponible en el `PATH`.

## Ejecución

Desde la raíz del proyecto:

```bash
mvn compile
mvn exec:java -Dexec.mainClass=reservas.Application
```

Si el proyecto se ejecuta desde IntelliJ IDEA, se puede iniciar la clase:

```text
reservas.Application
```

La aplicación utiliza el archivo `datos.xml` ubicado en la raíz del proyecto. Se recomienda realizar una copia de respaldo antes de modificar datos manualmente.

## Pruebas

El proyecto contiene pruebas unitarias y de integración con JUnit Jupiter.

### Pruebas unitarias — Surefire

Las clases con sufijo `Test.java` verifican entidades y reglas aisladas:

```bash
mvn test
```

### Pruebas de integración — Failsafe

Las clases con sufijo `IT.java` verifican la interacción entre servicios, DAO y persistencia XML:

```bash
mvn verify
```

Las pruebas de integración utilizan archivos XML temporales para no alterar `datos.xml`.

## Estructura de pruebas

```text
src/test/java/reservas/
├── dao/
│   ├── EstadisticaServiceIT.java
│   ├── ReservaDAOIT.java
│   ├── ReservaServiceIT.java
│   └── UsuarioServiceIT.java
└── services/
│   ├── EstadisticaServiceTest.java
│   ├── IAServiceTest.java
│   ├── RecursoeServiceTest.java
│   ├── ReservaServiceTest.java
│   └── UsuarioServiceTest.java
└── unitTest/
    ├── CategoriaRecursoTest.java
    ├── FuncionarioTest.java
    ├── RecursoTest.java
    └── ReservaTest.java
```

## Reglas de negocio relevantes

- Una reserva requiere actividad, fecha, funcionario y un horario válido.
- La hora inicial debe ser anterior a la hora final.
- Una reserva solo se registra si existe disponibilidad para todas las categorías solicitadas.
- Las reservas canceladas no ocupan recursos ni se consideran en las estadísticas.
- La calendarización muestra únicamente reservas activas asociadas a recursos de la categoría seleccionada.
- Las entradas inválidas deben ser rechazadas y reportadas al usuario.

## Integrantes del proyecto

- Yency Amador Centeno
- Wagner Barrantes Mora
- Daniela Lacayo Redondo

## Profesor a cargo

- Jose Sanchez Salazar