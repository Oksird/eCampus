package ua.foxminded.muzychenko.university.service.mapper;

import lombok.AllArgsConstructor;
import ua.foxminded.muzychenko.university.config.Mapper;
import ua.foxminded.muzychenko.university.dto.profile.LessonInfo;
import ua.foxminded.muzychenko.university.entity.Lesson;

@Mapper
@AllArgsConstructor
public class LessonInfoMapper {
    private final TeacherProfileMapper teacherProfileMapper;

    public LessonInfo mapLessonEntityToLessonInfo(Lesson lesson) {
        return new LessonInfo(
            lesson.getCourse().getCourseName(),
            teacherProfileMapper.mapTeacherEntityToProfile(lesson.getTeacher(), lesson.getTeacher().getCourses()),
            lesson.getGroup().getGroupName(),
            lesson.getDate(),
            lesson.getStartTime(),
            lesson.getEndTime());
    }
}