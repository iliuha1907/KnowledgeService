package com.senla.training.knowledgeservice.dao.query;

import javax.persistence.metamodel.SingularAttribute;

public class EqualQueryHandler<X, Y> {

    private final SingularAttribute<X, Y> field;
    private final Y value;

    public EqualQueryHandler(SingularAttribute<X, Y> field, Y value) {
        this.field = field;
        this.value = value;
    }

    public SingularAttribute<X, Y> getField() {
        return field;
    }

    public Y getValue() {
        return value;
    }
}
