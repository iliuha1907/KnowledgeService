package com.senla.training.knowledgeservice.dao.query;

import javax.persistence.metamodel.SingularAttribute;

public class CompareQueryHandler<X, Y extends Comparable<? super Y>> {

    private final SingularAttribute<X, Y> field;
    private final Y value;
    private final QuerySortOperation operation;

    public CompareQueryHandler(SingularAttribute<X, Y> field,
                               Y value,
                               QuerySortOperation operation) {
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    public SingularAttribute<X, Y> getField() {
        return field;
    }

    public Y getValue() {
        return value;
    }

    public QuerySortOperation getOperation() {
        return operation;
    }
}
