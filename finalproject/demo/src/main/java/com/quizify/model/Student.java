package com.quizify.model;

import java.io.Serializable;

public class Student implements Serializable {

    private String name;
    private String code;

    public Student(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}