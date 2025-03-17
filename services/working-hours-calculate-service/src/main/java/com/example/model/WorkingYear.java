package com.example.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "year")
public class WorkingYear {

    @Id
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private TrainerWorkload workload;

    @OneToMany(mappedBy = "year", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkingMonth> months;
}