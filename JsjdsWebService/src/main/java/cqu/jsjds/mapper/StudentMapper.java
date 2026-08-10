package cqu.jsjds.mapper;

import cqu.jsjds.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentMapper {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Student findStudentById(String school, String id) {
        return jdbcTemplate.queryForObject("select * from student where school=? and student_id=?", new BeanPropertyRowMapper<>(Student.class), school, id);

    }

    public void insertStudent(Student student) {
        jdbcTemplate.update("insert into student values (?,?,?,?,?,?,?,?,?,?,?,?,?)", student.getSchool(), student.getStudentId(), student.getPassword()
                , student.getName(), student.getEduBackground(), student.getMajor(), student.getEnterYear(), student.getGrade(), student.getRePassword(), student.getTelephone(), student.getEmail(), student.getGroupName(), student.getApplyGroupName());
    }

    public Student findStudent(Student student) {
        return jdbcTemplate.queryForObject("select * from student where school=? and student_id=? and password=?", new BeanPropertyRowMapper<>(Student.class), student.getSchool(), student.getStudentId(), student.getPassword());
    }

    public void updateStudent(Student student) {
        jdbcTemplate.update("update student set name=?,major=?,enter_year=?, edu_background=?,grade=?,email=?,telephone=? where school=? and student_id=? ", student.getName(), student.getMajor(), student.getEnterYear(), student.getEduBackground(), student.getGrade(), student.getEmail(), student.getTelephone(), student.getSchool(), student.getStudentId());
    }

    public void updateApplyGroupNameById(String applyGroupName, String school, String id){
        jdbcTemplate.update("update student set apply_group_name=? where school=? and student_id=?",applyGroupName,school,id);
    }

    public List<Student> findStudentByGroupOfTeacher(String school, String teacherId){
        return  jdbcTemplate.query("select * from student join `group` g on g.group_name = student.apply_group_name where g.school=? and g.teacherId=?",new BeanPropertyRowMapper<>(Student.class),school,teacherId);
    }



    public void updateStudentWithGroupName(String groupName, String school, String id) {
        jdbcTemplate.update("update student set group_name=? where school=? and student_id=?", groupName, school, id);
    }

    public List<Student> findStudentByGroupName(String groupName) {
        return jdbcTemplate.query("select school, student_id, name, major,grade,telephone from student where group_name=?", new BeanPropertyRowMapper<>(Student.class), groupName);
    }

    public String findPasswordById(String school, String studentId) {
        return jdbcTemplate.queryForObject("select password from student where school=? and student_id=?", String.class, school, studentId);
    }

    public void updatePasswordById(String password, String school, String studentId) {
        jdbcTemplate.update("update student set password=?,re_password=? where school=? and student_id=?", password, password, school, studentId);
    }

}
