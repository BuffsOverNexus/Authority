package com.buffsovernexus.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "authority_player_killing")
@Data
@Builder
public class AuthorityPlayerKill {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="killer_id")
    private AuthorityPlayer killer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="victim_id")
    private AuthorityPlayer victim;

    private String message;

    private Date occurred;
}
