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


/*INSERT INTO `tipo_servicio` (`nombre`, `descripcion`) VALUES
('Peluquería Mujer', 'Cortes, peinados y tratamientos para mujer'),
('Peluquería Hombre', 'Cortes y barbería para caballero'),
('Coloración', 'Tintes, mechas y baños de color'),
('Estética Facial', 'Limpiezas, tratamientos anti-edad y mascarillas'),
('Estética Corporal', 'Masajes y tratamientos reductores'),
('Manicura', 'Cuidado y esmaltado de manos'),
('Pedicura', 'Cuidado y tratamiento de pies'),
('Depilación', 'Depilación con cera y láser'),
('Maquillaje', 'Maquillaje social y de eventos'),
('Tratamientos Capilares', 'Hidratación, anti-caída y alisados');





INSERT INTO `servicio` (`nombre`, `descripcion`, `duracion_bloques`, `precio`, `tipo_servicio_id`) VALUES
('Corte Bob', 'Corte estilo Bob clásico con lavado', 2, 25.50, 1),
('Afeitado Navaja', 'Afeitado tradicional con toalla caliente', 1, 15.00, 2),
('Mechas Balayage', 'Técnica de degradado natural', 4, 60.00, 3),
('Limpieza Profunda', 'Limpieza facial con extracción y vapor', 2, 35.00, 4),
('Masaje Relajante', 'Masaje de cuerpo entero con aceites', 3, 45.00, 5),
('Manicura Semipermanente', 'Limado y esmaltado de larga duración', 1, 18.00, 6),
('Pedicura Spa', 'Tratamiento completo de pies con hidratación', 2, 28.00, 7),
('Depilación Piernas', 'Depilación con cera tibia piernas enteras', 1, 22.00, 8),
('Maquillaje Novia', 'Prueba y maquillaje para el día de la boda', 3, 150.00, 9),
('Alisado Keratina', 'Tratamiento alisador y reparador', 5, 120.00, 10);




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