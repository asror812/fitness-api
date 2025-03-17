package com.example.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class WorkingMonth {

    @Id
    private Integer month;

    private Double totalHours;

    @ManyToOne
    @JoinColumn(name = "year", nullable = false)
    private WorkingYear year;
}
