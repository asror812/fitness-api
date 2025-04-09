package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkingYear {

    private Integer year;

    private List<WorkingMonth> monthlyWorkload = new ArrayList<>();

}