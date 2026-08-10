package cqu.jsjds.mapper;

import cqu.jsjds.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewerReviewWorkMapper {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertReviewerReviewWork(String reviewerId, String workName) {
        jdbcTemplate.update("insert into reviewer_review_work(work_name, reviewer_id) value(?,?)", workName, reviewerId);
    }

    public List<Work> findWorkByReviewerId(String reviewerId) {
        return jdbcTemplate.query("select w.work_name, group_name, category, subclass, is_submitted, is_school_competition, is_province_competition, is_approved, score, award, school_competition_score from reviewer_review_work rrw join work w on rrw.work_name = w.work_name where rrw.reviewer_id=?", new BeanPropertyRowMapper<>(Work.class), reviewerId);
    }


}
