package com.senla.training.knowledgeservice.dao;

import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.model.AbstractEntity;
import org.springframework.stereotype.Component;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AbstractDao<T extends AbstractEntity, PK> implements GenericDao<T, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

    @Override
    public void add(@Nonnull T object) {
        entityManager.persist(object);
    }

    @Override
    @Nonnull
    public List<T> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());
        query.select(root);
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @Nonnull
    public <Y extends Comparable<? super Y>> List<T> findSortedEntities(
            @Nullable SingularAttribute<T, Y> sortField,
            @Nonnull List<EqualQueryHandler<T, ?>> equalParameters,
            @Nonnull List<CompareQueryHandler<T, Y>> compareParameters) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());
        query.select(root);
        if (sortField != null) {
            query.orderBy(criteriaBuilder.asc(root.get(sortField)));
        }
        List<Predicate> predicates = new ArrayList<>();
        addEqualParametersToPredicates(predicates, equalParameters, criteriaBuilder, root);
        addCompareParametersToPredicates(predicates, compareParameters, criteriaBuilder, root);
        query.where(predicates.toArray(Predicate[]::new));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    @CheckForNull
    public T findById(@Nonnull PK key) {
        return entityManager.find(getEntityClass(), key);
    }

    @Override
    public void update(@Nonnull T object) {
        entityManager.merge(object);
    }

    @Override
    public void delete(@Nonnull T object) {
        entityManager.remove(object);
    }

    protected abstract Class<T> getEntityClass();

    private void addEqualParametersToPredicates(
            @Nonnull List<Predicate> predicates,
            @Nonnull List<EqualQueryHandler<T, ?>> equalParameters,
            @Nonnull CriteriaBuilder criteriaBuilder,
            @Nonnull Root<T> root) {
        for (EqualQueryHandler<T, ?> parameter : equalParameters) {
            if (parameter != null) {
                if (parameter.getField() != null) {
                    predicates.add(criteriaBuilder.equal(root.get(
                            parameter.getField()), parameter.getValue()));
                }
            }
        }
    }

    private <Y extends Comparable<? super Y>> void addCompareParametersToPredicates(
            @Nonnull List<Predicate> predicates,
            @Nonnull List<CompareQueryHandler<T, Y>> compareParameters,
            @Nonnull CriteriaBuilder criteriaBuilder,
            @Nonnull Root<T> root) {
        for (CompareQueryHandler<T, Y> parameter : compareParameters) {
            if (parameter != null) {
                if (parameter.getField() != null) {
                    QuerySortOperation operation = parameter.getOperation();
                    SingularAttribute<T, Y> field = parameter.getField();
                    if (QuerySortOperation.GREATER.equals(operation)) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                                root.get(field), parameter.getValue()));
                    } else if (QuerySortOperation.LESS.equals(operation)) {
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(
                                root.get(field), parameter.getValue()));
                    }
                }
            }
        }
    }
}
