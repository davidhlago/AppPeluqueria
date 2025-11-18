package com.peluqueria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PeluqueriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeluqueriaApplication.class, args);
	}

}


//http://localhost:8080/swagger-ui/index.html#/



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