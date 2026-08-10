package cqu.jsjds.service.impl;

import cqu.jsjds.entity.*;
import cqu.jsjds.mapper.GroupMapper;
import cqu.jsjds.mapper.StudentMapper;
import cqu.jsjds.mapper.TeacherMapper;
import cqu.jsjds.mapper.WorkMapper;
import cqu.jsjds.mapper.TeacherJudgeWorkMapper;
import cqu.jsjds.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private WorkMapper workMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherJudgeWorkMapper teacherJudgeWorkMapper;

    @Override
    public Teacher getTeacherInfo(String school, String id) {
        return teacherMapper.findTeacherById(school, id);
    }

    @Override
    public boolean updateTeacherInfo(Teacher teacher) {
        try {
            teacherMapper.updateTeacher(teacher);
            return true;

        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public int addGroup(Group group) {
        try {
            groupMapper.insertGroup(group);
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
    public List<Group> getGroupOfTeacher(String school, String teacherId) {
        return groupMapper.findGroupByTeacherId(school, teacherId);
    }

    @Override
    public List<Student> getStudentApplyingGroupOfTeacher(String school, String teacherID) {
        return studentMapper.findStudentByGroupOfTeacher(school, teacherID);

    }

    @Override
    public int acceptApplyOfStudent(String school, String studentId, String groupName) {
        try {
            studentMapper.updateApplyGroupNameById(null, school, studentId);
            studentMapper.updateStudentWithGroupName(groupName, school, studentId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int refuseApplyOfStudent(String school, String studentId, String groupName) {
        try {
            studentMapper.updateApplyGroupNameById(null, school, studentId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public Group getGroupInfo(String groupId) {
        return groupMapper.findGroupById(groupId);
    }

    @Override
    public boolean deleteGroup(String groupId) {
        try {
            groupMapper.deleteGroupById(groupId);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    public int addWork(Work work) {
        try {
            workMapper.insertWork(work);
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
    public int deleteWork(String workName) {
        try {
            workMapper.deleteWork(workName);
            return 1;

        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public int sendToSchoolCompetition(String workName) {
        try {
            Work tempWork = workMapper.findWorkByWorkName(workName).get(0);
            List<Teacher> teacherList = teacherMapper.findTeacherByIdentityAndSchoolRandomly(2, groupMapper.findGroupById(tempWork.getGroupName()).getSchool());

            System.out.println("该作品被分配给校赛评委：" + teacherList + "-------------");
            if (!teacherList.isEmpty()) {
                Teacher tempteacher = teacherList.get(0);
                teacherJudgeWorkMapper.insertTeacherJudgeWork(tempteacher.getSchool(), tempteacher.getTeacherId(), workName);
            }

            workMapper.updateWorkWithIsSchoolCompetition(1, workName);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public List<Work> getWorkList(String groupName) {
        return workMapper.findWorkByGroupName(groupName);
    }

    @Override
    public List<Student> getStudentOfGroup(String groupName) {
        return studentMapper.findStudentByGroupName(groupName);

    }

    @Override
    public int deleteStudentFromGroup(String school, String studentId) {
        try {
            studentMapper.updateStudentWithGroupName(null, school, studentId);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 2;
        }
    }

    @Override
    public List<Work> getAssignedWork(String school, String teacherId) {

        return teacherJudgeWorkMapper.findWorkByTeacherId(school, teacherId);

    }

    @Override
    public int judgeWork(int score, String workName) {
        try {
            teacherJudgeWorkMapper.updateSCScoreByWorkName(workName, score);
            workMapper.updateSchoolCompetitionScoreByWorkName(workName, score);
            return 1;
        } catch (Exception e) {
            System.err.println(e);
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
    public int updatePassword(Password password, String school, String teacherId) {
        try {
            if (!password.getOldPassword().equals(teacherMapper.findPasswordById(school, teacherId))) {
                return 1;
            } else if (!password.getNewPassword().equals(password.getNewRePassword())) {
                return 2;
            } else {
                teacherMapper.updatePasswordById(password.getNewPassword(), school, teacherId);
                return 3;
            }

        } catch (Exception e) {
            System.err.println(e);
            return 4;

        }
    }
}
