package com.senla.training.hoteladmin.dao.room;

import com.senla.training.hoteladmin.dao.AbstractDao;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.model.room.Room;
import com.senla.training.hoteladmin.model.room.Room_;
import com.senla.training.hoteladmin.util.sort.RoomsSortCriterion;
import org.springframework.stereotype.Component;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Component
public class RoomDaoImpl extends AbstractDao<Room> implements RoomDao {

    @Override
    public Class<Room> getEntityClass() {
        return Room.class;
    }

    @Override
    public List<Room> getSortedRooms(RoomsSortCriterion criterion) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root);
            if (criterion.equals(RoomsSortCriterion.CAPACITY)) {
                query.orderBy(criteriaBuilder.asc(root.get(Room_.capacity)));
            } else if (criterion.equals(RoomsSortCriterion.PRICE)) {
                query.orderBy(criteriaBuilder.asc(root.get(Room_.price)));
            } else if (criterion.equals(RoomsSortCriterion.STARS)) {
                query.orderBy(criteriaBuilder.asc(root.get(Room_.stars)));
            }
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getFreeRooms() {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Room_.isFree), 1));
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<Room> getSortedFreeRooms(RoomsSortCriterion criterion) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Room_.isFree), 1));
            if (criterion.equals(RoomsSortCriterion.CAPACITY)) {
                query.orderBy(criteriaBuilder.asc(root.get(Room_.capacity)));
            } else if (criterion.equals(RoomsSortCriterion.PRICE)) {
                query.orderBy(criteriaBuilder.asc(root.get(Room_.price)));
            } else if (criterion.equals(RoomsSortCriterion.STARS)) {
                query.orderBy(criteriaBuilder.asc(root.get(Room_.stars)));
            }
            return entityManager.createQuery(query).getResultList();
        } catch (Exception ex) {
            logger.error("Error at getting sorted free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Long getNumberOfFreeRooms() {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
            Root<Room> root = query.from(Room.class);
            query.select(criteriaBuilder.count(root)).where(criteriaBuilder.equal(
                    root.get(Room_.isFree), 1));
            return entityManager.createQuery(query).getSingleResult();
        } catch (Exception ex) {
            logger.error("Error at getting number of free rooms: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public Room getFirstFreeRoom() {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Room> query = criteriaBuilder.createQuery(Room.class);
            Root<Room> root = query.from(Room.class);
            query.select(root).where(criteriaBuilder.equal(root.get(Room_.isFree), 1));
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
