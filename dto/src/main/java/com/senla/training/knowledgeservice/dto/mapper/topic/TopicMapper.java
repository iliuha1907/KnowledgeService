package com.senla.training.knowledgeservice.dto.mapper.topic;

import com.senla.training.knowledgeservice.dto.dto.topic.TopicDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.topic.Topic;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class TopicMapper extends AbstractMapper<Topic, TopicDto> {

    @Override
    @Nonnull
    protected Class<Topic> getEntityClass() {
        return Topic.class;
    }

    @Override
    @Nonnull
    protected Class<TopicDto> getDtoClass() {
        return TopicDto.class;
    }
}
