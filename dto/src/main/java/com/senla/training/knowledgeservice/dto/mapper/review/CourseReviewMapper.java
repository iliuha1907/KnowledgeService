package com.senla.training.knowledgeservice.dto.mapper.review;

import com.senla.training.knowledgeservice.dto.dto.review.CourseReviewDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class CourseReviewMapper extends AbstractMapper<CourseReview, CourseReviewDto> {

    @Override
    @Nonnull
    protected Class<CourseReview> getEntityClass() {
        return CourseReview.class;
    }

    @Override
    @Nonnull
    protected Class<CourseReviewDto> getDtoClass() {
        return CourseReviewDto.class;
    }
}
