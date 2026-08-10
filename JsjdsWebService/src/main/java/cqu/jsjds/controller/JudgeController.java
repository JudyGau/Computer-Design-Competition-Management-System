package cqu.jsjds.controller;

import cqu.jsjds.entity.*;
import cqu.jsjds.service.JudgeService;
import cqu.jsjds.service.ReviewerService;
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

import static cqu.jsjds.common.Constants.UPLOAD_DIR;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/judge")
@RestController
public class JudgeController {
    @Autowired
    JudgeService judgeService;

    @GetMapping("/getApprovedWork")
    public Result<List<Work>> getApprovedWork(HttpSession session) {
        try {
            Judge tempJudge = (Judge) session.getAttribute("judge");
            List<Work> workList = judgeService.getApprovedWork(tempJudge.getJudgeId());
            System.out.println(tempJudge.getCategory() + "大类通过审核的作品有" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println("获取通过审核的作品失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/judgeWork")
    public Result judgeWork(@RequestParam int score, @RequestParam String workName) {
        switch (judgeService.judgeWork(score, workName)) {
            case 1 -> {
                System.out.println("成功给作品" + workName + "评分" + score);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("给作品" + workName + "评分失败");
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

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody Password password, HttpSession session) {
        Judge tempJudge = (Judge) session.getAttribute("judge");
        switch (judgeService.updatePassword(password, tempJudge.getJudgeId())) {
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
