package com.sparta.spartaeats.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.UUID;

@Entity
@Getter
@Table(name = "p_location")
public class Location extends TimeStamped{
    @Id
    @GeneratedValue
    @Column(name = "location_id")
    private UUID id;
    private String locationName;
    private Character useYn;
    private Character delYn;

    public Location (UUID id){
        this.id = id;
    }
}
