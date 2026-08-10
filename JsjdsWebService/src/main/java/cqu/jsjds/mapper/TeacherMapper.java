package cqu.jsjds.mapper;

import cqu.jsjds.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TeacherMapper {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Teacher findTeacherById(String school, String id) {
        return jdbcTemplate.queryForObject("select * from teacher where school=? and teacher_id=?", new BeanPropertyRowMapper<>(Teacher.class), school, id);

    }

    public void insertTeacher(Teacher teacher) {
        jdbcTemplate.update("insert into teacher values (?,?,?,?,?,?,?,?,?)", teacher.getSchool(), teacher.getTeacherId()
                , teacher.getName(), teacher.getPassword(), teacher.getRePassword(), teacher.getIdentity(), teacher.getTelephone(), teacher.getEmail(), teacher.getTitle());
    }

    public Teacher findTeacher(Teacher teacher) {
        return jdbcTemplate.queryForObject("select * from teacher where school=? and teacher_id=? and password=?", new BeanPropertyRowMapper<>(Teacher.class), teacher.getSchool(), teacher.getTeacherId(), teacher.getPassword());
    }

    public void updateTeacher(Teacher teacher) {
        jdbcTemplate.update("update teacher set name=?,telephone=?,email=?,title=? where school=? and teacher_id=? ", teacher.getName(), teacher.getTelephone(), teacher.getEmail(), teacher.getTitle(), teacher.getSchool(), teacher.getTeacherId());
    }

    public List<Teacher> findTeacherBySchool(String school) {
        return jdbcTemplate.query("select school,teacher_id,name,identity,telephone,email,title from teacher where school=?", new BeanPropertyRowMapper<>(Teacher.class), school);
    }

    public void deleteTeacher(String school, String teacherId) {
        jdbcTemplate.update("delete from teacher where school=? and teacher_id=?", school, teacherId);

    }

    public void updateIdentityById(String identity, String school, String teacherId) {

        jdbcTemplate.update("update teacher set identity=? where school=? and teacher_id=?", identity, school, teacherId);
    }

    public String findPasswordById(String school, String teacherId) {
        return jdbcTemplate.queryForObject("select password from teacher where school=? and teacher_id=?", String.class, school, teacherId);
    }

    public void updatePasswordById(String password, String school, String teacherId) {
        jdbcTemplate.update("update teacher set password=?,re_password=? where school=? and teacher_id=?", password, password, school, teacherId);
    }

    public List<Teacher> findTeacherByIdentityAndSchoolRandomly(int identity, String school) {
        return jdbcTemplate.query("SELECT * \n" +
                "FROM teacher \n" +
                "WHERE identity=? AND school=? \n" +
                "ORDER BY RAND() \n" +
                "LIMIT 1;", new BeanPropertyRowMapper<>(Teacher.class), identity, school);
    }

}
