package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "training_types")
@NoArgsConstructor
@AllArgsConstructor
public class TrainingType extends BaseEntity {

    @Column(name = "training_type_name", nullable = false)
    private String trainingTypeName;

    @OneToMany(mappedBy = "specialization")
    private List<Trainer> trainers = new ArrayList<>();

    @OneToMany(mappedBy = "trainingType", cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, orphanRemoval = true)
    private List<Training> trainings = new ArrayList<>();

    @Override
    public String toString() {
        return "TrainingType [trainingTypeName=" + trainingTypeName + "]";
    }

}
