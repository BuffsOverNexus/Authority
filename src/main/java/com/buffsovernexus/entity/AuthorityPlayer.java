package com.buffsovernexus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

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

    private Date joined = new Date();

    private Date lastSeen = new Date();

}
