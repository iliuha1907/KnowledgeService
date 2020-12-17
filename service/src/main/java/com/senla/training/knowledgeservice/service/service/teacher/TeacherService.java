package com.senla.training.knowledgeservice.service.service.teacher;

import com.senla.training.knowledgeservice.common.sort.TeacherSortCriterion;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.model.teacher.Teacher;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;

public interface TeacherService {

    void addTeacher(@Nonnull Teacher teacher);

    @Nonnull
    Teacher findTeacherById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<Teacher> findTeachers(
            @Nonnull TeacherSortCriterion criterion,
            @Nullable BigDecimal startReward,
            @Nullable BigDecimal endReward,
            @Nullable RewardType type,
            @Nullable Boolean active);

    void updateTeacher(@Nonnull Teacher teacher, @Nonnull Integer id);
}
