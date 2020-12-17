package com.senla.training.knowledgeservice.dao;

import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.model.AbstractEntity;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

public interface GenericDao<T extends AbstractEntity, PK> {

    void add(@Nonnull T object);

    @Nonnull
    List<T> findAll();

    @Nonnull
    <Y extends Comparable<? super Y>> List<T> findSortedEntities(
            @Nullable SingularAttribute<T, Y> sortField,
            @Nonnull List<EqualQueryHandler<T, ?>> equalParameters,
            @Nonnull List<CompareQueryHandler<T, Y>> compareParameters);

    @CheckForNull
    T findById(@Nonnull PK key);

    void update(@Nonnull T object);

    void delete(@Nonnull T object);
}
