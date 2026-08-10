package cqu.jsjds.service;

import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Teacher;

public interface RegisterService {

    boolean addStudent(Student student);

    boolean addTeacher(Teacher teacher);
}
