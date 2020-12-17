package com.senla.training.knowledgeservice.dao.teacher;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class TeacherDaoImpl extends AbstractDao<Teacher, Integer>
        implements TeacherDao {

    @Override
    @Deprecated(since = "Deleting teachers is forbidden")
    public void delete(@Nonnull Teacher object) {
        throw new UnsupportedOperationException("Deleting teachers is forbidden");
    }

    @Override
    protected Class<Teacher> getEntityClass() {
        return Teacher.class;
    }
}
