package cqu.jsjds.controller;

import cqu.jsjds.entity.*;
import cqu.jsjds.service.SchoolManagerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import static cqu.jsjds.common.Constants.UPLOAD_DIR;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/schoolManager")
@RestController
public class SchoolManagerController {
    @Autowired
    SchoolManagerService schoolManagerService;

    @GetMapping("/getTeacherOfSchool")
    public Result<List<Teacher>> getTeacherOfSchool(HttpSession session) {
        Manager schoolManager = (Manager) session.getAttribute("schoolManager");
        String school = schoolManager.getSchool();
        try {
            List<Teacher> teacherList = schoolManagerService.getTeacherOfSchool(school);
            System.out.println(school + "的老师有" + teacherList);
            return Result.build(teacherList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println("查询" + school + "的老师失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/deleteTeacher")
    public Result deleteTeacher(@RequestParam String school, @RequestParam String teacherId) {
        switch (schoolManagerService.deleteTeacher(school, teacherId)) {
            case 1 -> {
                System.out.println("成功删除老师" + school + teacherId);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("删除老师" + school + teacherId + "失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/updateIdentityOfTeacher")
    public Result updateIdentityOfTeacher(@RequestParam String school, @RequestParam String teacherId, @RequestParam String identity) {
        switch (schoolManagerService.updateIdentityOfTeacher(school, teacherId, identity)) {
            case 1 -> {
                System.out.println("成功修改教师" + school + teacherId + "权限为" + identity);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("修改教师" + school + teacherId + "的权限失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }

        }

    }

    @GetMapping("/getWorkOfSchool")
    public Result<List<Work>> getWorkOfSchool(HttpSession session) {
        Manager schoolManager = (Manager) session.getAttribute("schoolManager");
        System.out.println(schoolManager + "-------------");
        String school = schoolManager.getSchool();
        try {
            List<Work> workList = schoolManagerService.getWorkOfSchool(school, 1);
            System.out.println(school + "的所有校赛作品" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("查询" + school + "的所有校赛作品失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/getProvinceWorkOfSchool")
    public Result<List<Work>> getProvinceWorkOfSchool(HttpSession session) {
        Manager schoolManager = (Manager) session.getAttribute("schoolManager");
        System.out.println(schoolManager + "-------------");
        String school = schoolManager.getSchool();
        try {
            List<Work> workList = schoolManagerService.getProvinceWorkOfSchool(school, 1);
            System.out.println(school + "的所有省赛作品" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("查询" + school + "的所有省赛作品失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/getProvinceQuotaOfSchool")
    public Result<String> getProvinceQuotaOfSchool(HttpSession session) {
        Manager schoolManager = (Manager) session.getAttribute("schoolManager");
        System.out.println(schoolManager + "-------------");
        String school = schoolManager.getSchool();
        try {
            String quota = schoolManagerService.getProvinceQuotaOfSchool(school);
            System.out.println(school + "的省赛作品参赛配额" + quota);
            return Result.build(quota, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("查询" + school + "的所有省赛作品参赛配额");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/autoSendWorkToProvinceCompetition")
    public Result autoSendWorkToProvinceCompetition(HttpSession session) {
        Manager schoolManager = (Manager) session.getAttribute("schoolManager");
        System.out.println(schoolManager + "-------------");
        switch (schoolManagerService.autoSendWorkToProvinceCompetition(schoolManager.getSchool(), schoolManager.getQuota())) {
            case 1 -> {
                System.out.println("成功将学校" + schoolManager.getSchool() + "的优秀作品推送到省赛");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("学校" + schoolManager.getSchool() + "的优秀作品已经推送完成，无需再次点击");
                return Result.build(null, ResultCodeEnum.NO_REPEAT_OPERATE);
            }
            default -> {
                System.out.println("未能将学校" + schoolManager.getSchool() + "的优秀作品推送到省赛");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }

    }

//    @GetMapping("/sendToProvinceCompetition")
//    public Result sendToProvinceCompetition(@RequestParam String workName) {
//        switch (schoolManagerService.sendToProvinceCompetition(workName)) {
//            case 1 -> {
//                System.out.println("成功将" + workName + "推送到省赛");
//                return Result.build(null, ResultCodeEnum.SUCCESS);
//            }
//            default -> {
//                System.out.println("推送" + workName + "到省赛失败");
//                return Result.build(null, ResultCodeEnum.DATA_ERROR);
//            }
//        }
//    }

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

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Password password, HttpSession session) {
        Manager tempManager = (Manager) session.getAttribute("schoolManager");
        switch (schoolManagerService.updatePassword(password, tempManager.getManagerId())) {
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

    @GetMapping("/getSchoolStatisticsInfo")
    public Result<Map<String, Object>> getSchoolStatisticsInfo(HttpSession session) {
        Manager schoolManager = (Manager) session.getAttribute("schoolManager");
        try {
            Map<String, Object> map = schoolManagerService.getSchoolStatisticsInfo(schoolManager.getSchool());
            return Result.build(map, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

}
