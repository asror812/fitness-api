package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TrainerWorkload {

    @Id
    private String username;

    @Column(name = "trainer_firstname")
    private String firstName;

    @Column(name = "trainer_lastname")
    private String lastName;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "workload", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingYear> years;
}