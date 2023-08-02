package ua.foxminded.muzychenko.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.CourseDao;
import ua.foxminded.muzychenko.entity.Course;

import java.util.UUID;

@Repository
public class CourseDaoImpl extends AbstractCrudDaoImpl<Course> implements CourseDao {

    private static final String CREATE_QUERY = "INSERT INTO courses VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE courses SET course_name=?, course_description=? WHERE course_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM courses WHERE course_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM courses";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM courses WHERE course_id=?";

    @Autowired
    protected CourseDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Course> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    protected Object[] getCreateParameters(Course course) {
        return new Object[]{course.getCourseId(), course.getCourseName(), course.getCourseDescription()};
    }

    @Override
    protected Object[] getUpdateParameters(UUID id, Course updatedCourse) {
        return new Object[]{updatedCourse.getCourseName(), updatedCourse.getCourseDescription(), id};
    }
}
