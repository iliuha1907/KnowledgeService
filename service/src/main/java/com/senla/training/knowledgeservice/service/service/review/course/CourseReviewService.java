package com.senla.training.knowledgeservice.service.service.review.course;

import com.senla.training.knowledgeservice.common.sort.CourseReviewSortCriterion;
import com.senla.training.knowledgeservice.model.review.CourseReview;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface CourseReviewService {

    void addReview(@Nonnull CourseReview courseReview);

    @Nonnull
    CourseReview findReviewById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<CourseReview> findReviews(
            @Nonnull CourseReviewSortCriterion criterion,
            @Nullable Integer userId,
            @Nullable Integer courseId,
            @Nullable Date startDate,
            @Nullable Date endDate);

    void updateReview(@Nonnull CourseReview courseReview, @Nonnull Integer id);

    void deleteReviewById(@Nonnull Integer id);
}
