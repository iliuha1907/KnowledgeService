package com.senla.training.knowledgeservice.dao.subscription.course;

import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription_;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class CourseSubscriptionDaoImpl
        extends AbstractDao<CourseSubscription, Integer>
        implements CourseSubscriptionDao {

    @Override
    @CheckForNull
    public CourseSubscription findSubscriptionByUserAndCourse(@Nullable User user,
                                                              @Nullable Course course) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<CourseSubscription> query = criteriaBuilder.createQuery(
                    CourseSubscription.class);
            Root<CourseSubscription> root = query.from(CourseSubscription.class);
            query.select(root);
            query.where(criteriaBuilder.equal(root.get(CourseSubscription_.course),
                    course),
                    criteriaBuilder.equal(root.get(CourseSubscription_.user), user));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    @Deprecated(since = "Updating course subscription is forbidden")
    public void update(@Nonnull CourseSubscription object) {
        throw new UnsupportedOperationException("Updating course"
                + " subscription is forbidden");
    }

    @Override
    protected Class<CourseSubscription> getEntityClass() {
        return CourseSubscription.class;
    }
}
