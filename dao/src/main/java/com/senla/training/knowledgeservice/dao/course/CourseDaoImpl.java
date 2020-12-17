package com.senla.training.knowledgeservice.dao.course;

import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.dao.AbstractDao;
import org.springframework.stereotype.Component;

@Component
public class CourseDaoImpl extends AbstractDao<Course, Integer> implements CourseDao {

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }
}
