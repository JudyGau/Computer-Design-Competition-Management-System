package cqu.jsjds.service;

import cqu.jsjds.entity.*;

import java.util.List;
import java.util.Map;

public interface ManagerService {
    int makeSchoolManager(Manager manager);

    List<Manager> getAllSchoolManager();

    int reviseSchoolManager(Manager manager);

    int deleteSchoolManager(String managerId);

    int makeReviewer(Reviewer reviewer);

    List<Reviewer> getAllReviewer();

    int updateReviewer(Reviewer reviewer);

    int deleteReviewer(String reviewerId);

    int makeJudge(Judge judge);

    List<Judge> getAllJudge();

    int updateJudge(Judge judge);

    int deleteJudge(String judgeId);

    List<Work> getWorkOfProvinceCompetition();

    int sendBack(String workName);

    int autoAward(int firstAwardPercent, int secondAwardPercent, int thirdAwardPercent);

    List<Work> getAwardWorkListOfCategory(String category);

    int updatePassword(Password password, String managerId);

    Map<String, Object> getStatisticsInfo();

    Map<String, Object> getCategoryStatisticsInfo(String category);


    Manager getManagerInfo(String managerId);
}
