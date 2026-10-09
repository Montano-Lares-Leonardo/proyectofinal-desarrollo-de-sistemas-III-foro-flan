<div align="center">
    <img
    src="/docs/assets/logo.png"
    alt="Logo 'Foro Flan'"
    width="260"
/>
  <h1 align="center">Foro Flan</h1>
  <h4 align="center">Foro elaborado en Java y MySQL.</h4>
</div>


## Acerca de
* El proyecto _Foro Flan_ utiliza Java para la creación de una interfaz gráfica por la cual el usuario pueda comunicarse con una base de datos relacional MySQL, permitiendo la creación de usuarios, publicaciones y respuestas en un foro.
* Desarrollado como proyecto final en la clase _Desarrollo de Sistemas III_, nos permitió aprender a crear un entorno donde el código y las bases de datos convergen para crear una aplicación funcional.
* Creado bajo la tutoría del profesor José Mercado Chan por los integrantes:
  * Leonardo Montaño Lares
  * Gerardo Tapia Fimbres
  * Alethse Maria Felix Espejo
  * Andrea Guadalupe López González

## Funcionalidades
* Usuarios personalizados
  * Crear usuarios con nombre y contraseña
  * Elegir foto de perfil para usuarios
* Posts 
  * Subir publicaciones públicas y privadas
  * Responder a publicaciones
* Notificaciones
  * Notificar al usuario cuando responden a sus posts y llevarlo al post respondido

## Dependencias
* Java 8
* MySQL
* JConnector MySQL
* IntelliJ IDE

## Instrucciones de uso
1. Instalar la versión más reciente de [Java](https://www.java.com/es/download/?locale=es).
2. Instalar la versión más reciente de MySQL, en este caso la versión [Community](vhttps://dev.mysql.com/downloads/installer/).
3. Instalar la versión más reciente de [IntelliJ IDE](https://www.jetbrains.com/idea/download/?section=windows).
4. Clonar el repositorio.
    ```
    git clone https://github.com/Montano-Lares-Leonardo/proyectofinal-desarrollo-de-sistemas-III-foro-flan.git
    ```
5. Crear una conexión MySQL en IntelliJ IDE a localhost.
6. Ejecutar el archivo 'flan sql para hacer tablas.sql' para crear la base de datos, un usuario y las tablas que permiten el correcto funcionamiento de la aplicación.
7. Compilar y ejecutar el archivo 'MainPageApplicacion' que se encuentra en el directorio 'src/main/java/com/example/proyectoflan'.

## Estructura del proyecto
```text
foro-flan: Directorio raíz de la aplicación.
├── src/main/java/com/example/proyectoflan: Archivos necesarios para que el proyecto funcione.
│   └── MainPageController.java: Archivo que ejecuta el programa.
└── docs: Documentación del proyecto e imágenes.
```
## Capturas de pantalla
![Página de inicio](/docs/assets/Screenshot1.png?raw=true "Página de inicio.")
![Inicio de sesión](/docs/assets/Screenshot2.png?raw=true "Inicio de sesión.")
![Creación de usuario](/docs/assets/Screenshot3.png?raw=true "Creación de usuario.")
![Publicación de ejemplo](/docs/assets/Screenshot4.png?raw=true "Publicación de ejemplo.")
![Publicación y respuesta](/docs/assets/Screenshot5.png?raw=true "Publicación y respuesta.")
![Responder a publicación](/docs/assets/Screenshot6.png?raw=true "Responder a publicación.")
