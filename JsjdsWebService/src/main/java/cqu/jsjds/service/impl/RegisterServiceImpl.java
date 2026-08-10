package cqu.jsjds.service.impl;

import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Teacher;
import cqu.jsjds.mapper.StudentMapper;
import cqu.jsjds.mapper.TeacherMapper;
import cqu.jsjds.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    StudentMapper studentMapper;
    @Autowired
    TeacherMapper teacherMapper;

    @Override
    public boolean addStudent(Student student) {
        try {
            studentMapper.insertStudent(student);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    @Override
    public boolean addTeacher(Teacher teacher) {
        try {
            teacherMapper.insertTeacher(teacher);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }


}
