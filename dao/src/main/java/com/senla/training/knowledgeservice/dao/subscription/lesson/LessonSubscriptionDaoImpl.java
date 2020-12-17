package com.senla.training.knowledgeservice.dao.subscription.lesson;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.Lesson_;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription_;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;

@Component
public class LessonSubscriptionDaoImpl
        extends AbstractDao<LessonSubscription, Integer>
        implements LessonSubscriptionDao {

    @Override
    @CheckForNull
    public LessonSubscription findSubscriptionByUserAndLesson(@Nullable User user,
                                                              @Nullable Lesson lesson) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<LessonSubscription> query = criteriaBuilder.createQuery(
                    LessonSubscription.class);
            Root<LessonSubscription> root = query.from(LessonSubscription.class);
            query
                    .select(root)
                    .where(criteriaBuilder.equal(root.get(LessonSubscription_.lesson),
                            lesson), criteriaBuilder.equal(root.get(
                            LessonSubscription_.user), user));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void deleteByCourseAndUser(@Nullable Course course, @Nullable User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<LessonSubscription> query = criteriaBuilder
                .createCriteriaDelete(LessonSubscription.class);
        Root<LessonSubscription> root = query.from(LessonSubscription.class);
        query.where(root.in(findSubscriptionsByCourseAndUser(course, user)));
        entityManager.createQuery(query).executeUpdate();
    }

    @Nonnull
    private List<LessonSubscription> findSubscriptionsByCourseAndUser(
            @Nullable Course course,
            @Nullable User user) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<LessonSubscription> query = criteriaBuilder.createQuery(
                LessonSubscription.class);
        Root<LessonSubscription> root = query.from(LessonSubscription.class);
        query
                .select(root)
                .where(criteriaBuilder.equal(root.get(LessonSubscription_.USER),
                        user), criteriaBuilder.equal(root.get(
                        LessonSubscription_.LESSON).get(Lesson_.COURSE),
                        course));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void deleteAllNotTookPlaceByTeacher(@Nullable Teacher teacher) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<LessonSubscription> query = criteriaBuilder
                .createCriteriaDelete(LessonSubscription.class);
        Root<LessonSubscription> root = query.from(LessonSubscription.class);
        query.where(criteriaBuilder.equal(root.get(LessonSubscription_.TEACHER),
                teacher), criteriaBuilder.equal(root.get(
                LessonSubscription_.tookPlace), false));
        entityManager.createQuery(query).executeUpdate();
    }

    @Override
    protected Class<LessonSubscription> getEntityClass() {
        return LessonSubscription.class;
    }
}
