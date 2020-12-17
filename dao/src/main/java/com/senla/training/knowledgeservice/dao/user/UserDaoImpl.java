package com.senla.training.knowledgeservice.dao.user;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.user.User_;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class UserDaoImpl extends AbstractDao<User, Integer> implements UserDao {

    @Override
    @CheckForNull
    public User findUserByLogin(@Nullable String login) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query
                    .select(root)
                    .where(criteriaBuilder.equal(root.get(User_.login), login));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException noResultException) {
            return null;
        }
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
