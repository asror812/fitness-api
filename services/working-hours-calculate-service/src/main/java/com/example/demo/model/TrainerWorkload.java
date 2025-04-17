package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter@ToString
@Document(collection = "trainer_workloads")
public class TrainerWorkload {
    @Id
    private String username;

    @Indexed
    private String firstName;

    @Indexed
    private String lastName;

    private List<WorkingYear> years = new ArrayList<>();
}