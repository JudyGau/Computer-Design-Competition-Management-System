package cqu.jsjds.mapper;

import cqu.jsjds.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JudgeJudgeWorkMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertJudgeJudgeWork(String judgeId, String workName) {
        jdbcTemplate.update("insert into judge_judge_work(work_name, judge_id) value(?,?)", workName, judgeId);
    }

    public List<Work> findWorkByJudgeId(String judgeId) {
        return jdbcTemplate.query("select w.work_name, group_name, category, subclass, is_submitted, is_school_competition, is_province_competition, is_approved, score, award, school_competition_score from judge_judge_work jjw join work w on jjw.work_name = w.work_name where jjw.judge_id=?", new BeanPropertyRowMapper<>(Work.class), judgeId);
    }

}
