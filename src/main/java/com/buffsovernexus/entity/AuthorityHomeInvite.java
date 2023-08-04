package com.buffsovernexus.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Entity
@Table(name = "authority_home_invite")
@Data
public class AuthorityHomeInvite {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    @CreationTimestamp
    private Date createdOn;

    private Date respondedOn;

    private HomeInviteResult result;

    @OneToOne
    @JoinColumn(name = "home_id")
    private AuthorityHome home;

}
