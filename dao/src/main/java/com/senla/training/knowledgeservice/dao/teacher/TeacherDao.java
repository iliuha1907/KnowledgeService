package com.senla.training.knowledgeservice.dao.teacher;

import com.senla.training.knowledgeservice.dao.GenericDao;
import com.senla.training.knowledgeservice.model.teacher.Teacher;

import javax.annotation.Nonnull;

public interface TeacherDao extends GenericDao<Teacher, Integer> {

    @Override
    @Deprecated(since = "Deleting teachers is forbidden")
    void delete(@Nonnull Teacher object);
}
