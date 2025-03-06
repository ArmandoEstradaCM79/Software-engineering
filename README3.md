Continuando con el desarrollo retomando la tarea 1
Tenemos Maven, JAVA y XAMPP instalados por lo que procederemos con:

EJERCICIO 1: Preparación del Proyecto y Conexión con Base de Datos
Creamos la base de datos para el inicio de sesión con administracion CRUD por roles

    -- Crear la base de datos
    CREATE DATABASE IF NOT EXISTS loginsystem;
    USE loginsystem;

    -- Crear la tabla 'user'
    CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,       -- Identificador único del usuario
    username VARCHAR(50) NOT NULL UNIQUE,       -- Nombre de usuario (único)
    password VARCHAR(100) NOT NULL,             -- Contraseña cifrada
    role VARCHAR(20) NOT NULL                   -- Rol del usuario (USER o ADMIN)
    );

Con la base de datos se crea la siguiente estructura de archivos

    src/main/java/mx/ipn/escom/loginsystem/
    ├── controller/
    │   ├── AuthController.java
    │   ├── AdminController.java
    |   ├── HomeController.java
    ├── model/
    │   ├── User.java
    ├── repository/
    │   ├── UserRepository.java
    ├── service/
    │   ├── UserService.java
    │   ├── UserDetailsServiceImpl.java
    ├── security/
    │   ├── SecurityConfig.java
    ├── LoginSystemApplication.java
    src/main/resources/
    ├── templates/
    │   ├── login.html
    │   ├── register.html
    │   ├── admin.html
    |   ├── logout.html
    │   ├── homeUser.html
    │   ├── homeAdmin.html
    ├── application.properties
    ├── schema.sql

Asi como los archivos de dockerización en la carpeta raíz

    login-system/
        ├── docker-compose.yml
        ├── DockerFile

Configuramos las dependencias

    <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.thymeleaf.extras</groupId>
			<artifactId>thymeleaf-extras-springsecurity6</artifactId>
		</dependency>
		<dependency>
        	<groupId>com.mysql</groupId>
        	<artifactId>mysql-connector-j</artifactId>
    	</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

 Una vez configurado todo correctamente se mostrará en pantalla la página para iniciar sesión
 ![image](https://github.com/user-attachments/assets/779a843d-6438-4f33-be2f-d4ea3f3a339d)

Para tener un usuario ADMINISTRADOR es importante colocar esta sentencia en el sql del servidor
    
    INSERT INTO user (username, password, role)
    VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubhFi', 'ADMIN');

Al iniciar sesión nos mostrará la página de inicio
![image](https://github.com/user-attachments/assets/15098053-dc64-4d89-8fdf-1620c55eb4e6)

Donde en la página de usuarios podremos modificar las credenciales del propio administrador como de otros usuarios
![image](https://github.com/user-attachments/assets/fe7e2569-1f2b-4687-be1d-a347647f3a84)

En editar perfil cambiaremos el usuario y la contraseña
![image](https://github.com/user-attachments/assets/151db397-d0f6-4c20-8bec-35d6de0910f8)

Cambio exitoso
![image](https://github.com/user-attachments/assets/99c70aff-77de-4526-968b-0760b5af48a0)




  
