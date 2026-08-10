package cqu.jsjds.service;

import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Reviewer;
import cqu.jsjds.entity.Work;

import java.util.List;

public interface ReviewerService {

    Reviewer getReviewerInfo(String reviewerId);

    public List<Work> getWokOfProvinceCompetition(String reviewerId);

    public int reviewWork(int isApproved, String workName, String comment);

    int updatePassword(Password password, String reviewerId);

}
