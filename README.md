# User Service - Microservice in Spring Boot

Este es un microservicio desarrollado en **Spring Boot** que maneja la información de los usuarios, incluyendo la creación, verificación, gestión de archivos de CV (Hoja de vida) y consulta de información del usuario. Este servicio está integrado en una arquitectura de microservicios donde la autenticación y autorización de usuarios es manejada externamente por un **API Gateway**.

## Características

- **Gestión de usuarios**: Creación, verificación y recuperación de usuarios.
- **Gestión de archivos CV**: Subida, actualización, eliminación y descarga de archivos CV.
- **Roles de usuario**: Soporta roles como `USER` y `ADMIN`.
- **Base de datos**: Utiliza **MySQL** como base de datos.

## Tecnologías

- **Java 17**
- **Spring Boot 2.5+**
- **Spring Data JPA**: Para la persistencia de datos de los usuarios.
- **MySQL**: Base de datos en contenedor Docker.
- **Spring Web**: Para manejar las APIs REST.
- **Lombok**: Para reducir la verbosidad del código.

## Endpoints

### 1. **Usuario:**

#### Crear un usuario

`POST /users/create`

- **Descripción**: Crea un nuevo usuario.
- **Request Body**: `UserDto` con la información del usuario, y el parámetro `password` para la creación de la contraseña.
- **Response**: Mensaje de éxito con el código de verificación.

  ![image](https://github.com/user-attachments/assets/adb1a908-cef4-4125-8d64-f92591ed4ad2)


#### Verificar un usuario

`GET /users/verify`

- **Descripción**: Verifica un usuario con el código enviado al correo electrónico.
- **Request Param**: `code` (El código de verificación enviado al correo).
- **Response**: Mensaje de éxito si la verificación fue correcta.

  ![image](https://github.com/user-attachments/assets/1e4729ef-9784-4088-88a8-6b4199eb3006)


#### Buscar usuario por correo

`GET /users/email/{email}`

- **Descripción**: Busca un usuario por su correo electrónico.
- **Response**: Detalles del usuario.

  ![image](https://github.com/user-attachments/assets/55be82bc-f3e1-461a-ac4b-c2f7d67e344f)


#### Buscar usuario por ID

`GET /users/{id}`

- **Descripción**: Busca un usuario por su ID.
- **Response**: Detalles del usuario.

![image](https://github.com/user-attachments/assets/f51fb910-359e-4430-bd8d-28c6af53b9e3)


#### Obtener rol de un usuario por correo

`GET /users/role/email/{email}`

- **Descripción**: Obtiene el rol del usuario basado en su correo electrónico.
- **Response**: Rol del usuario (`USER` o `ADMIN`).

  ![image](https://github.com/user-attachments/assets/631b4f3a-d0fd-4843-a79c-f809e29a5d4f)


### 2. **CV (Hoja de vida):**

#### Subir CV de un usuario

`POST /cv/upload/{userId}`

- **Descripción**: Permite subir un archivo CV para el usuario.
- **Request Param**: `file` (El archivo CV en formato PDF).
- **Response**: Mensaje de éxito si la carga es exitosa.
  
![image](https://github.com/user-attachments/assets/c47f9a73-2f6a-498c-bdd6-9e9564a5372f)


#### Descargar CV de un usuario

`GET /cv/{userId}`

- **Descripción**: Permite descargar el CV de un usuario.
- **Response**: El archivo CV del usuario.

  ![image](https://github.com/user-attachments/assets/4ac63784-f852-46d9-908c-7bbda193ccd3)



#### Eliminar CV de un usuario

`DELETE /cv/{userId}`

- **Descripción**: Elimina el archivo CV de un usuario.
- **Response**: Mensaje de éxito si la eliminación es exitosa.

  ![image](https://github.com/user-attachments/assets/25540224-3371-4619-94fd-e0799f5c9756)


#### Actualizar CV de un usuario

`PUT /cv/update/{userId}`

- **Descripción**: Permite actualizar el CV de un usuario.
- **Request Param**: `file` (El nuevo archivo CV en formato PDF).
- **Response**: Mensaje de éxito si la actualización es exitosa.

  ![image](https://github.com/user-attachments/assets/382fdd64-010e-41f8-aa4d-31e959086c4e)


## Arquitectura

Este microservicio está diseñado utilizando una arquitectura basada en **Microservicios**, lo que facilita la escalabilidad y mantenimiento. Los componentes clave incluyen:

1. **`UserController`**: Controlador que maneja las operaciones relacionadas con los usuarios, como creación, autenticación, verificación y búsqueda.
2. **`CvController`**: Controlador que maneja las operaciones relacionadas con los archivos de CV, como cargar, actualizar, descargar y eliminar archivos.
3. **`UserService`**: Lógica de negocio relacionada con la creación y verificación de usuarios.
4. **`CvService`**: Lógica de negocio relacionada con la gestión de archivos CV.
5. **`UserRepository`**: Interfaz que extiende **`JpaRepository`** para gestionar las operaciones CRUD de los usuarios en la base de datos.
6. **`CvRepository`**: Interfaz que gestiona las operaciones de creación, obtención, actualización y eliminación de archivos CV.

## Base de Datos

El microservicio utiliza **MySQL** como base de datos para almacenar la información de los usuarios. La base de datos se encuentra en un contenedor **Docker** dentro de una máquina virtual de **Azure**. **Nota importante**: la base de datos solo es accesible desde la propia máquina virtual, garantizando la seguridad del sistema.

### Acceso a la Base de Datos

La base de datos está configurada para que solo pueda ser accedida desde dentro de la máquina virtual, lo que garantiza que no sea accesible desde redes externas y reduce el riesgo de posibles vulnerabilidades. Esto se logra mediante configuraciones de red en la máquina virtual y Docker.

## Swagger

Para ver todos los endpoints de la API de manera interactiva, puedes acceder a la documentación de Swagger en:

`http://localhost:8081/services/be/user-service/swagger-ui/index.html`

![image](https://github.com/user-attachments/assets/d6872621-9bda-4070-a652-278e793fcfa5)


Esto te permitirá ver todos los métodos disponibles, sus parámetros y probar las API directamente desde el navegador.

## este esta cubierto por sonarquebe

<img width="1008" height="185" alt="image" src="https://github.com/user-attachments/assets/fb4fa62b-57a5-4c90-9f04-91e8c3bbe081" />


## diagram de clases

![image](https://github.com/user-attachments/assets/b18f351e-8c95-4fb0-bf30-c89ba798ef0e)

