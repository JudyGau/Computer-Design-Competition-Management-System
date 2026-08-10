package cqu.jsjds.service;

import cqu.jsjds.entity.Judge;
import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Work;

import java.util.List;

public interface JudgeService {
    Judge getJudgeInfo(String judgeId);

    public List<Work> getApprovedWork(String judgeId);

    public int judgeWork(int score, String workName);

    int updatePassword(Password password, String judgeId);
}
