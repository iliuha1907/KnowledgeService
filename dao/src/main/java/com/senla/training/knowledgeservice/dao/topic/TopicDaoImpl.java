package com.senla.training.knowledgeservice.dao.topic;

import com.senla.training.knowledgeservice.dao.AbstractDao;
import com.senla.training.knowledgeservice.model.topic.Topic;
import org.springframework.stereotype.Component;

@Component
public class TopicDaoImpl extends AbstractDao<Topic, Integer> implements TopicDao {

    @Override
    protected Class<Topic> getEntityClass() {
        return Topic.class;
    }
}
