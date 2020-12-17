package com.senla.training.knowledgeservice.dao.review.lesson;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.review.LessonReview;
import org.springframework.stereotype.Component;

@Component
public class LessonReviewDaoImpl extends AbstractDao<LessonReview, Integer>
        implements LessonReviewDao {

    @Override
    protected Class<LessonReview> getEntityClass() {
        return LessonReview.class;
    }
}
