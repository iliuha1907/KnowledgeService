package com.senla.training.knowledgeservice.service.service.section;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.SectionSortCriterion;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.section.Section_;
import com.senla.training.knowledgeservice.model.topic.Topic;
import com.senla.training.knowledgeservice.service.service.EntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private TopicDao topicDao;

    @Override
    @Transactional
    public void addSection(@Nonnull Section section) {
        checkSectionForNulls(section, EntityOperation.ADDING);
        Topic topic = topicDao.findById(section.getTopic().getId());
        if (topic == null) {
            throw new BusinessException("Error at adding section: "
                    + "no such topic for section");
        }
        section.setTopic(topic);
        sectionDao.add(section);
    }

    @Override
    @Transactional
    @Nonnull
    public Section findSectionById(@Nonnull Integer id) {
        Section section = sectionDao.findById(id);
        if (section == null) {
            throw new BusinessException("Error at finding section by id:"
                    + " no such section");
        }
        return section;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<Section> findSections(
            @Nonnull SectionSortCriterion criterion,
            @Nullable String title,
            @Nullable Integer topicId) {
        List<EqualQueryHandler<Section, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<Section, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, title, topicId);
        return sectionDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateSection(@Nonnull Section section, @Nonnull Integer id) {
        checkSectionForNulls(section, EntityOperation.UPDATING);
        if (sectionDao.findById(id) == null) {
            throw new BusinessException("Error at updating section:"
                    + " no such section");
        }
        Topic topic = topicDao.findById(section.getTopic().getId());
        if (topic == null) {
            throw new BusinessException("Error at updating section: "
                    + "no such topic for section");
        }
        section.setTopic(topic);
        section.setId(id);
        section.setId(null);
        sectionDao.update(section);
    }

    @Override
    @Transactional
    public void deleteSectionById(@Nonnull Integer id) {
        Section section = sectionDao.findById(id);
        if (section == null) {
            throw new BusinessException("Error at deleting section by id:"
                    + " no such section");
        }
        sectionDao.delete(section);
    }

    private void checkSectionForNulls(@Nonnull Section section,
                                      @Nonnull EntityOperation operation) {
        if (section.getTitle() == null) {
            throw new BusinessException("Error at " + operation
                    + " section: title is null");
        }
        if (section.getDescription() == null) {
            throw new BusinessException("Error at " + operation
                    + " section: description is null");
        }
        if (section.getTopic() == null) {
            throw new BusinessException("Error at " + operation
                    + " section: topic is null");
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<Section, Y> extractSortField(
            @Nullable SectionSortCriterion criterion) {
        if (SectionSortCriterion.TITLE.equals(criterion)) {
            return (SingularAttribute<Section, Y>) Section_.title;
        } else if (!SectionSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding sections by criterion:"
                    + " invalid criterion");
        }
        return null;
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<Section, ?>> equalParameters,
            @Nullable String title,
            @Nullable Integer topicId) {
        if (title != null) {
            equalParameters.add(new EqualQueryHandler<>(Section_.title, title));
        }
        if (topicId != null) {
            equalParameters.add(new EqualQueryHandler<>(
                    Section_.topic, topicDao.findById(topicId)));
        }
    }
}
