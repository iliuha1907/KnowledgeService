package com.senla.training.knowledgeservice.dao.subscription.lesson;

import com.senla.training.knowledgeservice.dao.GenericDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;
import com.senla.training.knowledgeservice.model.teacher.Teacher;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;

public interface LessonSubscriptionDao
        extends GenericDao<LessonSubscription, Integer> {

    @CheckForNull
    LessonSubscription findSubscriptionByUserAndLesson(@Nullable User user, @Nullable Lesson lesson);

    void deleteByCourseAndUser(@Nullable Course course, @Nullable User user);

    void deleteAllNotTookPlaceByTeacher(@Nullable Teacher teacher);
}
