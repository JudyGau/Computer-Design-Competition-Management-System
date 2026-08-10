package cqu.jsjds.controller;

import cqu.jsjds.entity.*;
import cqu.jsjds.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/teacher")
@RestController
public class TeacherController {
    @Autowired
    TeacherService teacherService;

    @GetMapping("/getTeacherInfo")
    public Result<Teacher> getTeacherInfo(HttpSession session) {
        try {
            Teacher tempteacher = (Teacher) session.getAttribute("teacher");
            Teacher teacher = teacherService.getTeacherInfo(tempteacher.getSchool(), tempteacher.getTeacherId());
            System.out.println("成功获取教师信息" + teacher);
            return Result.build(teacher, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @PostMapping("/saveTeacherInfo")
    public Result saveTeacherInfo(@RequestBody Teacher teacher, HttpSession session) {

        System.out.println("新的教师信息为" + teacher);
        if (teacherService.updateTeacherInfo(teacher)) {

            System.out.println("修改教师信息成功");
            return Result.build(null, ResultCodeEnum.SUCCESS);
        } else {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    //    @ApiResponse(content = @Content(mediaType = "application/json",
//            schema = @Schema(implementation = Result.class)))
//    @Operation(summary = "教师创建队伍")
//    @ApiResponse(responseCode = "200", description = "操作成功")
//    @ApiResponse(responseCode = "219", description = "队伍名称已被使用")
//    @ApiResponse(responseCode = "204", description = "其他错误")
    @PostMapping("/makeGroup")
    public Result makeGroup(@RequestBody Group group, HttpSession session) {
        System.out.println("新的队伍信息是" + group);
        Teacher tempteacher = (Teacher) session.getAttribute("teacher");

        Teacher teacher = teacherService.getTeacherInfo(tempteacher.getSchool(), tempteacher.getTeacherId());
        if (teacher.getIdentity() != 1) {
            return Result.build(null, ResultCodeEnum.NO_PERMISSION);
        }

        group.setTeacherId(tempteacher.getTeacherId());
        group.setSchool(tempteacher.getSchool());
        switch (teacherService.addGroup(group)) {
            case 1 -> {
                System.out.println("新建队伍成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("队伍名称已被使用");
                return Result.build(null, ResultCodeEnum.GROUP_NAME_IS_EXISTS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getGroupOfTeacher")
    public Result<List<Group>> getGroupOfTeacher(HttpSession session) {
        try {
            Teacher tempteacher = (Teacher) session.getAttribute("teacher");
            System.out.println("当前登录的教师为" + tempteacher);

            Teacher teacher = teacherService.getTeacherInfo(tempteacher.getSchool(), tempteacher.getTeacherId());
            if (teacher.getIdentity() == 0) {
                return Result.build(null, ResultCodeEnum.NO_PERMISSION);
            }

            List<Group> groupList = teacherService.getGroupOfTeacher(tempteacher.getSchool(), tempteacher.getTeacherId());
            System.out.println("该教师创建的队伍有" + groupList);
            return Result.build(groupList, ResultCodeEnum.SUCCESS);

        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/getStudentApplyingGroupOfTeacher")
    public Result<List<Student>> getStudentApplyingGroupOfTeacher(HttpSession session) {
        try {
            Teacher tempteacher = (Teacher) session.getAttribute("teacher");
            System.out.println("当前登录的教师为" + tempteacher);

            Teacher teacher = teacherService.getTeacherInfo(tempteacher.getSchool(), tempteacher.getTeacherId());
            if (teacher.getIdentity() == 0) {
                return Result.build(null, ResultCodeEnum.NO_PERMISSION);
            }

            List<Student> studentList = teacherService.getStudentApplyingGroupOfTeacher(tempteacher.getSchool(), tempteacher.getTeacherId());
            System.out.println("申请该教师所创队伍的学生有" + studentList);
            return Result.build(studentList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @PostMapping("/acceptApplyOfStudent")
    public Result acceptApplyOfStudent(@RequestBody Student student) {
        System.out.println("教师同意学生" + student.getName() + "加入队伍" + student.getApplyGroupName());
        switch (teacherService.acceptApplyOfStudent(student.getSchool(), student.getStudentId(), student.getApplyGroupName())) {
            case 1 -> {
                System.out.println("教师同意学生加入队伍，操作成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("教师同意学生加入队伍，操作失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @PostMapping("/refuseApplyOfStudent")
    public Result refuseApplyOfStudent(@RequestBody Student student) {
        System.out.println("教师拒绝学生" + student.getName() + "加入队伍" + student.getApplyGroupName());
        switch (teacherService.refuseApplyOfStudent(student.getSchool(), student.getStudentId(), student.getApplyGroupName())) {
            case 1 -> {
                System.out.println("教师拒绝学生加入队伍，操作成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("教师拒绝学生加入队伍，操作失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getGroupInfo")
    public Result<Group> getGroupInfo(@RequestParam("groupName") String groupId) {
        try {
            Group tempGroup = teacherService.getGroupInfo(groupId);
            System.out.println("队伍信息为" + tempGroup);
            return Result.build(tempGroup, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/deleteGroup")
    public Result deleteGroup(@RequestParam("groupName") String groupId) {
        System.out.println("删除队伍" + groupId);
        if (teacherService.deleteGroup(groupId)) {
            System.out.println("删除队伍成功");
            return Result.build(null, ResultCodeEnum.SUCCESS);
        } else {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/getStudentOfGroup")
    public Result<List<Student>> getStudentOfGroup(@RequestParam("groupName") String groupName) {
        System.out.println("查询队伍" + groupName + "的成员");
        try {
            List<Student> studentList = teacherService.getStudentOfGroup(groupName);
            System.out.println("查询学生成功" + studentList);
            return Result.build(studentList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/deleteStudentFromGroup")
    public Result deleteStudentFromGroup(@RequestParam("groupName") String groupName,
                                         @RequestParam("school") String school,
                                         @RequestParam("studentId") String studentId) {
        System.out.println("即将删除学生" + school + studentId + "从队伍" + groupName);
        switch (teacherService.deleteStudentFromGroup(school, studentId)) {
            case 1 -> {
                System.out.println("成功将学生" + school + studentId + "移出队伍" + groupName);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

//    // 处理文件上传请求
//    @PostMapping("/uploadWork")
//    public Result uploadFile(@RequestParam("file") MultipartFile file) {
//        // 获取上传的文件名
//        String fileName = file.getOriginalFilename();
//        if (fileName != null) {
//            // 定义文件保存路径
//            File destination = new File(UPLOAD_DIR + fileName);
//            System.out.println("File path: " + destination.getAbsolutePath());
//            try {
//                // 将文件保存到指定路径
//                file.transferTo(destination);
//                return Result.build(null, ResultCodeEnum.SUCCESS);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return Result.build(null, ResultCodeEnum.FILE_UPLOAD_FAILED);
//            }
//        }
//        return Result.build(null, ResultCodeEnum.NO_FILE_UPLOADED);
//    }

    @PostMapping("/makeWork")
    public Result makeGroup(@RequestBody Work work) {
        work.setIsSubmitted(0);
        work.setIsSchoolCompetition(0);
        work.setIsProvinceCompetition(0);
        work.setIsApproved(0);
        work.setScore(-1);
        work.setAward(0);
        work.setSchoolCompetitionScore(-1);
        work.setComment("");
        System.out.println("新建作品" + work);
        switch (teacherService.addWork(work)) {
            case 1 -> {
                System.out.println("新建作品成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("作品名称已被使用");
                return Result.build(null, ResultCodeEnum.WORK_NAME_IS_EXISTS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
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

    @GetMapping("/deleteWork")
    public Result deleteWOrk(@RequestParam("workName") String workName) {
        System.out.println("删除作品" + workName);
        switch (teacherService.deleteWork(workName)) {
            case 1 -> {
                System.out.println("成功删除作品" + workName);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/sendToSchoolCompetition")
    public Result sendToSchoolCompetition(@RequestParam("workName") String workName) {
        switch (teacherService.sendToSchoolCompetition(workName)) {
            case 1 -> {
                System.out.println("成功将作品" + workName + "推送到校赛");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("已经将作品" + workName + "推送到校赛，无需再次点击");
                return Result.build(null, ResultCodeEnum.NO_REPEAT_OPERATE);
            }
            default -> {
                System.out.println("推送作品" + workName + "到校赛失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getWorkList")
    public Result<List<Work>> getWorkList(@RequestParam("groupName") String groupName) {
        System.out.println("查询队伍" + groupName + "的作品");
        try {
            List<Work> workList = teacherService.getWorkList(groupName);
            System.out.println("查询作品成功" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/queryWork")
    public Result<Work> queryWork(@RequestParam("workName") String workName) {
        try {
            Work tempWork = teacherService.queryWork(workName);
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

    @GetMapping("/getAssignedWork")
    public Result<List<Work>> getAssignedWork(HttpSession session) {
        Teacher tempTeacher = (Teacher) session.getAttribute("teacher");
        System.out.println(tempTeacher);
        if (tempTeacher.getIdentity() != 2) {
            return Result.build(null, ResultCodeEnum.NO_PERMISSION);
        }
        try {
            List<Work> workList = teacherService.getAssignedWork(tempTeacher.getSchool(), tempTeacher.getTeacherId());
            System.out.println("查询分配给教师" + tempTeacher + "进行评分的作品为" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/judgeWork")
    public Result judgeWork(@RequestParam("score") int score, @RequestParam("workName") String workName) {
        switch (teacherService.judgeWork(score, workName)) {
            case 1 -> {
                System.out.println("校赛评委成功给作品" + workName + "评分" + score);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("校赛评委未能成功给作品" + workName + "评分" + score);
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Password password, HttpSession session) {
        Teacher tempTeacher = (Teacher) session.getAttribute("teacher");
        switch (teacherService.updatePassword(password, tempTeacher.getSchool(), tempTeacher.getTeacherId())) {
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
