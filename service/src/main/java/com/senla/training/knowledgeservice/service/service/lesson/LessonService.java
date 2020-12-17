package com.senla.training.knowledgeservice.service.service.lesson;

import com.senla.training.knowledgeservice.common.sort.LessonSortCriterion;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface LessonService {

    void addLesson(@Nonnull Lesson lesson);

    @Nonnull
    Lesson findLessonById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<Lesson> findLessons(
            @Nonnull LessonSortCriterion criterion,
            @Nullable LessonType type,
            @Nullable String title,
            @Nullable Integer courseId);

    void updateLesson(@Nonnull Lesson lesson, @Nonnull Integer id);

    void deleteLessonById(@Nonnull Integer id);
}
