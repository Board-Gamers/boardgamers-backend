package com.a404.boardgamers.User.Domain.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user")
@ToString
@DynamicUpdate
@DynamicInsert
public class User {
    @Builder
    public User(String id, String nickname, String password) {
        this.id = id;
        this.nickname = nickname;
        this.password = password;
    }

    @Id
    @Column(name = "id", nullable = false)
    private String id;
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
}