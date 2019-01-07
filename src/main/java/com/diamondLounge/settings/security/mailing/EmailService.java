package com.diamondLounge.settings.security.mailing;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendEmail(SimpleMailMessage email);

    String getServerEmail();
}
