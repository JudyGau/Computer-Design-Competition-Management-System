package cqu.jsjds.entity;

import lombok.Data;

@Data
public class Teacher {
    String name;
    String teacherId;
    String school;
    String password;
    String rePassword;
    int identity;
    String telephone;
    String email;
    String title;
}
