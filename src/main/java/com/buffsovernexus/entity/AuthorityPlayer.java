package com.buffsovernexus.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="authority_player")
@Data
public class AuthorityPlayer {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    private String uuid;

    private String name;
    @CreationTimestamp
    private Date joined;

    private Date lastSeen = new Date();

    @OneToMany
    @JoinColumn(name = "player_id")
    private List<AuthorityHome> homes;

}
