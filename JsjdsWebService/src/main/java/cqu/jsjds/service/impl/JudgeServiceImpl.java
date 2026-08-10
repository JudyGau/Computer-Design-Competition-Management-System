package cqu.jsjds.service.impl;

import cqu.jsjds.entity.Judge;
import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Work;
import cqu.jsjds.mapper.JudgeJudgeWorkMapper;
import cqu.jsjds.mapper.JudgeMapper;
import cqu.jsjds.mapper.WorkMapper;
import cqu.jsjds.service.JudgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Autowired
    WorkMapper workMapper;

    @Autowired
    JudgeMapper judgeMapper;

    @Autowired
    JudgeJudgeWorkMapper judgeJudgeWorkMapper;


    @Override
    public Judge getJudgeInfo(String judgeId) {
        return judgeMapper.findJudgeById(judgeId);
    }

    @Override
    public List<Work> getApprovedWork(String judgeId) {

        return judgeJudgeWorkMapper.findWorkByJudgeId(judgeId);
        //return workMapper.findWorkByIsApprovedAndCategory(1, category);
    }

    @Override
    public int judgeWork(int score, String workName) {
        try {
            workMapper.updateWorkWithScore(score, workName);
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    @Override
    public int updatePassword(Password password, String judgeId) {
        try {
            if (!password.getOldPassword().equals(judgeMapper.findPasswordById(judgeId))) {
                return 1;
            } else if (!password.getNewPassword().equals(password.getNewRePassword())) {
                return 2;
            } else {
                judgeMapper.updatePasswordById(password.getNewPassword(), judgeId);
                return 3;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 4;
        }
    }
}
