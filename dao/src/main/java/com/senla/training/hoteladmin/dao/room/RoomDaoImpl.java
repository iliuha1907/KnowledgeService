package com.senla.training.hoteladmin.dao.room;

import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
import com.senla.training.hoteladmin.dao.HibernateAbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.Room_;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@NeedInjectionClass
public class RoomDaoImpl extends HibernateAbstractDao<Room> implements RoomDao {

    @Override
    public Class<Room> getEntityClass() {
        return Room.class;
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root);
            query.orderBy(criteriaBuilder.asc(root.get(criterion.toString().toLowerCase())));
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getFreeRooms(EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Room_.IS_FREE), 1));
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion, EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Room_.IS_FREE), 1))
                    .orderBy(criteriaBuilder.asc(root.get(criterion.toString().toLowerCase())));
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Long getNumberOfFreeRooms(EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Room> root = query.from(Room.class);
            query.select(criteriaBuilder.count(root)).where(criteriaBuilder.equal(
                    root.get(Room_.IS_FREE), 1));
            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception ex) {
            logger.error("Error at getting number of free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Room getFirstFreeRoom(EntityManager entityManager) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Room_.IS_FREE), 1));
            return entityManager.createQuery(query).setMaxResults(1).getSingleResult();
        } catch (NoResultException noResultException) {
            logger.error("Error at getting first free room: no such entity!");
            return null;
        } catch (Exception ex) {
            logger.error("Error at getting first free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }
}
