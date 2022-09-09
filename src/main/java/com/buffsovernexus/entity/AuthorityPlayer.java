package com.buffsovernexus.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="authority_player")
@Data
public class AuthorityPlayer {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    private UUID uuid;

    private String name;

    private Date joined = new Date();

    private Date lastSeen;

    @OneToMany(cascade= CascadeType.ALL)
    @JoinColumn(name="home_id")
    private List<AuthorityHome> homes;
}
