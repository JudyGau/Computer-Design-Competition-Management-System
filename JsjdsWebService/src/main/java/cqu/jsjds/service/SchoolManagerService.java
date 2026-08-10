package cqu.jsjds.service;

import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Teacher;
import cqu.jsjds.entity.Work;

import java.util.List;
import java.util.Map;

public interface SchoolManagerService {

    public List<Teacher> getTeacherOfSchool(String school);

    public int deleteTeacher(String school, String teacherId);

    int updateIdentityOfTeacher(String school, String teacherId, String identity);

    public List<Work> getWorkOfSchool(String school, int isSchoolCompetition);

    List<Work> getProvinceWorkOfSchool(String school, int flag);

    String getProvinceQuotaOfSchool(String school);

    int autoSendWorkToProvinceCompetition(String school, int quota);

//    public void sendToProvinceCompetition(String workName);


    int updatePassword(Password password, String schoolManagerId);

    Map<String, Object> getSchoolStatisticsInfo(String school);
}
