package com.a404.boardgamers.User.Domain.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
    @Id
    private String id;
    private String nickname;
    private String password;

}