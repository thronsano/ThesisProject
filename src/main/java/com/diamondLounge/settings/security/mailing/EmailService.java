package com.diamondLounge.settings.security.mailing;

public interface EmailService {
    void sendEmail(String to, String body);

    String getServerEmail();
}
