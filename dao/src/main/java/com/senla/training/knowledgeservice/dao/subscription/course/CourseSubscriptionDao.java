package com.senla.training.knowledgeservice.dao.subscription.course;

import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.dao.GenericDao;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface CourseSubscriptionDao
        extends GenericDao<CourseSubscription, Integer> {

    @CheckForNull
    CourseSubscription findSubscriptionByUserAndCourse(@Nullable User user,
                                                       @Nullable Course course);

    @Override
    @Deprecated(since = "Updating course subscription is forbidden")
    void update(@Nonnull CourseSubscription object);
}
