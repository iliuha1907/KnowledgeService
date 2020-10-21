package com.senla.training.hoteladmin.dao.user;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.user.User;
import com.senla.training.hoteladmin.model.user.User_;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
public class UserDaoImpl extends AbstractDao<User, Integer> implements UserDao {

    @Override
    public void add(User object) {
        logger.error("Error at adding entity: method is not supported");
        throw new BusinessException("Error at adding entity: method is not supported");
    }

    @Override
    public void update(User object) {
        logger.error("Error at updating entity: method is not supported");
        throw new BusinessException("Error at updating entity: method is not supported");
    }

    @Override
    public User getUserByLogin(String login) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root).where(criteriaBuilder.equal(root.get(User_.login), login));
            return entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException noResultException) {
            logger.error("Error at getting first free room: no such entity!");
            return null;
        } catch (Exception ex) {
            logger.error("Error at getting user: " + ex.getMessage());
            throw new BusinessException("Error at getting user: " + ex.getMessage());
        }
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }
}
