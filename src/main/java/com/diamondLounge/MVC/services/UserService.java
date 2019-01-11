package com.diamondLounge.MVC.services;

import com.diamondLounge.entity.db.Authority;
import com.diamondLounge.entity.db.ResetToken;
import com.diamondLounge.entity.db.User;
import com.diamondLounge.entity.exceptions.DiamondLoungeException;
import com.diamondLounge.entity.exceptions.UsernamePasswordException;
import com.diamondLounge.settings.security.mailing.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Repository
public class UserService extends PersistenceService<User> {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private ResetTokenService resetTokenService;

    @Autowired
    private EmailService emailService;

    public User getUserByEmail(String email) {
        return getObjectById("User", email);
    }

    public void createUser(String email, String password, String confirmPassword, String username) throws UsernamePasswordException {
        if (getAllObjects("User").size() != 0) {
            throw new UsernamePasswordException("<p>Master account has already been created!</p>" +
                                                "<p>Please use 'forgot password' option if recovery is required.</p>");
        }
        if (validateString(email) && validateString(password) && validateString(username)) {
            if (password.equals(confirmPassword)) {
                String hashedPassword = passwordEncoder.encode(password);
                User user = new User(email, hashedPassword, username);
                Authority authority = new Authority(user);

                user.getAuthorities().add(authority);

                persistObject(user);
            } else {
                throw new UsernamePasswordException("Passwords do not match!");
            }
        } else {
            throw new UsernamePasswordException("Password doesn't meet the criteria!");
        }
    }

    public void resetPasswordWithToken(String token, String password, String confirmPassword) throws UsernamePasswordException {
        if (validateString(token)) {
            ResetToken resetToken = resetTokenService.getByResetToken(token);
            User user = getUserByEmail(resetToken.getEmail());

            if (user != null) {
                if (password.equals(confirmPassword)) {
                    user.setPassword(passwordEncoder.encode(password));
                    resetTokenService.deletePreviousResetToken(user.getEmail());
                    persistObject(user);
                }
            } else {
                throw new UsernamePasswordException("User doesn't exist!");
            }
        } else {
            throw new UsernamePasswordException("Token was lost!");
        }
    }

    public void sendResetToken(String email, HttpServletRequest request) throws DiamondLoungeException {
        User user = getUserByEmail(email);

        if (user == null) {
            throw new UsernamePasswordException("User doesn't exist!");
        } else {
            ResetToken resetToken = new ResetToken(email, UUID.randomUUID().toString());
            resetTokenService.addResetToken(resetToken);

            String appUrl = request.getScheme() + "://" + request.getServerName();
            String emailMessage = "To reset your password, click the link below:\n" + appUrl + ":8080" + "/reset?token=" + resetToken.getResetToken();

            emailService.sendEmail(email, emailMessage);
        }
    }

    public void changeUsername(String newUsername) throws DiamondLoungeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = getUserByEmail(email);
        user.setUsername(newUsername);
        persistObject(user);
    }

    public void changePassword(String currentPassword, String newPassword, String confirmNewPassword, PasswordEncoder passwordEncoder) throws DiamondLoungeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (passwordMatchesDatabase(email, currentPassword, passwordEncoder)) {
            if (newPassword.equals(confirmNewPassword)) {
                String hashedPassword = passwordEncoder.encode(newPassword);
                User user = getUserByEmail(email);

                user.setPassword(hashedPassword);

                persistObject(user);
            } else {
                throw new DiamondLoungeException("Passwords do not match!");
            }
        } else {
            throw new DiamondLoungeException("Incorrect password");
        }
    }

    private boolean passwordMatchesDatabase(String email, String password, PasswordEncoder passwordEncoder) {
        User user = getUserByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }
}
