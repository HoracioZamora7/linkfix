package com.linkfix.service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

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


    //metodo html mail aca
}
