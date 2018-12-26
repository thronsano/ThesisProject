package com.diamondLounge.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "resetToken")
public class ResetToken {

    @Id
    @NotBlank
    @Size(max = 100)
    @Email
    @Column
    private String email;

    @Column
    private String resetToken;

    @Column
    private LocalDateTime date;

    public ResetToken() {
    }

    public ResetToken(@NotBlank @Size(max = 100) @Email String email, String resetToken) {
        this.email = email;
        this.date = LocalDateTime.now();
        this.resetToken = resetToken;
    }

    public String getEmail() {
        return email;
    }

    public String getResetToken() {
        return resetToken;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
