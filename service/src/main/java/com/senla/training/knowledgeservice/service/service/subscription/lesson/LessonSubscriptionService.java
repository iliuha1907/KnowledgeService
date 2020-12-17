package com.senla.training.knowledgeservice.service.service.subscription.lesson;

import com.senla.training.knowledgeservice.common.sort.LessonSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

public interface LessonSubscriptionService {

    void addSubscription(@Nonnull LessonSubscription lessonSubscription);

    @Nonnull
    LessonSubscription findSubscriptionById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<LessonSubscription> findSubscriptions(
            @Nonnull LessonSubscriptionSortCriterion criterion,
            @Nullable Date startDate,
            @Nullable Date endDate,
            @Nullable Integer userId,
            @Nullable Integer teacherId,
            @Nullable Integer lessonId,
            @Nullable Boolean tookPlace);

    void updateSubscription(@Nonnull LessonSubscription lessonSubscription,
                            @Nonnull Integer id);

    void deleteSubscriptionById(@Nonnull Integer id);
}
