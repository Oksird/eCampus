package ua.foxminded.muzychenko.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ua.foxminded.muzychenko.dao.TeacherDao;
import ua.foxminded.muzychenko.entity.Teacher;

import java.util.List;
import java.util.UUID;

@Repository
public class TeacherDaoImpl extends AbstractCrudDaoImpl<Teacher> implements TeacherDao {
    private static final String CREATE_QUERY = "INSERT INTO users VALUES (?, 'Teacher', ?, ?, ? ,? )";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name=?, last_name=?, email=?, password=? WHERE user_id=?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE user_id=?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM users WHERE user_type='Teacher'";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE user_id=?";
    private static final String FIND_BY_COURSE_QUERY = """
        SELECT u.*
        FROM public.users AS u
        JOIN public.teachers_courses AS tc ON u.user_id = tc.teacher_id
        JOIN public.courses AS c ON tc.course_id = c.course_id
        WHERE u.user_type = 'Teacher' AND c.course_name =?;
        """;
    private static final String ADD_TO_COURSE_QUERY = """
        WITH updated_teacher AS (
            UPDATE public.users
            SET course_id = (
                SELECT course_id
                FROM public.courses
                WHERE course_name =?
            )
            WHERE user_id =? AND user_type = 'Teacher'
            RETURNING user_id, course_id
        )
        INSERT INTO public.teachers_courses (teacher_id, course_id)
        SELECT user_id, course_id
        FROM updated_teacher;
        """;
    private static final String DELETE_FROM_COURSE_QUERY = """
        WITH deleted_teacher AS (
            DELETE FROM public.teachers_courses
            WHERE teacher_id = ? AND course_id = (
                SELECT course_id
                FROM public.courses
                WHERE course_name =?
            )
            RETURNING course_id
        )
        UPDATE public.users AS u
        SET course_id = NULL
        FROM deleted_teacher
        WHERE u.user_id =? AND u.user_type = 'Teacher';
        """;

    protected TeacherDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Teacher> rowMapper) {
        super(jdbcTemplate, rowMapper, CREATE_QUERY, UPDATE_QUERY, FIND_BY_ID_QUERY, FIND_ALL_QUERY, DELETE_BY_ID_QUERY);
    }

    @Override
    public List<Teacher> findByCourse(String courseName) {
        return jdbcTemplate.query(
            FIND_BY_COURSE_QUERY,
            new Object[]{courseName},
            rowMapper
        );
    }

    @Override
    public void addToCourse(UUID id, String courseName) {
        jdbcTemplate.update(
            ADD_TO_COURSE_QUERY,
            courseName,
            id
        );
    }

    @Override
    public void deleteFromCourse(UUID id, String courseName) {
        jdbcTemplate.update(
            DELETE_FROM_COURSE_QUERY,
            id,
            courseName,
            id
        );
    }

    @Override
    protected Object[] getCreateParameters(Teacher entity) {
        return new Object[]{
            entity.getUserId(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getEmail(),
            entity.getPassword()
        };
    }

    @Override
    protected Object[] getUpdateParameters(UUID id, Teacher newEntity) {
        return new Object[]{
            newEntity.getFirstName(),
            newEntity.getLastName(),
            newEntity.getEmail(),
            newEntity.getPassword(),
            id
        };
    }
}