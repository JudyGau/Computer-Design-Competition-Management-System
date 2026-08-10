package cqu.jsjds.service.impl;

import cqu.jsjds.common.Constants;
import cqu.jsjds.entity.*;
import cqu.jsjds.mapper.JudgeMapper;
import cqu.jsjds.mapper.ManagerMapper;
import cqu.jsjds.mapper.ReviewerMapper;
import cqu.jsjds.mapper.WorkMapper;
import cqu.jsjds.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cqu.jsjds.utils.Function.splitListIntoArrays;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    ManagerMapper managerMapper;

    @Autowired
    ReviewerMapper reviewerMapper;

    @Autowired
    JudgeMapper judgeMapper;

    @Autowired
    WorkMapper workMapper;

    @Override
    public int makeSchoolManager(Manager manager) {
        try {
            manager.setAdministratorType(1);
            managerMapper.insertManager(manager);
            return 1;
        } catch (DuplicateKeyException e) {
            return 2;
        } catch (Exception e) {
            return 3;
        }
    }

    @Override
    public List<Manager> getAllSchoolManager() {
        return managerMapper.findManagerByType(1);
    }

    @Override
    public int reviseSchoolManager(Manager manager) {
        try {
            managerMapper.updateManagerByManagerId(manager);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int deleteSchoolManager(String managerId) {
        try {
            managerMapper.deleteManagerByManagerId(managerId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int makeReviewer(Reviewer reviewer) {
        try {
            reviewerMapper.insertReviewer(reviewer);
            return 1;
        } catch (DuplicateKeyException e) {
            System.err.println(e);
            return 2;
        } catch (Exception e) {
            System.err.println(e);
            return 3;
        }
    }

    @Override
    public List<Reviewer> getAllReviewer() {
        return reviewerMapper.findAllReviewer();
    }

    @Override
    public int updateReviewer(Reviewer reviewer) {
        try {
            reviewerMapper.updateReviewerByReviewerId(reviewer);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int deleteReviewer(String reviewerId) {
        try {
            reviewerMapper.deleteReviewerByReviewerId(reviewerId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int makeJudge(Judge judge) {
        try {
            judgeMapper.insertJudge(judge);
            return 1;
        } catch (DuplicateKeyException e) {
            System.err.println(e);
            return 2;
        } catch (Exception e) {
            System.err.println(e);
            return 3;
        }
    }

    @Override
    public List<Judge> getAllJudge() {
        return judgeMapper.findAllJudge();
    }

    @Override
    public int updateJudge(Judge judge) {
        try {
            judgeMapper.updateJudgeByJudgeId(judge);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int deleteJudge(String judgeId) {
        try {
            judgeMapper.deleteJudgeByJudgeId(judgeId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public List<Work> getWorkOfProvinceCompetition() {
        return workMapper.findWorkByIsProvinceCompetition(1);
    }

    @Override
    public int sendBack(String workName) {
        try {
            workMapper.updateWorkWithIsProvinceCompetition(0, workName);
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    @Override
    public int autoAward(int firstAwardPercent, int secondAwardPercent, int thirdAwardPercent) {
        try {
            if (firstAwardPercent < secondAwardPercent && secondAwardPercent < thirdAwardPercent) {
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY1);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY2);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY3);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY4);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY5);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY6);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY7);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY8);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY9);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY10);
                workMapper.updateAwardByCategory(firstAwardPercent, secondAwardPercent, thirdAwardPercent, Constants.CATEGORY11);
                return 1;
            } else {
                return 2;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 3;

        }
    }

    @Override
    public List<Work> getAwardWorkListOfCategory(String category) {
        return workMapper.findWorksByCategoryOrderByScoreDesc(category);
    }

    @Override
    public int updatePassword(Password password, String managerId) {
        try {
            if (!password.getOldPassword().equals(managerMapper.findPasswordById(managerId))) {
                return 1;
            } else if (!password.getNewPassword().equals(password.getNewRePassword())) {
                return 2;
            } else {
                managerMapper.updatePasswordById(password.getNewPassword(), managerId);
                return 3;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 4;
        }
    }


    @Override
    public Map<String, Object> getStatisticsInfo() {
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Integer>> mapList = workMapper.findWorkNumberOfEachCategory();
        Object[] objects = splitListIntoArrays(mapList);
        map.put("category", objects[0]);
        map.put("categoryWorkNumber", objects[1]);

        mapList = workMapper.findWorkNumberOfEachSchool();
        objects = splitListIntoArrays(mapList);
        map.put("school", objects[0]);
        map.put("schoolWorkNumber", objects[1]);

        int schoolNumber = mapList.size();
        //String workNumber = workMapper.findWorkNumber();
        String workNumber = workMapper.findWorkNumberByIsProvinceCompetition(1);
        List<String> awardNumber = workMapper.findWOrkNumberOfEachAward();
        String s = "本届计算机设计大赛成功举办，本赛区共有来自" + schoolNumber + "个学校的" + workNumber + "件作品成功晋级到省赛,其中有" + awardNumber.get(1) + "件作品获一等奖，有" +
                awardNumber.get(2) + "件作品获二等奖，有" + awardNumber.get(3) + "件作品获三等奖。";
        map.put("str", s);

        return map;
    }

    @Override
    public Map<String, Object> getCategoryStatisticsInfo(String category) {
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Integer>> mapList = workMapper.findWorkNumberOfEachSchoolByCategory(category);
        Object[] objects = splitListIntoArrays(mapList);
        map.put("school", objects[0]);
        map.put("schoolWorkNumber", objects[1]);

        mapList = workMapper.findWorkNumberOfEachScoreByCategory(category);
        objects = splitListIntoArrays(mapList);
        map.put("score", objects[0]);
        map.put("scoreWorkNumber", objects[1]);

        return map;
    }

    @Override
    public Manager getManagerInfo(String managerId) {
        return managerMapper.findManagerByManagerId(managerId);
    }


}
