package com.linkfix.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

@Service
public class EmailService {

    private final JavaMailSender eMailSender;

    public EmailService(JavaMailSender mailSender){
        this.eMailSender=mailSender;
    }

    @Value("${spring.mail.username}")
    private String mailRemitente;

    
    @Async
    public void sendEmail(String to, String asunto, String body){
        try {
            
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to); //destinatario
            message.setSubject(asunto); //asunto
            message.setText(body);//cuerpo

            message.setFrom(mailRemitente);

            eMailSender.send(message);
            

            return;
        } catch (Exception e) {
            return;
        }
    }

    @Async
    public void sendHtmlEmail(String to, String asunto, String htmlFileTemplate, Map<String, String> variables) throws MessagingException
    {
        MimeMessage message = eMailSender.createMimeMessage();
        message.setFrom(new InternetAddress(mailRemitente));
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(asunto);

        String htmlTemplate = readFile(htmlFileTemplate);

        for(Map.Entry<String, String> entry: variables.entrySet())
        {
            htmlTemplate = htmlTemplate.replace("${"+ entry.getKey() + "}", entry.getValue());
        }

        /* String htmlContent = htmlTemplate.replace("${username}", usuarioEntity.getPersona().getApellidos()+", "+usuarioEntity.getPersona().getNombre());
        htmlContent = htmlContent.replace("${enlaceConfirmacion}", "http://localhost:8080/registrar/verificar-email?token="+usuarioEntity.getEmailToken()); */

        message.setContent(htmlTemplate, "text/html; charset=utf-8");

        eMailSender.send(message);
    }
    
    public String readFile(String fileClassPath) {
        try {
            ClassPathResource resource = new ClassPathResource(fileClassPath);
            InputStream inputStream = resource.getInputStream();
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return "fallo";
        }
    }
}
