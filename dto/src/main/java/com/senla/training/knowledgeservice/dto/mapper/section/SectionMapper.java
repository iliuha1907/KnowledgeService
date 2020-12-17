package com.senla.training.knowledgeservice.dto.mapper.section;

import com.senla.training.knowledgeservice.dto.dto.section.SectionDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.section.Section;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class SectionMapper extends AbstractMapper<Section, SectionDto> {

    @Override
    @Nonnull
    protected Class<Section> getEntityClass() {
        return Section.class;
    }

    @Override
    @Nonnull
    protected Class<SectionDto> getDtoClass() {
        return SectionDto.class;
    }
}
