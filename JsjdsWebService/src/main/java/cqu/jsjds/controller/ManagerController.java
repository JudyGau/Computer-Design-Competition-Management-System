package cqu.jsjds.controller;


import cqu.jsjds.entity.*;
import cqu.jsjds.service.ManagerService;
import jakarta.servlet.http.HttpSession;
import org.apache.tomcat.Jar;
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
@RequestMapping("/manager")
@RestController
public class ManagerController {
    @Autowired
    ManagerService managerService;

    @PostMapping("/makeSchoolManager")
    public Result makeSchoolManager(@RequestBody Manager manager) {
        System.out.println("创建校管理员" + manager);
        switch (managerService.makeSchoolManager(manager)) {
            case 1 -> {
                System.out.println("成功创建校管理员" + manager);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("校管理账号已存在");
                return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getAllSchoolManager")
    public Result<List<Manager>> getAllSchoolManager() {
        try {
            List<Manager> managerList = managerService.getAllSchoolManager();
            System.out.println("全部校管理员" + managerList);
            return Result.build(managerList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @PostMapping("/reviseSchoolManager")
    public Result reviseSchoolManager(@RequestBody Manager manager) {

        switch (managerService.reviseSchoolManager(manager)) {
            case 1 -> {
                System.out.println("成功修改校管理员" + manager.getManagerId());
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("修改校管理员" + manager.getManagerId() + "失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }

    }

    @GetMapping("/deleteSchoolManager")
    public Result deleteSchoolManager(@RequestParam String managerId) {

        switch (managerService.deleteSchoolManager(managerId)) {
            case 1 -> {
                System.out.println("成功删除校管理员" + managerId);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }

    }

    @PostMapping("/makeReviewer")
    public Result makeReviewer(@RequestBody Reviewer reviewer) {
        System.out.println("新增审核员" + reviewer);
        switch (managerService.makeReviewer(reviewer)) {
            case 1 -> {
                System.out.println("新增审核员成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("审核账号已存在");
                return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getAllReviewer")
    public Result<List<Reviewer>> getAllReviewer() {

        try {
            List<Reviewer> reviewerList = managerService.getAllReviewer();
            System.out.println("全部审核员" + reviewerList);
            return Result.build(reviewerList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println("查询审核员失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @PostMapping("/updateReviewer")
    public Result updateReviewer(@RequestBody Reviewer reviewer) {
        switch (managerService.updateReviewer(reviewer)) {
            case 1 -> {
                System.out.println("成功修改审核员" + reviewer.getReviewerId());
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("修改审核员" + reviewer.getReviewerId() + "失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/deleteReviewer")
    public Result deleteReviewer(@RequestParam String reviewerId) {
        switch (managerService.deleteReviewer(reviewerId)) {
            case 1 -> {
                System.out.println("成功删除审核员" + reviewerId);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("删除审核员" + reviewerId + "失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @PostMapping("/makeJudge")
    public Result makeJudge(@RequestBody Judge judge) {
        switch (managerService.makeJudge(judge)) {
            case 1 -> {
                System.out.println("成功创建评委" + judge);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("评委账号已存在");
                return Result.build(null, ResultCodeEnum.USER_NAME_IS_EXISTS);
            }
            default -> {
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getAllJudge")
    public Result<List<Judge>> getAllJudge() {
        try {
            List<Judge> judgeList = managerService.getAllJudge();
            System.out.println("全部评委" + judgeList);
            return Result.build(judgeList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println("查询评委失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @PostMapping("/updateJudge")
    public Result updateJudge(@RequestBody Judge judge) {
        switch (managerService.updateJudge(judge)) {
            case 1 -> {
                System.out.println("成功修改评委" + judge.getJudgeId());
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("修改评委" + judge.getJudgeId() + "失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/deleteJudge")
    public Result deleteJudge(@RequestParam String judgeId) {
        switch (managerService.deleteJudge(judgeId)) {
            case 1 -> {
                System.out.println("成功删除评委" + judgeId);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("删除评委" + judgeId + "失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);
            }
        }
    }

    @GetMapping("/getWorkOfProvinceCompetition")
    public Result<List<Work>> getWorkOfProvinceCompetition() {
        try {
            List<Work> workList = managerService.getWorkOfProvinceCompetition();
            System.out.println("所有省赛作品" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            System.out.println("查询省赛作品失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/sendBack")
    public Result sendBack(@RequestParam String workName) {
        switch (managerService.sendBack(workName)) {
            case 1 -> {
                System.out.println("成功退回作品" + workName);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("退回作品" + workName + "失败");
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

    @GetMapping("/autoAward")
    public Result autoAward(@RequestParam("firstAward") int firstAward,
                            @RequestParam("secondAward") int secondAward,
                            @RequestParam("thirdAward") int thirdAward) {
        switch (managerService.autoAward(firstAward, secondAward, thirdAward)) {
            case 1 -> {
                System.out.println("自动评奖成功");
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            case 2 -> {
                System.out.println("获奖比例错误");
                return Result.build(null, ResultCodeEnum.AWARD_PERCENT_ERROR);
            }
            default -> {
                System.out.println("自动评奖失败");
                return Result.build(null, ResultCodeEnum.DATA_ERROR);

            }
        }
    }

    @GetMapping("/getAwardWorkListOfCategory")
    public Result<List<Work>> getAwardWorkListOfCategory(@RequestParam("category") String category) {
        try {
            List<Work> workList = managerService.getAwardWorkListOfCategory(category);
            System.out.println(category + "大类的作品按照分数降序排列的结果为" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println("查找并按分数降序排列" + category + "大类的作品失败");
            System.out.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }

    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Password password, HttpSession session) {
        Manager tempManager = (Manager) session.getAttribute("manager");
        switch (managerService.updatePassword(password, tempManager.getManagerId())) {
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

    @GetMapping("/getStatisticsInfo")
    public Result<Map<String, Object>> getStatisticsInfo() {
        try {
            Map<String, Object> map = managerService.getStatisticsInfo();
            return Result.build(map, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/getCategoryStatisticsInfo")
    public Result<Map<String, Object>> getStatisticsInfo(@RequestParam("category") String category) {
        try {
            Map<String, Object> map = managerService.getCategoryStatisticsInfo(category);
            return Result.build(map, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.err.println(e);
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }


}
