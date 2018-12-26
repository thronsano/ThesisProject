package com.diamondLounge.entity;

import javax.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column
    private String authority;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    public Authority(){}

    public Authority(User user){
        this.user = user;
        this.authority = "ROLE_USER";
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
