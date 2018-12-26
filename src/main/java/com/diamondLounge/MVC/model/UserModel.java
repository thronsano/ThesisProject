package com.diamondLounge.MVC.model;

import com.diamondLounge.entity.db.Authority;
import com.diamondLounge.entity.db.ResetToken;
import com.diamondLounge.entity.db.User;
import com.diamondLounge.exceptions.DiamondLoungeException;
import com.diamondLounge.exceptions.UsernamePasswordException;
import com.diamondLounge.security.mailing.EmailService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.InputMismatchException;
import java.util.UUID;

import static com.diamondLounge.utility.Logger.logError;

@Repository
public class UserModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private ResetTokenModel resetTokenModel;

    @Autowired
    private EmailService emailService;

    public User getUserByEmail(String email) {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            return session.get(User.class, email);
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private void saveUsersPassword(User user) throws UsernamePasswordException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            User userToUpdate = session.get(User.class, user.getEmail());
            userToUpdate.setPassword(user.getPassword());
        } catch (Exception e) {
            logError("Exception during saving user into database");
            throw new UsernamePasswordException(e.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    private void saveNewUser(User user, Authority authority) throws UsernamePasswordException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();

            session.beginTransaction();
            session.save(authority);
        } catch (Exception e) {
            logError("Exception during adding new user into database");
            logError(e.getMessage());
            throw new UsernamePasswordException(e.getMessage());
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void createUser(String email, String password, String confirmPassword, String username, PasswordEncoder passwordEncoder) throws UsernamePasswordException {
        if (getUserByEmail(email) != null)
            throw new UsernamePasswordException("Username already taken!");

        if (validateString(email) && validateString(password) && validateString(username)) {
            if (password.equals(confirmPassword)) {
                String hashedPassword = passwordEncoder.encode(password);
                User user = new User(email, hashedPassword, username);
                Authority authority = new Authority(user);

                saveNewUser(user, authority);
            } else {
                throw new UsernamePasswordException("Passwords do not match!");
            }
        } else {
            throw new UsernamePasswordException("Passwords do not match!");
        }
    }

    public void resetPasswordFromToken(String token, String password, String confirmPassword, PasswordEncoder passwordEncoder) throws UsernamePasswordException {
        User user;
        ResetToken resetToken;

        if (validateString(token)) {
            resetToken = resetTokenModel.getByResetToken(token);
            user = getUserByEmail(resetToken.getEmail());

            if (user != null) {
                if (password.equals(confirmPassword)) {
                    user.setPassword(passwordEncoder.encode(password));
                    resetTokenModel.deleteResetToken(user.getEmail());
                    saveUsersPassword(user);
                }
            } else {
                throw new UsernamePasswordException("User doesn't exist!");
            }
        } else {
            throw new UsernamePasswordException("Token was lost!");
        }
    }

    public void sendResetToken(String userEmail, HttpServletRequest request) throws DiamondLoungeException {
        User user = getUserByEmail(userEmail);

        if (user == null) {
            throw new UsernamePasswordException("User doesn't exist!");
        } else {
            ResetToken resetToken = new ResetToken(userEmail, UUID.randomUUID().toString());
            resetTokenModel.addResetToken(resetToken);
            String appUrl = request.getScheme() + "://" + request.getServerName();

            // Email message
            SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
            passwordResetEmail.setFrom(emailService.getServerEmail());
            passwordResetEmail.setTo(user.getEmail());
            passwordResetEmail.setSubject("Password Reset Request");
            passwordResetEmail.setText("To reset your password, click the link below:\n" + appUrl + ":8080"
                    + "/reset?token=" + resetToken.getResetToken());

            emailService.sendEmail(passwordResetEmail);
        }
    }

    private boolean validateString(String text) {
        return text != null && !text.isEmpty();
    }

    public void editUser(String username) throws DiamondLoungeException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User(email, "", username);
        editUserInDatabase(user);
    }

    private void editUserInDatabase(User user) throws DiamondLoungeException {
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            User updateUser = session.get(User.class, user.getEmail());

            updateUser.setEmail(user.getEmail());
            updateUser.setUsername(user.getUsername());
        } catch (Exception e) {
            logError("Exception during saving user into database");
            throw new DiamondLoungeException("Exception during saving user into database");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void changePassword(String currentPassword, String newPassword, String confirmNewPassword, PasswordEncoder passwordEncoder) throws UsernamePasswordException {
        User user;
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (checkPassword(email, currentPassword, passwordEncoder)) {
            if (newPassword.equals(confirmNewPassword)) {
                newPassword = passwordEncoder.encode(newPassword);
                user = new User(email, newPassword, "");
                saveUsersPassword(user);
            } else {
                throw new InputMismatchException("Passwords do not match!");
            }
        } else {
            throw new UsernamePasswordException("Wrong password!");
        }
    }

    private boolean checkPassword(String email, String password, PasswordEncoder passwordEncoder) {
        User user = getUserByEmail(email);
        return passwordEncoder.matches(password, user.getPassword());
    }
}
