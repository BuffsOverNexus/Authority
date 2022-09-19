package com.buffsovernexus.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authority_home")
@Data
public class AuthorityHome {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    private String name;
    @CreationTimestamp
    private Date createdOn;
    private String world;
    private Double x, y, z;
    private Float yaw, pitch;

    public World getWorld() {
        return Bukkit.getWorld(UUID.fromString(world));
    }

    public Location toLocation() {
        return new Location(getWorld(), x, y, z, yaw, pitch);
    }

    public void fromLocation(String name, Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = Objects.requireNonNull(location.getWorld()).getUID().toString();
        this.name = name;
    }
}
