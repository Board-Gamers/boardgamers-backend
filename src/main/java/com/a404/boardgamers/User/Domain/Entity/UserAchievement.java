package com.a404.boardgamers.User.Domain.Entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "user_achievement")
@ToString
@DynamicUpdate
@DynamicInsert
public class UserAchievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    private int achievementId;

    private Timestamp achievedAt;

    private int detail;

    public void update(int count) {
        this.detail = count;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        this.achievedAt = timestamp;
    }
}
