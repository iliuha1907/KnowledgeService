package com.senla.training.knowledgeservice.service.service.review.lesson;

import com.senla.training.knowledgeservice.common.sort.LessonReviewSortCriterion;
import com.senla.training.knowledgeservice.model.review.LessonReview;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface LessonReviewService {

    void addReview(@Nonnull LessonReview lessonReview);

    @Nonnull
    LessonReview findReviewById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<LessonReview> findReviews(
            @Nonnull LessonReviewSortCriterion criterion,
            @Nullable Integer userId,
            @Nullable Integer lessonId,
            @Nullable Date startDate,
            @Nullable Date endDate);

    void updateReview(@Nonnull LessonReview lessonReview, @Nonnull Integer id);

    void deleteReviewById(@Nonnull Integer id);
}
