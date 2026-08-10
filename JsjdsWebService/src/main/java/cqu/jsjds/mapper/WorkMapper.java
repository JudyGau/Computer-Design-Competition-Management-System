package cqu.jsjds.mapper;

import cqu.jsjds.entity.Group;
import cqu.jsjds.entity.Work;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkMapper {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertWork(Work work) {
        jdbcTemplate.update("insert into work values (?,?,?,?,?,?,?,?,?,?,?,?)",
                work.getWorkName(), work.getGroupName(), work.getCategory(), work.getSubclass(), work.getIsSubmitted(), work.getIsSchoolCompetition(), work.getIsProvinceCompetition(), work.getIsApproved(), work.getScore(), work.getAward(), work.getSchoolCompetitionScore(), work.getComment());
    }

    public List<Work> findWorkByGroupName(String groupName) {
        return jdbcTemplate.query("select * from work where group_name=?", new BeanPropertyRowMapper<>(Work.class), groupName);
    }

    public void deleteWork(String workName) {
        jdbcTemplate.update("delete from work where work_name=?", workName);
    }

    public void updateWorkWithIsSubmitted(int flag, String workName) {
        jdbcTemplate.update("update work set is_submitted=? where work_name=?", flag, workName);

    }

    public List<Work> findWorkByIsProvinceCompetition(int flag) {
        return jdbcTemplate.query("select * from work where is_province_competition=?", new BeanPropertyRowMapper<>(Work.class), flag);
    }

    public List<Work> findWorkByIsProvinceCompetitionAndCategory(int flag, String category) {
        return jdbcTemplate.query("select * from work where is_province_competition=? and category=?", new BeanPropertyRowMapper<>(Work.class), flag, category);
    }

    public void updateWorkWithIsProvinceCompetition(int flag, String workName) {
        jdbcTemplate.update("update work set is_province_competition=? where work_name=?", flag, workName);
    }

    public void updateWorkWithIsSchoolCompetition(int flag, String workName) {
        jdbcTemplate.update("update work set is_school_competition=? where work_name=?", flag, workName);
    }

    public List<Work> findWorkBySchoolAndIsSchoolCompetition(String school, int flag) {
        return jdbcTemplate.query("select * from work join `group` on work.group_name=`group`.group_name where `group`.school=? and is_school_competition=? order by school_competition_score desc ", new BeanPropertyRowMapper<>(Work.class), school, flag);
    }

    public List<Work> findWorkBySchoolAndIsProvinceCompetition(String school, int flag) {
        return jdbcTemplate.query("select * from work join `group` on work.group_name=`group`.group_name where `group`.school=? and is_province_competition=? order by school_competition_score desc", new BeanPropertyRowMapper<>(Work.class), school, flag);
    }

    public void updateWorkWithIsApproved(int isApproved, String workName, String comment) {
        jdbcTemplate.update("update work set is_approved=?, comment=? where work_name=?", isApproved, comment, workName);
    }

    public List<Work> findWorkByIsApprovedAndCategory(int isApproved, String category) {
        return jdbcTemplate.query("select * from work where is_approved=? and category=?", new BeanPropertyRowMapper<>(Work.class), isApproved, category);
    }

    public void updateWorkWithScore(int score, String workName) {
        jdbcTemplate.update("update work set score=? where work_name=?", score, workName);
    }

    public void updateAwardByCategory(int percent1, int percent2, int percent3, String category) {
        // 1. 计算总数
        Integer total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM work WHERE category = ? AND score != -1",
                Integer.class, category
        );

        // 2. 计算各段阈值
        int limit1 = (int) Math.ceil(total * percent1 / 100.0);
        int limit2 = (int) Math.ceil(total * percent2 / 100.0);
        int limit3 = (int) Math.ceil(total * percent3 / 100.0);

        Integer threshold1 = getThreshold(category, limit1);
        Integer threshold2 = getThreshold(category, limit2);
        Integer threshold3 = getThreshold(category, limit3);

        // 3. 执行更新
        String updateSql = """
                UPDATE work SET award = CASE
                    WHEN score >= ? THEN 1
                    WHEN score >= ? THEN 2
                    WHEN score >= ? THEN 3
                    ELSE award
                END
                WHERE category = ? AND score != -1
                """;

        jdbcTemplate.update(updateSql,
                threshold1, threshold2, threshold3, category
        );
    }

    private Integer getThreshold(String category, int limit) {
        return jdbcTemplate.queryForObject(
                "SELECT MIN(score) FROM (" +
                        "SELECT score FROM work " +
                        "WHERE category = ? AND score != -1 " +
                        "ORDER BY score DESC LIMIT ?" +
                        ") AS t",
                Integer.class, category, limit
        );
    }

    /**
     * 根据类别查询作品并按分数降序排列
     *
     * @param category 作品大类名称
     * @return 排序后的作品列表
     */
    public List<Work> findWorksByCategoryOrderByScoreDesc(String category) {
        String sql = "SELECT * FROM work WHERE category = ? AND award!=0 ORDER BY score DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Work.class), category);
    }

    public List<Map<String, Integer>> findWorkNumberOfEachCategory() {

        return jdbcTemplate.query("select category, count(*) from work group by category",
                (rs, rowNum) -> {
                    Map<String, Integer> result = new HashMap<>();
                    result.put(rs.getString("category"), rs.getInt(2));  // `count(*)` is the second column
                    return result;
                });
    }

    public List<Map<String, Integer>> findWorkNumberOfEachSchool() {

        return jdbcTemplate.query("select `group`.school, count(*) as count from work join `group` on work.group_name=`group`.group_name group by `group`.school order by count desc",
                (rs, rowNum) -> {
                    Map<String, Integer> result = new HashMap<>();
                    result.put(rs.getString("school"), rs.getInt(2));  // `count(*)` is the second column
                    return result;
                });
    }

    public List<Map<String, Integer>> findWorkNumberOfEachSchoolByCategory(String category) {

        return jdbcTemplate.query("select `group`.school ,count(*) from work join `group` on work.group_name=`group`.group_name where category=? group by `group`.school ",
                (rs, rowNum) -> {
                    Map<String, Integer> result = new HashMap<>();
                    result.put(rs.getString("school"), rs.getInt(2));  // `count(*)` is the second column
                    return result;
                }, category);
    }

    public List<Map<String, Integer>> findWorkNumberOfEachScoreByCategory(String category) {

        return jdbcTemplate.query("SELECT " +
                        "    CASE" +
                        "        WHEN work.score BETWEEN 90 AND 100 THEN '90-100'" +
                        "        WHEN work.score BETWEEN 80 AND 89 THEN '80-89'" +
                        "        WHEN work.score BETWEEN 70 AND 79 THEN '70-79'" +
                        "        WHEN work.score BETWEEN 60 AND 69 THEN '60-69'" +
                        "        ELSE '不合格' " +
                        "    END AS value_range, " +
                        "    COUNT(*) AS count " +
                        "FROM work join `group` on work.group_name=`group`.group_name where work.category=? and work.score!=-1 GROUP BY value_range",
                (rs, rowNum) -> {
                    Map<String, Integer> result = new HashMap<>();
                    result.put(rs.getString("value_range"), rs.getInt(2));  // `count(*)` is the second column
                    return result;
                }, category);
    }

    public String findWorkNumber() {
        return jdbcTemplate.queryForObject("select count(*) from work", String.class);
    }

    public String findWorkNumberByIsProvinceCompetition(int flag) {
        return jdbcTemplate.queryForObject("select count(*) from work where is_province_competition=?", String.class, flag);
    }

    public List<String> findWOrkNumberOfEachAward() {
        return jdbcTemplate.query("select count(*) from work group by award order by award", new BeanPropertyRowMapper<>(String.class));
    }

    public List<Map<String, Integer>> findWorkNumberOfEachCategoryBySchool(String school) {
        return jdbcTemplate.query("select category, count(*) from work join `group` on work.group_name=`group`.group_name where `group`.school=? group by work.category",
                (rs, rowNum) -> {
                    Map<String, Integer> result = new HashMap<>();
                    result.put(rs.getString("category"), rs.getInt(2));  // `count(*)` is the second column
                    return result;
                }, school);
    }

    public List<Map<String, Integer>> findWorkNumberOfEachScoreBySchool(String school) {
        return jdbcTemplate.query("SELECT " +
                        "    CASE" +
                        "        WHEN work.score BETWEEN 90 AND 100 THEN '90-100'" +
                        "        WHEN work.score BETWEEN 80 AND 89 THEN '80-89'" +
                        "        WHEN work.score BETWEEN 70 AND 79 THEN '70-79'" +
                        "        WHEN work.score BETWEEN 60 AND 69 THEN '60-69'" +
                        "        ELSE '不合格' " +
                        "    END AS value_range, " +
                        "    COUNT(*) AS count " +
                        "FROM work join `group` on work.group_name=`group`.group_name where `group`.school=? and work.score!= -1 GROUP BY value_range",
                (rs, rowNum) -> {
                    Map<String, Integer> result = new HashMap<>();
                    result.put(rs.getString("value_range"), rs.getInt(2));  // `count(*)` is the second column
                    return result;
                }, school);

    }

    public List<Map<String, Integer>> findWorkNumberOfEachAwardBySchool(String school) {
        String[] award = {"一等奖", "二等奖", "三等奖"};
        return jdbcTemplate.query("select award, count(*) from work join `group` on work.group_name=`group`.group_name where `group`.school=? and work.award!=0 group by work.award order by work.award", (rs, rowNum) -> {
            Map<String, Integer> result = new HashMap<>();
            result.put(award[rs.getInt("award") - 1], rs.getInt(2));  // `count(*)` is the second column
            return result;
        }, school);
    }

    public String findWorkNumberBySchool(String school) {
        return jdbcTemplate.queryForObject("select count(*) as count from work w join `group` g on g.group_name = w.group_name where g.school=? group by g.school", new BeanPropertyRowMapper<>(String.class), school);
    }

    public List<Work> findWorkByWorkName(String workName) {
        return jdbcTemplate.query("select * from work where work_name=?", new BeanPropertyRowMapper<>(Work.class), workName);
    }

    public void updateSchoolCompetitionScoreByWorkName(String workName, int score) {
        jdbcTemplate.update("update work set school_competition_score=? where work_name=?", score, workName);
    }

    public List<Work> findWorkBySchoolOrderBySCScoreLimitQuota(String school, int quota) {
        return jdbcTemplate.query("select * from work w join `group` g on w.group_name = g.group_name where g.school=? order by w.school_competition_score desc limit ? ", new BeanPropertyRowMapper<>(Work.class), school, quota);

    }


}
