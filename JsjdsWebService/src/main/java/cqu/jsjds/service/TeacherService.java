package cqu.jsjds.service;

import cqu.jsjds.entity.*;

import java.util.List;

public interface TeacherService {
    Teacher getTeacherInfo(String school, String id);

    boolean updateTeacherInfo(Teacher teacher);

    int addGroup(Group group);

    List<Group> getGroupOfTeacher(String school,String teacherId);

    List<Student> getStudentApplyingGroupOfTeacher(String school, String teacherID);

    int acceptApplyOfStudent(String school, String studentId, String groupName);

    int refuseApplyOfStudent(String school, String studentId, String groupName);

    Group getGroupInfo(String groupId);

    boolean deleteGroup(String groupId);

    int addWork(Work work);

    int deleteWork(String workName);

    int sendToSchoolCompetition(String workName);

    List<Work> getWorkList(String groupName);

    List<Student> getStudentOfGroup(String groupName);

    int deleteStudentFromGroup( String school, String studentId);

    List<Work> getAssignedWork(String school, String teacherId);

    int judgeWork(int score, String workName);

    Work queryWork(String workName);

    int updatePassword(Password password, String school, String teacherId);
}
