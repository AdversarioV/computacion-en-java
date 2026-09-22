# computacion-en-java

Sistema de administración de citas para consultorio clínico — Evidencia de Computación en Java, Universidad Tecmilenio.

## Descripción

Aplicación de consola desarrollada en Java que simula un sistema de administración de citas para un consultorio clínico. Permite registrar doctores y pacientes, crear citas médicas relacionadas con un doctor y un paciente, y controlar el acceso al sistema mediante usuarios administradores. La información se almacena de forma persistente en archivos de texto plano con formato CSV.

## Instalación y configuración

### Requisitos previos

- JDK 11 o superior
- Git
- IntelliJ IDEA (opcional, cualquier IDE compatible con Java funciona)

### Pasos

1. Clonar el repositorio:

   git clone https://github.com/AdversarioV/computacion-en-java.git

   

2. Abrir el proyecto en IntelliJ IDEA con `File > Open` y seleccionar la carpeta del proyecto.

3. Configurar el SDK del proyecto en Java 11 desde `File > Project Structure > Project > SDK`.

4. Verificar que exista la carpeta `db` en la raíz del proyecto. En esta carpeta se generan los archivos CSV con la información del sistema. Si los archivos no existen, el programa los crea automáticamente al iniciar.

5. Compilar y ejecutar la clase `Main`.

### Generar el archivo ejecutable

El proyecto está configurado para compilarse como FAT JAR, de modo que todas las dependencias queden incluidas y la aplicación sea portable. Para ejecutarlo:

java -jar consultorio.jar


## Uso del programa

Al iniciar, el programa solicita un identificador y una contraseña de administrador. Solo los usuarios registrados como administradores pueden acceder al sistema.

Una vez autenticado, se muestra un menú con las siguientes opciones:

| Opción | Descripción |
|--------|-------------|
| 1 | Dar de alta un doctor (identificador, nombre completo y especialidad) |
| 2 | Dar de alta un paciente (identificador y nombre completo) |
| 3 | Crear una cita médica (identificador, fecha y hora, motivo) |
| 4 | Consultar los registros existentes |
| 5 | Salir del programa |

Para crear una cita es necesario que existan al menos un doctor y un paciente registrados, ya que cada cita debe estar relacionada con ambos.

Los datos ingresados se validan antes de guardarse. Si ocurre un error durante la operación, el programa muestra el mensaje correspondiente y continúa su ejecución sin cerrarse.

## Estructura del proyecto

computacion-en-java/
├── src/ Código fuente de la aplicación
├── db/ Archivos CSV con la información del sistema
├── .gitignore
├── LICENSE
└── README.md


Los archivos contenidos en la carpeta `db` no se incluyen en el repositorio, ya que corresponden a datos generados en tiempo de ejecución.

## Créditos

Desarrollado por Mauricio Gonzalez como evidencia de la materia Computación en Java.

Universidad Tecmilenio.
Profesora: Silvia Tello Zúñiga.

## Licencia

Este proyecto se distribuye bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.
