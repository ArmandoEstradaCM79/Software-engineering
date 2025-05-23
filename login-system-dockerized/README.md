
# Proyecto individual Log in, Dockerización y Bibliotecas recomendadas

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

Ejercicio2: Implementación de Vista de Login

 Una vez configurado todo correctamente se mostrará en pantalla la página para iniciar sesión
 
 ![image](https://github.com/user-attachments/assets/779a843d-6438-4f33-be2f-d4ea3f3a339d)

Ejercicio 3: Implementación de un sistema de autenticación, y roles y permisos

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

En la vista de administrador tambien es posible editar los datos de todos los usuarios asi como el rol que tomará incluso poder ELIMINAR usuarios

![image](https://github.com/user-attachments/assets/16cddbfa-fc80-40a3-a622-d5bd74628f42)

Registramos un nuevo usuario

![image](https://github.com/user-attachments/assets/ec4ef89b-9524-454c-bb8c-ace1362cc27a)

A continuacion se muestra la vista de modo usuario

![image](https://github.com/user-attachments/assets/a6a6894e-e16b-4cd0-b2b9-ca62cc1b384b)

Donde solo puede editar tu propio perfil, ademas de la exploración de la página

![image](https://github.com/user-attachments/assets/0f3103e1-7dfb-4e0b-a8ff-1bdf76f452b5)

Finalmente así queda la base de datos

![image](https://github.com/user-attachments/assets/95784255-e0a1-4b7d-93bc-ff6b96891a5b)


Ejercicio 4: Dockerización de la Aplicación
Creamos un Dockerfile en la carpeta raíz del proyecto
		
  	FROM openjdk:23-jdk-slim
   
	# Copia wait-for-it.sh al contenedor
	COPY wait-for-it.sh /wait-for-it.sh
	RUN chmod +x /wait-for-it.sh
	
	WORKDIR /app
	COPY target/login-system-0.0.1-SNAPSHOT.jar app.jar
	
	EXPOSE 8080
	
	# Espera a que MySQL esté listo y luego ejecuta la aplicación
	ENTRYPOINT ["/wait-for-it.sh", "db:3306", "--", "java", "-jar", "app.jar"]

Posteriormente creamos docker-compose.yml igualmente en la carpeta raíz

	version: '3.8'

	services:
	  db:
	    image: mysql:8.0
	    container_name: mysql_db
	    environment:
	      MYSQL_ROOT_PASSWORD: rootpassword  # Configura la contraseña del usuario root
	      MYSQL_DATABASE: loginsystem        # Crea la base de datos automáticamente
	    ports:
	      - "3307:3306"
	    volumes:
	      - mysql_data:/var/lib/mysql
	
	  app:
	    build: .
	    container_name: springboot_app
	    ports:
	      - "8080:8080"
	    environment:
	      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/loginsystem
	      SPRING_DATASOURCE_USERNAME: root
	      SPRING_DATASOURCE_PASSWORD: rootpassword
	    depends_on:
	      - db
	
	volumes:
	  mysql_data:

Creamos una cuenta nueva en https://hub.docker.com/ y descargamos DockerDesktop

![Captura de pantalla 2025-03-05 225749](https://github.com/user-attachments/assets/a5ad3412-0aeb-4a89-b6c3-f2a85eb9d37e)

Modificamos application properties

	spring.application.name=login-system
	spring.datasource.url=jdbc:mysql://mysql_database:3306/loginsystem
	spring.datasource.username=chalesmanto
	spring.datasource.password=1234
	spring.jpa.hibernate.ddl-auto=update
	spring.jpa.show-sql=true
	spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

Ejecutamos 'docker build -t "login-admin" .' y tambien 'docker pull mysql' para crear las imágenes de nuestro proyecto

![image](https://github.com/user-attachments/assets/08ec60a2-3fcb-402c-9548-e795a44d7072)


Finalmente ejecutamos 'docker-compose up' para levantar el programa en el contenedor
   
![image](https://github.com/user-attachments/assets/31b4a489-2bd5-413d-ba30-e3b2f47b1e0e)

Ingresamos a http://localhost:8080/ y podremos observar el Login funcionando

![Captura de pantalla 2025-03-05 232835](https://github.com/user-attachments/assets/baff3397-3076-4dc3-bded-6dee9664d402)
