package com.senla.training.knowledgeservice.dto.mapper.teacher;

import com.senla.training.knowledgeservice.dto.dto.teacher.TeacherDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class TeacherMapper extends AbstractMapper<Teacher, TeacherDto> {

    @Override
    @Nonnull
    protected Class<Teacher> getEntityClass() {
        return Teacher.class;
    }

    @Override
    @Nonnull
    protected Class<TeacherDto> getDtoClass() {
        return TeacherDto.class;
    }
}
