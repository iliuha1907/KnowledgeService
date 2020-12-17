package com.senla.training.knowledgeservice.dao.lesson;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonDaoImpl extends AbstractDao<Lesson, Integer> implements LessonDao {

    @Override
    protected Class<Lesson> getEntityClass() {
        return Lesson.class;
    }
}
