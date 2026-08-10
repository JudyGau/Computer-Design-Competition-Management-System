package cqu.jsjds.service.impl;

import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Teacher;
import cqu.jsjds.entity.Manager;
import cqu.jsjds.mapper.*;
import cqu.jsjds.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    StudentMapper studentMapper;

    @Autowired
    TeacherMapper teacherMapper;

    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    ReviewerMapper reviewerMapper;

    @Autowired
    JudgeMapper judgeMapper;

    @Override
    public boolean studentLogin(Student student) {
        try {
            Student tempStudent = studentMapper.findStudent(student);
            System.out.println(tempStudent);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;

        }
    }

    @Override
    public boolean teacherLogin(Teacher teacher) {
        try {
            Teacher tempTeacher = teacherMapper.findTeacher(teacher);
            System.out.println(tempTeacher);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public int managerLogin(Manager manager) {
        try {
            Manager tempManager = managerMapper.findManagerByIdAndPassword(manager.getManagerId(), manager.getPassword());
            System.out.println("tempManager为" + tempManager);
            manager.setSchool(managerMapper.findSchoolByManagerId(manager.getManagerId()));
            System.out.println(manager);
            return tempManager.getAdministratorType();
        } catch (EmptyResultDataAccessException e) {
            System.err.println(e);
            return 0;
        } catch (Exception e) {
            System.err.println(e);
            return -1;
        }
    }


    @Override
    public int reviewerLogin(String reviewerId, String password) {
        try {
            if (!reviewerMapper.findReviewerByIdAndPassword(reviewerId, password).isEmpty()) {
                return 1;
            } else {
                return 2;
            }
        } catch (Exception e) {
            return 3;
        }
    }

    @Override
    public int judgeLogin(String judgeId, String password) {
        try {
            if (!judgeMapper.findJudgeByIdAndPassword(judgeId, password).isEmpty()) {
                return 1;
            } else {
                return 2;
            }
        } catch (Exception e) {
            return 3;
        }
    }


}
