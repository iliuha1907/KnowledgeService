package com.senla.training.knowledgeservice.service.service.topic;

import com.senla.training.knowledgeservice.common.sort.TopicSortCriterion;
import com.senla.training.knowledgeservice.model.topic.Topic;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface TopicService {

    void addTopic(@Nonnull Topic topic);

    @Nonnull
    Topic findTopicById(@Nonnull Integer id);

    @Nonnull
    <Y extends Comparable<? super Y>> List<Topic> findTopics(
            @Nonnull TopicSortCriterion criterion,
            @Nullable String title);

    void updateTopic(@Nonnull Topic topic, @Nonnull Integer id);

    void deleteTopicById(@Nonnull Integer id);
}
