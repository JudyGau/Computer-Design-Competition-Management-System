package cqu.jsjds.mapper;

import cqu.jsjds.entity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GroupMapper {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void insertGroup(Group group) {
        jdbcTemplate.update("insert into `group` values (?,?,?,?,?)", group.getGroupName(), group.getNumber(), group.getSchool(), group.getTeacherId(), 0);
    }

    public List<Group> findGroupByTeacherId(String school, String teacherId) {
        return jdbcTemplate.query("select * from `group` where school=? and teacherId=?", new BeanPropertyRowMapper<>(Group.class), school, teacherId);
    }

    public Group findGroupById(String name) {
        return jdbcTemplate.queryForObject("select * from `group` where group_name=?", new BeanPropertyRowMapper<>(Group.class), name);
    }

    public void deleteGroupById(String name) {
        jdbcTemplate.update("delete from `group` where group_name=? ", name);
    }

    public int findNumberByGroupName(String groupName) {
        return jdbcTemplate.queryForObject("select number from `group` where group_name=?", Integer.class, groupName);
    }

    public int findCurrentNumberByGroupName(String groupName) {
        return jdbcTemplate.queryForObject("select current_number from `group` where group_name=?", Integer.class, groupName);
    }

    public List<Group> findGroupBySchool(String school){
        return jdbcTemplate.query( "select * from `group` where school=?" ,new BeanPropertyRowMapper<>(Group.class),school);
    }
}
