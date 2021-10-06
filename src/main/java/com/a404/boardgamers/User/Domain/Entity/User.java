package com.a404.boardgamers.User.Domain.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
@ToString
@DynamicUpdate
@DynamicInsert
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "login_id", nullable = false)
    private String loginId;

    @Builder
    public User(String loginId, String nickname, String password) {
        this.loginId = loginId;
        this.nickname = nickname;
        this.password = password;
    }

    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "gender", nullable = true)
    private Boolean gender;
    @Column(name = "age", nullable = true)
    private Integer age;
    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;
    @Column(name = "is_withdraw", nullable = false)
    private boolean isWithdraw;

    public void updateInfo(String nickname, Integer age, Boolean gender) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
    }

    public void changePassword(String password) {
        this.password = password;
    }
}