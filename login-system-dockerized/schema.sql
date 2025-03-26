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

