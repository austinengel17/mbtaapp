package com.mbtaapp.service;

import com.mbtaapp.model.EmailRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Properties;
@Service
public class ContactService {
    private final String EMAIL_ADDRESS = System.getenv("GMAIL_USERNAME");
    private final String PASSWORD = System.getenv("GMAIL_PASSWORD");
    private final String MAIL_MESSAGE_TEMPLATE = "From %s: \n %s";
    public Mono<ResponseEntity<Object>> sendEmail(EmailRequest request){
        return Mono.fromCallable(()-> {
            try {
                JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
                mailSender.setHost("smtp.gmail.com");
                mailSender.setPort(587);

                mailSender.setUsername(EMAIL_ADDRESS);
                mailSender.setPassword(PASSWORD);

                Properties props = mailSender.getJavaMailProperties();
                props.put("mail.transport.protocol", "smtp");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.debug", "true");

                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom(request.getEmailAddress());
                message.setTo(EMAIL_ADDRESS);
                message.setSubject(request.getSubject());
                message.setText(String.format(MAIL_MESSAGE_TEMPLATE, request.getEmailAddress(),request.getMessage()));
                mailSender.send(message);
                return ResponseEntity.status(HttpStatus.OK).build();
            } catch (Exception e) {
                System.out.println(e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Oops, something went wrong internally. Try again later or send a message on LinkedIn!");
            }
        });
    }
}
