package com.a404.boardgamers.Game.Domain.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "category")
@ToString
@DynamicUpdate
@DynamicInsert
public class Category {
    String name;
    @Id
    private int id;
}
