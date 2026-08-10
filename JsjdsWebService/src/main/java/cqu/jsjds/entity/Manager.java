package cqu.jsjds.entity;

import lombok.Data;

@Data
public class Manager {
    String managerId;
    String password;
    String school;
    int administratorType;
    int quota;
}
