package com.example.SpringBatchTutorial.job.fileDataReadWrite.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Player implements Serializable {

    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;

    // setters and getters...
}
