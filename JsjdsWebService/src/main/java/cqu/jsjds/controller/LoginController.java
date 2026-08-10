package cqu.jsjds.controller;

import cqu.jsjds.entity.*;
import cqu.jsjds.service.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private ReviewerService reviewerService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private TeacherService teacherService;

    @Autowired
    private JudgeService judgeService;

    @PostMapping("/studentLogin")
    public Result studentLogin(@RequestBody Student student, HttpSession session) {
        System.out.println(student);

        if (loginService.studentLogin(student)) {
            session.setAttribute("student", student);
            System.out.println("学生" + student.getStudentId() + "登录成功");

            return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
        } else {
            return Result.build(null, ResultCodeEnum.LOGIN_ERROR);  // 密码错误
        }
    }

    @PostMapping("/teacherLogin")
    public Result teacherLogin(@RequestBody Teacher teacher, HttpSession session) {
        System.out.println(teacher);
        // 模拟数据库验证：这里应该根据学校、学号和密码查询数据库
        if (loginService.teacherLogin(teacher)) {
            teacher = teacherService.getTeacherInfo(teacher.getSchool(), teacher.getTeacherId());
            session.setAttribute("teacher", teacher);
            System.out.println("教师" + teacher.getTeacherId() + "登录成功");

            return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功

        } else {

            return Result.build(null, ResultCodeEnum.LOGIN_ERROR);  // 密码错误
        }
    }

    @PostMapping("/managerLogin")
    public Result<String> managerLogin(@RequestBody Manager manager, HttpSession session) {
        System.out.println(manager + "登录");
        // 模拟数据库验证：这里应该根据学校、学号和密码查询数据库
        switch (loginService.managerLogin(manager)) {
            case 0 -> {
                System.out.println("账号或密码错误");
                return Result.build(null, ResultCodeEnum.LOGIN_ERROR);
            }
            case 1 -> {
                manager = managerService.getManagerInfo(manager.getManagerId());
                session.setAttribute("schoolManager", manager);
                System.out.println("校管理员登录成功");
                return Result.build("school-manager", ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                manager = managerService.getManagerInfo(manager.getManagerId());
                session.setAttribute("manager", manager);
                System.out.println("管理员登录成功");
                return Result.build("manager", ResultCodeEnum.SUCCESS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @PostMapping("/reviewerLogin")
    public Result reviewerLogin(@RequestBody Reviewer reviewer, HttpSession session) {
        System.out.println(reviewer);
        switch (loginService.reviewerLogin(reviewer.getReviewerId(), reviewer.getPassword())) {
            case 1 -> {
                reviewer = reviewerService.getReviewerInfo(reviewer.getReviewerId());
                session.setAttribute("reviewer", reviewer);
                System.out.println(session);
                System.out.println("审核员" + reviewer.getReviewerId() + "登录成功");
                return Result.build(null, ResultCodeEnum.SUCCESS); //操作成功
            }
            case 2 -> {
                System.out.println("账号或密码错误");
                return Result.build(null, ResultCodeEnum.LOGIN_ERROR); //密码错误
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @PostMapping("/judgeLogin")
    public Result judgeLogin(@RequestBody Judge judge, HttpSession session) {
        System.out.println(judge);
        switch (loginService.judgeLogin(judge.getJudgeId(), judge.getPassword())) {
            case 1 -> {
                judge = judgeService.getJudgeInfo(judge.getJudgeId());
                session.setAttribute("judge", judge);
                System.out.println("评委" + judge.getJudgeId() + "登录成功");
                return Result.build(null, ResultCodeEnum.SUCCESS); //操作成功
            }
            case 2 -> {
                System.out.println("账号或密码错误");
                return Result.build(null, ResultCodeEnum.LOGIN_ERROR); //密码错误
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/studentLogout")
    public Result studentLogout(HttpSession session) {
        session.removeAttribute("student");
        System.out.println("学生" + "退出登录");
        return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
    }

    @GetMapping("/teacherLogout")
    public Result teacherLogout(HttpSession session) {
        session.removeAttribute("teacher");
        System.out.println("教师" + "退出登录");
        return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
    }

    @GetMapping("/managerLogout")
    public Result managerLogout(HttpSession session) {
        System.out.println(session);
        session.removeAttribute("manager");
        System.out.println("省赛管理员" + "退出登录");
        return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
    }

    @GetMapping("/schoolManagerLogout")
    public Result schoolManagerLogout(HttpSession session) {
        System.out.println(session);
        session.removeAttribute("schoolManager");
        System.out.println("校赛管理员" + "退出登录");
        return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
    }


    @GetMapping("/reviewerLogout")
    public Result reviewerLogout(HttpSession session) {
        session.removeAttribute("reviewer");
        System.out.println("评审" + "退出登录");
        return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
    }

    @GetMapping("/judgeLogout")
    public Result judgeLogout(HttpSession session) {
        session.removeAttribute("judge");
        System.out.println("评委" + "退出登录");
        return Result.build(null, ResultCodeEnum.SUCCESS); // 操作成功
    }


}
