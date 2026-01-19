package com.peluqueria.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ServicioEmail {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigoRecuperacion(String emailDestino, String codigo) {
        SimpleMailMessage message = new SimpleMailMessage();

        // 👇 IMPORTANTE: Aquí debe ir el mismo correo que en application.properties
        message.setFrom("botpeluqueria1@gmail.com");
        message.setTo(emailDestino);
        message.setSubject("Código de Recuperación - Peluquería App");
        message.setText("Hola,\n\nTu código para restablecer la contraseña es:\n\n" +
                codigo +
                "\n\nSi no has solicitado este código, ignora este mensaje.");

        mailSender.send(message);
        System.out.println("📧 Email enviado correctamente a " + emailDestino);
    }
}