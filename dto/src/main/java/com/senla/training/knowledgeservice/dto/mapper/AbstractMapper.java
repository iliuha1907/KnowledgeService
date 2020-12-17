package com.senla.training.knowledgeservice.dto.mapper;

import com.senla.training.knowledgeservice.dto.dto.AbstractDto;
import com.senla.training.knowledgeservice.model.AbstractEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AbstractMapper<A extends AbstractEntity, B extends AbstractDto> {

    @Autowired
    private ModelMapper mapper;

    @Nonnull
    public A toEntity(@Nonnull B dto) {
        return mapper.map(dto, getEntityClass());
    }

    @Nonnull
    public B toDto(@Nonnull A entity) {
        return mapper.map(entity, getDtoClass());
    }

    @Nonnull
    public List<B> listToDto(@Nonnull List<A> entities) {
        List<B> dtos = new ArrayList<>();
        entities.forEach(entity -> {
            if (entity != null) {
                dtos.add(mapper.map(entity, getDtoClass()));
            }
        });
        return dtos;
    }

    @Nonnull
    protected abstract Class<A> getEntityClass();

    @Nonnull
    protected abstract Class<B> getDtoClass();
}
