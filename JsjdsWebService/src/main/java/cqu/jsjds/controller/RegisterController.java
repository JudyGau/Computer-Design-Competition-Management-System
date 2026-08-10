package cqu.jsjds.controller;

import cqu.jsjds.entity.Result;
import cqu.jsjds.entity.ResultCodeEnum;
import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Teacher;
import cqu.jsjds.service.RegisterService;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @PostMapping("/studentRegister")
    public Result studentRegister(@RequestBody Student student) {
        System.out.println(student);

        if (registerService.addStudent(student)) {
            return Result.build(null, ResultCodeEnum.SUCCESS);
        } else {
            return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
        }
    }

    @PostMapping("/teacherRegister")
    public Result teacherRegister(@RequestBody Teacher teacher) {
        teacher.setIdentity(0);
        System.out.println(teacher);
        if (registerService.addTeacher(teacher)) {
            return Result.build(null, ResultCodeEnum.SUCCESS);
        } else {
            return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
        }

    }

}
