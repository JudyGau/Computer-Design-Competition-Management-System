package cqu.jsjds.mapper;

import cqu.jsjds.entity.Reviewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewerMapper {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertReviewer(Reviewer reviewer) {
        System.out.println(reviewer + "-------------------");
        jdbcTemplate.update("insert into reviewer values (?,?,?,?,?)", reviewer.getReviewerId(), reviewer.getPassword(), reviewer.getTelephone(), reviewer.getEmail(), reviewer.getCategory());
    }

    public List<Reviewer> findReviewerByIdAndPassword(String reviewerId, String password) {
        return jdbcTemplate.query("select * from reviewer where reviewer_id=? and password=?", new BeanPropertyRowMapper<>(Reviewer.class), reviewerId, password);
    }

    public List<Reviewer> findAllReviewer() {
        return jdbcTemplate.query("select * from reviewer", new BeanPropertyRowMapper<>(Reviewer.class));
    }

    public void updateReviewerByReviewerId(Reviewer reviewer) {
        jdbcTemplate.update("update reviewer set password=?, category=? where reviewer_id=?", reviewer.getPassword(), reviewer.getCategory(), reviewer.getReviewerId());
    }

    public void deleteReviewerByReviewerId(String reviewerId) {
        jdbcTemplate.update("delete from reviewer where reviewer_id=?", reviewerId);
    }

    public String findPasswordById(String reviewerId) {
        return jdbcTemplate.queryForObject("select password from reviewer where reviewer_id=?", String.class, reviewerId);
    }

    public void updatePasswordById(String password, String reviewerId) {
        jdbcTemplate.update("update reviewer set password=?where  reviewer_id=?", password, reviewerId);
    }

    public Reviewer findReviewerById(String reviewerId) {
        return jdbcTemplate.queryForObject("select * from reviewer where reviewer_id=?", new BeanPropertyRowMapper<>(Reviewer.class), reviewerId);
    }

    public List<Reviewer> findReviewerByCategoryRandomly(String category) {
        return jdbcTemplate.query("select * from reviewer where category=? order by rand() limit 1", new BeanPropertyRowMapper<>(Reviewer.class), category);
    }
}
