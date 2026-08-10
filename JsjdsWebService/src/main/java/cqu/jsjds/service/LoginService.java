package cqu.jsjds.service;

import cqu.jsjds.entity.Manager;
import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Teacher;

public interface LoginService {
    boolean studentLogin(Student student);

    boolean teacherLogin(Teacher teacher);

    int managerLogin(Manager manager);

    int reviewerLogin(String reviewerId, String password);

    int judgeLogin(String judgeId, String password);
}
