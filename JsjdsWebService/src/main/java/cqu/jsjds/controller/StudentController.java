package cqu.jsjds.controller;

import cqu.jsjds.entity.*;
import cqu.jsjds.service.StudentService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static cqu.jsjds.common.Constants.UPLOAD_DIR;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/student")
@RestController
public class StudentController {
    @Autowired
    StudentService studentService;

    @GetMapping("/getStudentInfo")
    public Result<Student> getStudentInfo(HttpSession session) {
        try {
            System.out.println(session);
            Student tempstudent = (Student) session.getAttribute("student");
            Student student = studentService.getStudentInfo(tempstudent.getSchool(), tempstudent.getStudentId());
            student.setPassword("");
            student.setRePassword("");
            System.out.println("成功获取学生信息" + student);
            return Result.build(student, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @PostMapping("/saveStudentInfo")
    public Result saveStudentInfo(@RequestBody Student student, HttpSession session) {

        System.out.println("新的学生信息为" + student);
        if (studentService.updateStudentInfo(student)) {

            System.out.println("修改学生信息成功");
            return Result.build(null, ResultCodeEnum.SUCCESS);

        } else {

            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }


    @GetMapping("/getGroupOfSchool")
    public Result<List<Group>> getGroupOfSchool(HttpSession session) {
        Student tempStudent = (Student) session.getAttribute("student");
        String school = tempStudent.getSchool();
        try {
            List<Group> groupList = studentService.getGroupOfSchool(school);
            System.out.println(school + "的队伍有" + groupList);
            return Result.build(groupList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);

        }


    }

    @GetMapping("/joinGroup")
    public Result joinGroup(@RequestParam("groupName") String groupName, HttpSession session) {
        Student tempStudent = (Student) session.getAttribute("student");
        System.out.println("学生" + tempStudent + "要加入队伍" + groupName);
        switch (studentService.joinGroup(groupName, tempStudent.getSchool(), tempStudent.getStudentId())) {
            case 1 -> {
                System.out.println("加入队伍成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("队伍人数已满");
                return Result.build(null, ResultCodeEnum.GROUP_NUMBER_LIMIT);
            }
            case 3 -> {
                System.out.println("学生" + tempStudent + "已经在申请队伍");
                return Result.build(null, ResultCodeEnum.IS_APPLYING_GROUP);
            }
            case 4 -> {
                System.out.println("学生" + tempStudent + "已经加入队伍");
                return Result.build(null, ResultCodeEnum.HAVE_JOINED_GROUP);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getGroupInfo")
    public Result<Group> getGroupInfo(HttpSession session) {
        Student tempStudent = (Student) session.getAttribute("student");
        String groupName = studentService.getStudentInfo(tempStudent.getSchool(), tempStudent.getStudentId()).getGroupName();
        try {
            Group tempGroup = studentService.getGroupInfo(groupName);
            if (tempGroup != null) {
                System.out.println("队伍信息为" + tempGroup);
                return Result.build(tempGroup, ResultCodeEnum.SUCCESS);
            } else {
                System.out.println("学生" + tempStudent + "尚未加入任何队伍");
                return Result.build(null, ResultCodeEnum.JOIN_NONE_GROUP);
            }
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/getStudentOfGroup")
    public Result<List<Student>> getStudentOfGroup(HttpSession session) {
        Student tempStudent = (Student) session.getAttribute("student");
        String groupName = studentService.getStudentInfo(tempStudent.getSchool(), tempStudent.getStudentId()).getGroupName();
        try {
            List<Student> studentList = studentService.getStudentOfGroup(groupName);
            System.out.println(groupName + "的成员有" + studentList);
            return Result.build(studentList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }


    @GetMapping("/getWorkOfGroup")
    public Result<List<Work>> getWorkOfGroup(HttpSession session) {
        Student tempStudent = (Student) session.getAttribute("student");
        String groupName = studentService.getStudentInfo(tempStudent.getSchool(), tempStudent.getStudentId()).getGroupName();
        try {
            List<Work> workList = studentService.getWorkOfGroup(groupName);
            System.out.println(groupName + "的作品有" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    // 处理文件上传请求
    @PostMapping("/uploadWork")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {
        // 获取上传的文件名
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            // 定义文件保存路径
            File destination = new File(UPLOAD_DIR + fileName);
            System.out.println("File path: " + destination.getAbsolutePath());
            try {
                // 将文件保存到指定路径
                file.transferTo(destination);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.build(null, ResultCodeEnum.FILE_UPLOAD_FAILED);
            }
        }
        return Result.build(null, ResultCodeEnum.NO_FILE_UPLOADED);
    }

    @GetMapping("/downloadWork")
    public ResponseEntity<FileSystemResource> downloadWork(@RequestParam String workName) {
        String fileName = workName + ".zip";
        File file = new File(UPLOAD_DIR + fileName);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }
        // 对文件名进行UTF-8编码
        try {
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20"); // 替换空格为%20
            System.out.println("文件" + fileName + "下载成功");
            // 使用 filename* 语法指定编码
            return ResponseEntity.ok()
                    .header("Content-Type", "application/zip") // 设置文件类型为 zip
                    .header("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(new FileSystemResource(file));
        } catch (UnsupportedEncodingException e) {
            // 处理异常
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/submitWork")
    public Result submitWOrk(@RequestParam String workName) {
        switch (studentService.submitWork(workName)) {
            case 1 -> {
                System.out.println("成功提交作品" + workName + "的文件");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("提交作品" + workName + "的文件失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/queryWork")
    public Result<Work> queryWork(@RequestParam("workName") String workName) {
        try {
            Work tempWork = studentService.queryWork(workName);
            if (tempWork == null) {
                System.out.println("作品" + workName + "不存在");
                return Result.build(null, ResultCodeEnum.WORK_NOT_EXISTS);
            } else {
                System.out.println("查询作品成功");
                return Result.build(tempWork, ResultCodeEnum.SUCCESS);
            }
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }


    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Password password, HttpSession session) {
        Student tempStudent = (Student) session.getAttribute("student");
        switch (studentService.updatePassword(password, tempStudent.getSchool(), tempStudent.getStudentId())) {
            case 1 -> {
                System.out.println("原密码错误");
                return Result.build(null, ResultCodeEnum.OLD_PASSWORD_ERROR);
            }
            case 2 -> {
                System.out.println("两次输入的密码不一致");
                return Result.build(null, ResultCodeEnum.RE_PASSWORD_NOT_EQUAL_PASSWORD);
            }
            case 3 -> {
                System.out.println("原密码" + password.getOldPassword() + "成功修改为" + password.getNewPassword());
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("其他错误");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

}
