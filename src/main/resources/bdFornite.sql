-- ============================================
--  CREAR BASE DE DATOS
-- ============================================
DROP DATABASE IF EXISTS `fornite2`;
CREATE DATABASE `fornite2`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE `fornite2`;
--  Crear usuario DS3 con contraseña ds3
CREATE USER IF NOT EXISTS 'DS3'@'localhost' IDENTIFIED BY 'ds3';

-- Dar todos los permisos sobre la base de datos empresaDS3
GRANT ALL PRIVILEGES ON fornite2.* TO 'DS3'@'localhost';

--  Aplicar los cambios de permisos
FLUSH PRIVILEGES;

-- ============================================
--  BORRAR TABLAS SI YA EXISTEN (ORDEN CORRECTO)
-- ============================================
DROP TABLE IF EXISTS `personaje_habilidad`;
DROP TABLE IF EXISTS `personaje`;
DROP TABLE IF EXISTS `habilidad`;
DROP TABLE IF EXISTS `clase`;
DROP TABLE IF EXISTS `arma`;

-- ============================================
--  CREAR TABLAS (InnoDB - integridad referencial)
-- ============================================

CREATE TABLE `arma` (
  `id_arma` INT NOT NULL AUTO_INCREMENT,
  `nombre_arma` VARCHAR(50) NOT NULL,
  `descripcion_arma` TEXT NOT NULL,
  `danio` INT NOT NULL,
  PRIMARY KEY (`id_arma`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `clase` (
  `id_clase` INT NOT NULL AUTO_INCREMENT,
  `nombre_clase` VARCHAR(50) NOT NULL,
  `descripcion_clase` TEXT NOT NULL,
  PRIMARY KEY (`id_clase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `habilidad` (
  `id_habilidad` INT NOT NULL AUTO_INCREMENT,
  `nombre_habilidad` VARCHAR(50) NOT NULL,
  `descripcion_habilidad` TEXT NOT NULL,
  `poder_base` INT NOT NULL,
  PRIMARY KEY (`id_habilidad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `personaje` (
  `id_personaje` INT NOT NULL AUTO_INCREMENT,
  `nombre_personaje` VARCHAR(100) NOT NULL,
  `descripcion_personaje` TEXT NOT NULL,
  `id_clase` INT NULL,
  `id_arma` INT NULL,
  `is_ally` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id_personaje`),
  FOREIGN KEY (`id_arma`) REFERENCES `arma`(`id_arma`)
    ON UPDATE CASCADE
    ON DELETE SET NULL,
  FOREIGN KEY (`id_clase`) REFERENCES `clase`(`id_clase`)
    ON UPDATE CASCADE
    ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `personaje_habilidad` (
  `id_habilidad` INT NOT NULL,
  `nivel_habilidad` INT NOT NULL DEFAULT 1,
  `id_personaje` INT NOT NULL,
  PRIMARY KEY (`id_personaje`, `id_habilidad`),
  FOREIGN KEY (`id_personaje`) REFERENCES `personaje`(`id_personaje`)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
  FOREIGN KEY (`id_habilidad`) REFERENCES `habilidad`(`id_habilidad`)
    ON UPDATE CASCADE
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
