package com.senla.training.knowledgeservice.dto.mapper.review;

import com.senla.training.knowledgeservice.dto.dto.review.LessonReviewDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.review.LessonReview;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class LessonReviewMapper extends AbstractMapper<LessonReview, LessonReviewDto> {

    @Override
    @Nonnull
    protected Class<LessonReview> getEntityClass() {
        return LessonReview.class;
    }

    @Override
    @Nonnull
    protected Class<LessonReviewDto> getDtoClass() {
        return LessonReviewDto.class;
    }
}
