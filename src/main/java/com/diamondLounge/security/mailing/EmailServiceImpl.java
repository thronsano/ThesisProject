package com.diamondLounge.security.mailing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Properties;

@Service("emailService")
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements EmailService {

    @Autowired
    Environment env;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(env.getProperty("email.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("email.port"))));

        mailSender.setUsername(env.getProperty("email.username"));
        mailSender.setPassword(env.getProperty("email.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", Objects.requireNonNull(env.getProperty("mail.transport.protocol")));
        props.put("mail.smtp.auth", Objects.requireNonNull(env.getProperty("mail.smtp.auth")));
        props.put("mail.smtp.starttls.enable", Objects.requireNonNull(env.getProperty("mail.smtp.starttls.enable")));
        props.put("mail.debug", Objects.requireNonNull(env.getProperty("mail.debug")));

        return mailSender;
    }

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }

    public String getServerEmail() {
        return env.getProperty("email.host");
    }
}
