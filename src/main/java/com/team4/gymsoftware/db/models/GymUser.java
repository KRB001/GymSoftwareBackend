package com.team4.gymsoftware.db.models;

import jakarta.persistence.*;

@Entity
public class GymUser {

    @Id @GeneratedValue(
            strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(
            name = "trainer_id",
            referencedColumnName = "id"
    )
    private Trainer trainer;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
