package com.example.workload_service.model;

import java.util.ArrayList;
import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkingYear {

    private Integer year;

    private List<WorkingMonth> monthlyWorkload = new ArrayList<>();

}