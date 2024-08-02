package com.game.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Integer experience;
    private Integer level;// = (int) (Math.sqrt(2500 + experience * 200) - 50)/100;
    private Integer untilNextLevel;// = 50 * (level + 1) * (level + 2) - experience;
    private Date birthday;
    private Boolean banned;

}
