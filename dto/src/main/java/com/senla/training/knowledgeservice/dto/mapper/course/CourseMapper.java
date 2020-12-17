package com.senla.training.knowledgeservice.dto.mapper.course;

import com.senla.training.knowledgeservice.dto.dto.course.CourseDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.course.Course;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class CourseMapper extends AbstractMapper<Course, CourseDto> {

    @Override
    @Nonnull
    protected Class<Course> getEntityClass() {
        return Course.class;
    }

    @Override
    @Nonnull
    protected Class<CourseDto> getDtoClass() {
        return CourseDto.class;
    }
}
