package cqu.jsjds.mapper;

import cqu.jsjds.entity.Judge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JudgeMapper {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertJudge(Judge judge) {
        jdbcTemplate.update("insert into judge values (?,?,?,?,?)", judge.getJudgeId(), judge.getPassword(), judge.getTelephone(), judge.getEmail(), judge.getCategory());
    }

    public List<Judge> findJudgeByIdAndPassword(String judgeId, String password) {
        return jdbcTemplate.query("select * from judge where judge_id=? and password=?", new BeanPropertyRowMapper<>(Judge.class), judgeId, password);
    }

    public List<Judge> findAllJudge() {
        return jdbcTemplate.query("select * from judge", new BeanPropertyRowMapper<>(Judge.class));
    }

    public void updateJudgeByJudgeId(Judge judge) {
        jdbcTemplate.update("update judge set password=?, category=? where judge_id=?", judge.getPassword(), judge.getCategory(), judge.getJudgeId());
    }

    public void deleteJudgeByJudgeId(String judgeId) {
        jdbcTemplate.update("delete from judge where judge_id=?", judgeId);
    }

    public String findPasswordById(String judgeId) {
        return jdbcTemplate.queryForObject("select password from judge where judge_id=?", String.class, judgeId);
    }

    public void updatePasswordById(String password, String judgeId) {
        jdbcTemplate.update("update judge set password=?where  judge_id=?", password, judgeId);
    }

    public Judge findJudgeById(String judgeId) {
        return jdbcTemplate.queryForObject("select * from judge where judge_id=?", new BeanPropertyRowMapper<>(Judge.class), judgeId);
    }

    public List<Judge> findJudgeByCategoryRandomly(String category){
        return jdbcTemplate.query("select * from judge where category=? order by rand() limit 1",new BeanPropertyRowMapper<>(Judge.class),category);
    }
}
