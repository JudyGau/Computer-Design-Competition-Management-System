package cqu.jsjds.controller;

import cqu.jsjds.entity.*;
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
@RequestMapping("/reviewer")
@RestController
public class ReviewerController {
    @Autowired
    ReviewerService reviewerService;

    @GetMapping("/getWorkOfProvinceCompetition")
    public Result<List<Work>> getWokOfProvinceCompetition(HttpSession session) {
        Reviewer tempReviewer = (Reviewer) session.getAttribute("reviewer");
        try {
            List<Work> workList = reviewerService.getWokOfProvinceCompetition(tempReviewer.getReviewerId());
            System.out.println(tempReviewer.getCategory() + "大类的省赛作品有" + workList);
            return Result.build(workList, ResultCodeEnum.SUCCESS);
        } catch (Exception e) {
            System.out.println("获取省赛作品失败");
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
    }

    @GetMapping("/reviewWork")
    public Result reviewWork(@RequestParam int isApproved, @RequestParam String workName, @RequestParam String comment) {
        switch (reviewerService.reviewWork(isApproved, workName, comment)) {
            case 1 -> {
                System.out.println("成功审核作品" + workName);
                return Result.build(null, ResultCodeEnum.SUCCESS);
            }
            default -> {
                System.out.println("审核作品" + workName + "失败");
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
        Reviewer tempReviewer = (Reviewer) session.getAttribute("reviewer");
        switch (reviewerService.updatePassword(password, tempReviewer.getReviewerId())) {
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
