package com.peluqueria.security.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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

    public void enviarEmailBienvenida(String emailDestino, String nombreCompleto, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("botpeluqueria1@gmail.com");
            helper.setTo(emailDestino);
            helper.setSubject("¡Bienvenido/a a Peluquería App!");

            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "  <meta charset=\"utf-8\">\n" +
                    "  <title>¡Te damos la bienvenida!</title>\n" +
                    "  <style>\n" +
                    "    body {\n" +
                    "      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                    "      background-color: #f7f9fa;\n" +
                    "      color: #333333;\n" +
                    "      margin: 0;\n" +
                    "      padding: 0;\n" +
                    "      -webkit-font-smoothing: antialiased;\n" +
                    "    }\n" +
                    "    .container {\n" +
                    "      max-width: 600px;\n" +
                    "      margin: 40px auto;\n" +
                    "      background-color: #ffffff;\n" +
                    "      border-radius: 16px;\n" +
                    "      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);\n" +
                    "      overflow: hidden;\n" +
                    "    }\n" +
                    "    .header {\n" +
                    "      background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);\n" +
                    "      padding: 40px 20px;\n" +
                    "      text-align: center;\n" +
                    "      color: #ffffff;\n" +
                    "    }\n" +
                    "    .header h1 {\n" +
                    "      margin: 0;\n" +
                    "      font-size: 28px;\n" +
                    "      font-weight: 700;\n" +
                    "      letter-spacing: 0.5px;\n" +
                    "    }\n" +
                    "    .content {\n" +
                    "      padding: 40px 30px;\n" +
                    "      line-height: 1.6;\n" +
                    "    }\n" +
                    "    .content p {\n" +
                    "      font-size: 16px;\n" +
                    "      margin-bottom: 24px;\n" +
                    "    }\n" +
                    "    .user-info-card {\n" +
                    "      background-color: #fff9f0;\n" +
                    "      border: 1px solid #ffe8cc;\n" +
                    "      border-radius: 12px;\n" +
                    "      padding: 20px;\n" +
                    "      margin-bottom: 30px;\n" +
                    "    }\n" +
                    "    .user-info-title {\n" +
                    "      font-weight: 700;\n" +
                    "      color: #e65100;\n" +
                    "      margin-top: 0;\n" +
                    "      margin-bottom: 12px;\n" +
                    "      font-size: 16px;\n" +
                    "      text-transform: uppercase;\n" +
                    "      letter-spacing: 0.8px;\n" +
                    "    }\n" +
                    "    .info-row {\n" +
                    "      margin-bottom: 8px;\n" +
                    "      font-size: 15px;\n" +
                    "    }\n" +
                    "    .info-row:last-child {\n" +
                    "      margin-bottom: 0;\n" +
                    "    }\n" +
                    "    .info-label {\n" +
                    "      color: #666666;\n" +
                    "      font-weight: 600;\n" +
                    "      display: inline-block;\n" +
                    "      width: 100px;\n" +
                    "    }\n" +
                    "    .info-value {\n" +
                    "      font-weight: 700;\n" +
                    "      color: #333333;\n" +
                    "    }\n" +
                    "    .footer {\n" +
                    "      background-color: #f7f9fa;\n" +
                    "      padding: 24px;\n" +
                    "      text-align: center;\n" +
                    "      font-size: 13px;\n" +
                    "      color: #888888;\n" +
                    "      border-top: 1px solid #eeeeee;\n" +
                    "    }\n" +
                    "  </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "  <div class=\"container\">\n" +
                    "    <div class=\"header\">\n" +
                    "      <h1>¡Te damos la bienvenida!</h1>\n" +
                    "    </div>\n" +
                    "    <div class=\"content\">\n" +
                    "      <p>Hola <strong>" + nombreCompleto + "</strong>,</p>\n" +
                    "      <p>¡Gracias por registrarte en <strong>Peluquería App</strong>! Estamos muy felices de tenerte con nosotros y de poder ayudarte a lucir tu mejor estilo.</p>\n" +
                    "      \n" +
                    "      <div class=\"user-info-card\">\n" +
                    "        <div class=\"user-info-title\">Detalles de tu cuenta</div>\n" +
                    "        <div class=\"info-row\">\n" +
                    "          <span class=\"info-label\">Usuario:</span>\n" +
                    "          <span class=\"info-value\">" + username + "</span>\n" +
                    "        </div>\n" +
                    "        <div class=\"info-row\">\n" +
                    "          <span class=\"info-label\">Email:</span>\n" +
                    "          <span class=\"info-value\">" + emailDestino + "</span>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "      \n" +
                    "      <p>Ya puedes abrir la aplicación en tu móvil e iniciar sesión para reservar tu primera cita y gestionar tus servicios preferidos.</p>\n" +
                    "      \n" +
                    "      <p>¡Te esperamos pronto!</p>\n" +
                    "    </div>\n" +
                    "    <div class=\"footer\">\n" +
                    "      Este es un correo automático, por favor no respondas a este mensaje.<br>\n" +
                    "      &copy; 2026 Peluquería App. Todos los derechos reservados.\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</body>\n" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            System.out.println("📧 Correo de bienvenida enviado correctamente a " + emailDestino);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar el correo de bienvenida a " + emailDestino + ": " + e.getMessage());
        }
    }
}