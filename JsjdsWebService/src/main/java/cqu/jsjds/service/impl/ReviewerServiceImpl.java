package cqu.jsjds.service.impl;

import cqu.jsjds.entity.Judge;
import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Reviewer;
import cqu.jsjds.entity.Work;
import cqu.jsjds.mapper.*;
import cqu.jsjds.service.ReviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewerServiceImpl implements ReviewerService {
    @Autowired
    WorkMapper workMapper;

    @Autowired
    ReviewerMapper reviewerMapper;

    @Autowired
    ReviewerReviewWorkMapper reviewerReviewWorkMapper;

    @Autowired
    JudgeJudgeWorkMapper judgeJudgeWorkMapper;

    @Autowired
    JudgeMapper judgeMapper;

    @Override
    public Reviewer getReviewerInfo(String reviewerId) {
        return reviewerMapper.findReviewerById(reviewerId);
    }

    @Override
    public List<Work> getWokOfProvinceCompetition(String reviewerId) {

        return reviewerReviewWorkMapper.findWorkByReviewerId(reviewerId);

//        return workMapper.findWorkByIsProvinceCompetitionAndCategory(1, category);
    }

    @Override
    public int reviewWork(int isApproved, String workName, String comment) {
        try {
            //如果作品审核通过则将该作品分配给某个省赛评委进行审核
            if (isApproved == 1) {
                Work tempWork = workMapper.findWorkByWorkName(workName).get(0);
                List<Judge> judgeList = judgeMapper.findJudgeByCategoryRandomly(tempWork.getCategory());

                System.out.println("该作品被分配给省赛评委：" + judgeList + "-------------");

                if (!judgeList.isEmpty()) {
                    Judge tempJudge = judgeList.get(0);
                    judgeJudgeWorkMapper.insertJudgeJudgeWork(tempJudge.getJudgeId(), workName);
                }
            }
            workMapper.updateWorkWithIsApproved(isApproved, workName, comment);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 3;
        }
    }

    @Override
    public int updatePassword(Password password, String reviewerId) {
        try {
            if (!password.getOldPassword().equals(reviewerMapper.findPasswordById(reviewerId))) {
                return 1;
            } else if (!password.getNewPassword().equals(password.getNewRePassword())) {
                return 2;
            } else {
                reviewerMapper.updatePasswordById(password.getNewPassword(), reviewerId);
                return 3;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 4;
        }
    }
}
