package cqu.jsjds.mapper;

import cqu.jsjds.entity.Manager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ManagerMapper {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public Manager findManagerByIdAndPassword(String managerId, String password) {
        return jdbcTemplate.queryForObject("select * from jsjds.manager where manager_id=? and password=?", new BeanPropertyRowMapper<>(Manager.class), managerId, password);
    }

    public void insertManager(Manager manager) {
        jdbcTemplate.update("insert into manager values (?,?,?,?,?)", manager.getManagerId(), manager.getPassword(), manager.getSchool(), manager.getAdministratorType(), manager.getQuota());
    }

    public List<Manager> findManagerByType(int type) {
        return jdbcTemplate.query("select * from manager where administrator_type=?", new BeanPropertyRowMapper<>(Manager.class), type);
    }

    public void updateManagerByManagerId(Manager manager) {
        jdbcTemplate.update("update manager set password=?, quota=? where manager_id=?", manager.getPassword(), manager.getQuota(), manager.getManagerId());
    }

    public void deleteManagerByManagerId(String managerId) {
        jdbcTemplate.update("delete from manager where manager_id = ?", managerId);
    }

    public String findSchoolByManagerId(String managerId) {
        return jdbcTemplate.queryForObject("select school from manager where manager_id=?", String.class, managerId);
    }

    public String findPasswordById(String managerId) {
        return jdbcTemplate.queryForObject("select password from manager where manager_id=?", String.class, managerId);
    }

    public void updatePasswordById(String password, String managerId) {
        jdbcTemplate.update("update manager set password=? where manager_id=?", password, managerId);
    }

    public String findQuotaBySchool(String school) {
        return jdbcTemplate.queryForObject("select quota from manager where school=?", String.class, school);

    }

    public Manager findManagerByManagerId(String managerId){
        return jdbcTemplate.queryForObject("select * from manager where manager_id=?",new BeanPropertyRowMapper<>(Manager.class),managerId);

    }

}
