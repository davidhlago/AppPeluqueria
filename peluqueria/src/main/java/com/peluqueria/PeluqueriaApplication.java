package com.peluqueria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PeluqueriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeluqueriaApplication.class, args);
	}

}


/*
*  post http://localhost:8080/api/auth/signup/admin
* {
    "nombre": "Carlos",
    "apellidos": "Sanz",
    "username": "admin_jefe",
    "email": "otro_email_inventado@peluqueria.com",
    "password": "password123",
    "especialidad": "Gerencia"
}
*
*
* post http://localhost:8080/api/auth/signin
* {
    "username": "admin_jefe",
    "password": "password123"
}
* get http://localhost:8080/api/test/admin
*
* */

//http://localhost:8080/swagger-ui/index.html#/


/*INSERT INTO tipo_servicio (id, nombre, descripcion) VALUES
(1, 'Peluquería', 'Servicios de corte, coloración, peinado y tratamientos capilares.'),
(2, 'Manicura y Pedicura', 'Cuidado y esmaltado de uñas de manos y pies, incluyendo tratamientos de parafina.'),
(3, 'Depilación', 'Servicios de eliminación de vello facial y corporal (cera o métodos similares).'),
(4, 'Pestañas y Cejas', 'Servicios especializados como lifting, laminado y tinte.'),
(5, 'Tratamientos Faciales', 'Limpieza profunda, hidratación y tratamientos de antienvejecimiento o específicos para la piel.'),
(6, 'Tratamientos Corporales', 'Masajes, drenaje linfático, exfoliación y tratamientos reductores.');





INSERT INTO servicio (id_servicio, nombre, descripcion, precio, duracion_bloques, tipo_servicio_id) VALUES
(1, 'Lavado + Peinado', 'Lavado y peinado básico para todo tipo de cabello', 5, 2, 1),
(2, 'Lavado + Peinado + Recogido', 'Peinado con recogido para eventos o ocasiones especiales', 12, 3, 1),
(3, 'Corte femenino', 'Corte de cabello para mujer con lavado incluido', 5, 3, 1),
(4, 'Corte masculino', 'Corte de cabello para hombre con lavado incluido', 2, 2, 1),
(5, 'Color completo', 'Aplicación de color en todo el cabello', 8, 2.5, 1),
(6, 'Color raíces', 'Aplicación de color solo en raíces', 5, 2.5, 1),
(7, 'Mechas', 'Técnica de coloración parcial para dar luz al cabello', 10, 4, 1),
(8, 'Cambio de estilo + color', 'Transformación completa del look con nuevo color', 15, 3, 1),
(9, 'Permanente', 'Cambio de forma del cabello mediante técnica permanente', 10, 4, 1),
(10, 'Tratamiento capilar', 'Tratamiento nutritivo y reparador para el cabello', 5, 3, 1),
(11, 'Manicura exprés', 'Limpieza y limado de uñas sin esmaltado', 2, 0.75, 2),
(12, 'Manicura completa semipermanente', 'Manicura con esmaltado semipermanente de larga duración', 5, 1.5, 2),
(13, 'Pedicura semipermanente', 'Pedicura con esmaltado semipermanente', 5, 1.5, 2),
(14, 'Parafina manos', 'Tratamiento hidratante con baño de parafina', 2, 1, 2),
(15, 'Uñas artificiales', 'Aplicación de uñas postizas con acabado profesional', 10, 3, 2),
(16, 'Manicura semipermanente', 'Manicura con esmaltado semipermanente', 5, 2, 2),
(17, 'Manicura normal', 'Manicura básica con esmaltado tradicional', 2, 1, 2),
(18, 'Depilación facial', 'Eliminación de vello en labio, cejas, mentón y zonas faciales', 3, 0.5, 3),
(19, 'Diseño de cejas', 'Moldeado y definición de cejas según el rostro', 2, 0.5, 3),
(20, 'Depilación de axilas', 'Eliminación de vello en la zona de las axilas', 4, 0.3, 3),
(21, 'Depilación de brazos', 'Depilación completa de ambos brazos', 6, 0.5, 3),
(22, 'Depilación medias piernas', 'Depilación desde rodilla hasta tobillo', 8, 0.5, 3),
(23, 'Depilación piernas completas', 'Depilación total de ambas piernas', 10, 1, 3),
(24, 'Depilación ingles', 'Depilación básica en zona íntima', 4, 0.5, 3),
(25, 'Depilación brasileña', 'Depilación íntima estilo brasileño', 5, 0.75, 3),
(26, 'Depilación integral', 'Depilación completa en zona íntima', 6, 0.75, 3),
(27, 'Depilación espalda', 'Eliminación de vello en espalda completa', 4, 0.75, 3),
(28, 'Depilación abdomen', 'Eliminación de vello en zona abdominal', 3, 0.5, 3),
(29, 'Pack completo', 'Depilación de cejas, labio, axilas, piernas completas e ingles', 18, 1.5, 3),
(30, 'Lifting pestañas', 'Elevación de pestañas naturales para efecto rizado', 8, 0.75, 4),
(31, 'Laminado cejas', 'Alisado y fijación de cejas para forma definida', 8, 0.75, 4),
(32, 'Tinte pestañas', 'Coloración de pestañas para mayor intensidad', 5, 0.5, 4),
(33, 'Lifting + Tinte pestañas', 'Elevación y coloración de pestañas en una sola sesión', 12, 1, 4),
(34, 'Drenaje linfático facial', 'Masaje facial para mejorar circulación y eliminar toxinas', 3, 1, 5),
(35, 'Higiene facial', 'Limpieza profunda de la piel del rostro', 0, 1.5, 5),
(36, 'Profunda facial', 'Tratamiento facial intensivo con extracción y mascarilla', 15, 1.5, 5),
(37, 'Hidratación facial', 'Tratamiento para restaurar la hidratación de la piel', 15, 1.5, 5),
(38, 'Antienvejecimiento', 'Tratamiento facial para reducir signos de edad', 15, 1.5, 5),
(39, 'Vitamina C', 'Tratamiento facial antioxidante con vitamina C', 15, 1.5, 5),
(40, 'Pieles grasas', 'Tratamiento facial para controlar el exceso de grasa', 15, 1.5, 5),
(41, 'Despigmentante', 'Tratamiento para reducir manchas y unificar tono', 15, 1.5, 5),
(42, 'Bolsas y ojeras', 'Tratamiento para mejorar la apariencia del contorno de ojos', 15, 1.5, 5),
(43, 'Drenaje linfático corporal', 'Masaje corporal para mejorar circulación y eliminar líquidos', 6, 1.5, 6),
(44, 'Drenaje linfático completo', 'Masaje completo para activar el sistema linfático', 8, 2.5, 6),
(45, 'Exfoliación corporal', 'Eliminación de células muertas y renovación de la piel', 10, 1, 6);




-- Usuario Padre
INSERT INTO `usuarios` (`id`, `tipo_usuario`, `nombre`, `apellidos`, `email`, `password`, `rol`, `username`) VALUES
(2, 'ADMIN', 'Laura', 'Gomez', 'laura.admin@peluqueria.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'ADMIN', 'admin_laura'),
(3, 'ADMIN', 'David', 'Martinez', 'david.admin@peluqueria.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'ADMIN', 'admin_david'),
(4, 'ADMIN', 'Elena', 'Vazquez', 'elena.admin@peluqueria.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'ADMIN', 'admin_elena');

-- Tabla Hija Admin
INSERT INTO `admin` (`id`, `especialidad`) VALUES
(2, 'Recursos Humanos'),
(3, 'Marketing Digital'),
(4, 'Contabilidad');


-- Usuario Padre
INSERT INTO `usuarios` (`id`, `tipo_usuario`, `nombre`, `apellidos`, `email`, `password`, `rol`, `username`) VALUES
(5, 'CLIENTE', 'Ana', 'Torres', 'ana.torres@gmail.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'CLIENTE', 'cliente_ana'),
(6, 'CLIENTE', 'Javier', 'Ruiz', 'javier.ruiz@hotmail.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'CLIENTE', 'cliente_javi'),
(7, 'CLIENTE', 'Sofia', 'López', 'sofia.lopez@yahoo.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'CLIENTE', 'cliente_sofia'),
(8, 'CLIENTE', 'Miguel', 'Angel', 'miguel.angel@outlook.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'CLIENTE', 'cliente_miguel');

-- Tabla Hija Cliente
INSERT INTO `cliente` (`id`, `telefono`, `direccion`, `observacion`, `alergenos`) VALUES
(5, '600111222', 'Calle Mayor 10, 2A', 'Prefiere citas por la mañana', 'Alergia al Látex'),
(6, '600333444', 'Av. Libertad 45', 'Cuero cabelludo sensible', 'Ninguno'),
(7, '600555666', 'Plaza España 1', 'Suele venir con su hija', 'Alergia al Amoniaco'),
(8, '600777888', 'Calle Pez 8', 'Le gusta el café solo', 'Ninguno');


-- Usuario Padre
INSERT INTO `usuarios` (`id`, `tipo_usuario`, `nombre`, `apellidos`, `email`, `password`, `rol`, `username`) VALUES
(9, 'GRUPO', 'Clase', '1A', 'grupo.1a@escuela.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'GRUPO', 'grupo_1a'),
(10, 'GRUPO', 'Clase', '1B', 'grupo.1b@escuela.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'GRUPO', 'grupo_1b'),
(11, 'GRUPO', 'Clase', '2A', 'grupo.2a@escuela.com', '$2a$10$sfBqCurhq8oh57/sWJJeauzwjM8brU3.o6SrEjX5zqp1D3I3OgmQm', 'GRUPO', 'grupo_2a');

-- Tabla Hija Grupo
INSERT INTO `grupo` (`id`, `curso`, `turno`) VALUES
(9, '1º Peluquería', 'Mañana'),
(10, '1º Estética', 'Tarde'),
(11, '2º Peluquería', 'Mañana');*/

















