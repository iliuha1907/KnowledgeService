package com.senla.training.knowledgeservice.service.service.topic;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.TopicSortCriterion;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.model.topic.Topic;
import com.senla.training.knowledgeservice.model.topic.Topic_;
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
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;

    @Override
    @Transactional
    public void addTopic(@Nonnull Topic topic) {
        checkTopicForNulls(topic, EntityOperation.ADDING);
        topic.setId(null);
        topicDao.add(topic);
    }

    @Override
    @Transactional
    @Nonnull
    public Topic findTopicById(@Nonnull Integer id) {
        Topic topic = topicDao.findById(id);
        if (topic == null) {
            throw new BusinessException("Error at finding topic by id:"
                    + " no such topic");
        }
        return topic;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<Topic> findTopics(
            @Nonnull TopicSortCriterion criterion,
            @Nullable String title) {
        List<EqualQueryHandler<Topic, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<Topic, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, title);
        return topicDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateTopic(@Nonnull Topic topic, @Nonnull Integer id) {
        checkTopicForNulls(topic, EntityOperation.UPDATING);
        if (topicDao.findById(id) == null) {
            throw new BusinessException("Error at updating topic by id:"
                    + " no such topic");
        }
        topic.setId(id);
        topicDao.update(topic);
    }

    @Override
    @Transactional
    public void deleteTopicById(@Nonnull Integer id) {
        Topic topic = topicDao.findById(id);
        if (topic == null) {
            throw new BusinessException("Error at deleting topic by id:"
                    + " no such topic");
        }
        topicDao.delete(topic);
    }

    private void checkTopicForNulls(@Nonnull Topic topic,
                                    @Nonnull EntityOperation operation) {
        if (topic.getTitle() == null) {
            throw new BusinessException("Error at " + operation
                    + " topic: title is null");
        }
        if (topic.getDescription() == null) {
            throw new BusinessException("Error at " + operation
                    + " topic: description is null");
        }
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<Topic, ?>> equalParameters,
            @Nullable String title) {
        if (title != null) {
            equalParameters.add(new EqualQueryHandler<>(Topic_.title, title));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<Topic, Y> extractSortField(
            @Nullable TopicSortCriterion criterion) {
        if (TopicSortCriterion.TITLE.equals(criterion)) {
            return (SingularAttribute<Topic, Y>) Topic_.title;
        } else if (!TopicSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding topics by criterion:"
                    + " invalid criterion");
        }
        return null;
    }
}
