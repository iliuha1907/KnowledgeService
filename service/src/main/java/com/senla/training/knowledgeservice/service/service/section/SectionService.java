package com.senla.training.knowledgeservice.service.service.section;

import com.senla.training.knowledgeservice.common.sort.SectionSortCriterion;
import com.senla.training.knowledgeservice.model.section.Section;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface SectionService {

    void addSection(@Nonnull Section section);

    @Nonnull
    Section findSectionById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<Section> findSections(
            @Nonnull SectionSortCriterion criterion,
            @Nullable String title,
            @Nullable Integer topicId);

    void updateSection(@Nonnull Section section, @Nonnull Integer id);

    void deleteSectionById(@Nonnull Integer id);
}
