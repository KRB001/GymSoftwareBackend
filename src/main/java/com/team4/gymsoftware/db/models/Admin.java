package com.team4.gymsoftware.db.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Admin {

    @Id @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;
    private String name;

}
