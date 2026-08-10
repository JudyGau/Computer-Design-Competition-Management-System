package cqu.jsjds.service.impl;

import cqu.jsjds.entity.Group;
import cqu.jsjds.entity.Password;
import cqu.jsjds.entity.Student;
import cqu.jsjds.entity.Work;
import cqu.jsjds.mapper.GroupMapper;
import cqu.jsjds.mapper.StudentMapper;
import cqu.jsjds.mapper.WorkMapper;
import cqu.jsjds.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private WorkMapper workMapper;

    @Override
    public Student getStudentInfo(String school, String id) {
        return studentMapper.findStudentById(school, id);
    }

    @Override
    public boolean updateStudentInfo(Student student) {
        try {
            studentMapper.updateStudent(student);
            return true;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public int joinGroup(String applyGroupName, String school, String studentId) {
        try {
            Student tempStudent = studentMapper.findStudentById(school, studentId);
            if (tempStudent.getGroupName() != null) {
                return 4;
            } else if (tempStudent.getApplyGroupName() != null) {
                return 3;
            } else if (groupMapper.findNumberByGroupName(applyGroupName) <= groupMapper.findCurrentNumberByGroupName(applyGroupName)) {
                return 2;
            } else {
                studentMapper.updateApplyGroupNameById(applyGroupName, school, studentId);
                return 1;
            }
        } catch (Exception e) {
            return 3;
        }

    }


    @Override
    public List<Group> getGroupOfSchool(String school) {
        return groupMapper.findGroupBySchool(school);
    }

    @Override
    public Group getGroupInfo(String groupName) {
        return groupMapper.findGroupById(groupName);
    }

    @Override
    public List<Student> getStudentOfGroup(String groupName) {
        return studentMapper.findStudentByGroupName(groupName);
    }

    @Override
    public List<Work> getWorkOfGroup(String groupName) {
        return workMapper.findWorkByGroupName(groupName);
    }

    @Override
    public int submitWork(String workName) {
        try {
            workMapper.updateWorkWithIsSubmitted(1, workName);
            return 1;
        } catch (Exception e) {
            return 2;
        }
    }

    @Override
    public Work queryWork(String workName) {
        List<Work> workList = workMapper.findWorkByWorkName(workName);
        if (workList.isEmpty()) {
            return null;
        } else {
            return workList.get(0);
        }
    }

    @Override
    public int updatePassword(Password password, String school, String studentId) {
        try {
            if (!password.getOldPassword().equals(studentMapper.findPasswordById(school, studentId))) {
                return 1;
            } else if (!password.getNewPassword().equals(password.getNewRePassword())) {
                return 2;
            } else {
                studentMapper.updatePasswordById(password.getNewPassword(), school, studentId);
                return 3;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 4;

        }
    }


}
