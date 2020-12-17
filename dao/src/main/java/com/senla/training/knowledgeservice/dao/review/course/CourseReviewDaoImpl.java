package com.senla.training.knowledgeservice.dao.review.course;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import org.springframework.stereotype.Component;

@Component
public class CourseReviewDaoImpl extends AbstractDao<CourseReview, Integer>
        implements CourseReviewDao {

    @Override
    protected Class<CourseReview> getEntityClass() {
        return CourseReview.class;
    }
}
