package cqu.jsjds.service;

import cqu.jsjds.entity.Group;
import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Work;

import java.util.List;

public interface StudentService {

    public Student getStudentInfo(String school,String id);

    public boolean updateStudentInfo(Student student);

    int joinGroup(String groupName, String school, String studentId);

    Group getGroupInfo(String groupName);

    List<Group> getGroupOfSchool(String school);

    List<Student> getStudentOfGroup(String groupName);

    List<Work> getWorkOfGroup(String groupName);

    int submitWork(String workName);

    Work queryWork(String workName);

    int updatePassword(Password password, String school, String studentId);
}
