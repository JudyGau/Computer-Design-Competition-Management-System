package cqu.jsjds.mapper;

import cqu.jsjds.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherJudgeWorkMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertTeacherJudgeWork(String school, String teacherId, String workName) {
        jdbcTemplate.update("insert into teacher_judge_work(school, teacher_id, work_name, sc_score) value(?,?,?,?)", school, teacherId, workName, -1);
    }

    public List<Work> findWorkByTeacherId(String school, String teacherId) {
        return jdbcTemplate.query("select  w.work_name, group_name, category, subclass, is_submitted, is_school_competition, is_province_competition, is_approved, score, award,school_competition_score from teacher_judge_work tjw join work w on tjw.work_name=w.work_name where tjw.school=? and tjw.teacher_id=? ", new BeanPropertyRowMapper<>(Work.class), school, teacherId);
    }

    public void updateSCScoreByWorkName(String workName, int scScore) {
        jdbcTemplate.update("update teacher_judge_work set sc_score=? where work_name=?", scScore, workName);

    }
}