/*
*   CREATE TABLE `servicio` (
    `id_servicio` BIGINT NOT NULL AUTO_INCREMENT,
    `tipo_servicio` VARCHAR(255) NOT NULL, -- Mapeo del Enum TiposServicio como STRING
    `nombre` VARCHAR(255) NOT NULL,
    `descripcion` TEXT,
    `duracion_bloques` INT NOT NULL,
    `precio` DOUBLE NOT NULL,
    PRIMARY KEY (`id_servicio`)
);
*
*
* INSERT INTO `servicio` (`tipo_servicio`, `nombre`, `descripcion`, `duracion_bloques`, `precio`) VALUES
('CORTE_MASCULINO', 'Corte Clásico Caballero', 'Corte de pelo y arreglo de contornos.', 2, 15.00),
('CORTE_FEMENINO', 'Corte a la Moda Mujer', 'Corte con lavado y secado básico.', 3, 25.00),
('PEINADO', 'Secado y Peinado', 'Lavado y peinado con secador.', 1, 10.00),
('COLOR_COMPLETO', 'Tinte Completo', 'Aplicación de color uniforme en todo el cabello.', 4, 45.00),
('MECHAS', 'Mechas Balayage', 'Técnica de mechas con efecto natural.', 6, 75.00),
('TRATAMIENTO', 'Tratamiento de Keratina', 'Tratamiento para alisar e hidratar el cabello.', 5, 60.00),
('PEINADO_RECOGIDO', 'Recogido de Fiesta', 'Peinado elegante para eventos.', 3, 35.00),
('CORTE_ESTILO', 'Asesoría y Corte', 'Consulta de estilo y corte de tendencia.', 4, 30.00),
('COLOR_RAICES', 'Retoque de Raíces', 'Aplicación de color solo en la raíz.', 2, 30.00),
('CAMBIO_ESTILO_COLOR', 'Decoloración Completa', 'Proceso para cambiar radicalmente el tono.', 8, 90.00);
*
*
* */