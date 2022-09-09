package com.buffsovernexus.entity;

import com.buffsovernexus.entity.enumerator.HomeType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name="authority_home")
@Data
public class AuthorityHome {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    @Column
    private String name;

    private HomeType homeType = HomeType.CUSTOM;
}
