package com.buffsovernexus.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "authority_player_damage")
@Data
public class AuthorityPlayerDamage {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attacker_id")
    private AuthorityPlayer attacker;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "defender_id")
    private AuthorityPlayer defender;
    @CreationTimestamp
    private Date occurred;

    private Double damage;

    @Override
    public String toString() {
        return String.format("%s has damaged %s by %s", attacker.getName(), defender.getName(), damage);
    }
}
