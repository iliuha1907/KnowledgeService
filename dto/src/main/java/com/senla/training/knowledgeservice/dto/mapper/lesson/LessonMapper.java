package com.senla.training.knowledgeservice.dto.mapper.lesson;

import com.senla.training.knowledgeservice.dto.dto.lesson.LessonDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class LessonMapper extends AbstractMapper<Lesson, LessonDto> {

    @Override
    @Nonnull
    protected Class<Lesson> getEntityClass() {
        return Lesson.class;
    }

    @Override
    @Nonnull
    protected Class<LessonDto> getDtoClass() {
        return LessonDto.class;
    }
}
