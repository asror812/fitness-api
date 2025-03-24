package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainerWorkload {

    private String username;

    private String firstName;

    private String lastName;

    private boolean active;

    private List<WorkingYear> years = new ArrayList<>();
}