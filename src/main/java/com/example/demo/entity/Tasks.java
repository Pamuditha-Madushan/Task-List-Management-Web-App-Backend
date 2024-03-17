package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Tasks {

    @Id
    private Long id;
    private String title;
    private String description;
    private String status;
    private Long timestamp;

}
