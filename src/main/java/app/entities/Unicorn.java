package app.entities;

import jakarta.persistence.*;

@Entity
public class Unicorn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unicorn_id", nullable = false)
    private int id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "age")
    private int age;

    @Column(name = "power_strength")
    private int powerStrength;

    public Unicorn(int id, String name, int age, int powerStrength) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.powerStrength = powerStrength;
    }

    public Unicorn() {

    }

    // Getters and setters
}

