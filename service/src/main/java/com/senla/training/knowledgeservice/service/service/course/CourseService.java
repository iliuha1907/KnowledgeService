package com.senla.training.knowledgeservice.service.service.course;

import com.senla.training.knowledgeservice.common.sort.CourseSortCriterion;
import com.senla.training.knowledgeservice.model.course.Course;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

public interface CourseService {

    void addCourse(@Nonnull Course course);

    @Nonnull
    Course findCourseById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<Course> findCourses(
            @Nonnull CourseSortCriterion criterion,
            @Nullable String title,
            @Nullable BigDecimal startPrice,
            @Nullable BigDecimal endPrice,
            @Nullable Integer startDuration,
            @Nullable Integer endDuration,
            @Nullable Integer sectionId);

    void updateCourse(@Nonnull Course course, @Nonnull Integer id);

    void deleteCourseById(@Nonnull Integer id);
}
