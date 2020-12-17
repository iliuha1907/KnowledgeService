package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.TopicSortCriterion;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.model.topic.Topic;
import com.senla.training.knowledgeservice.service.service.topic.TopicService;
import com.senla.training.knowledgeservice.testunit.config.ServiceTestConfigurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class TopicServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Topic> topics;
    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicDao topicDao;

    @BeforeAll
    public static void setUp() {
        Topic topicOne = new Topic(1,"Title", "Topic");
        Topic topicTwo = new Topic(2,"Title", "Topic two");
        topics = Arrays.asList(topicOne, topicTwo);
    }

    @Test
    void TopicServiceImpl_addTopic() {
        Topic topic = topics.get(0);

        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Assertions.assertDoesNotThrow(() -> topicService.addTopic(topic));
    }

    @Test
    void TopicServiceImpl_addTopic_titleIsNull() {
        String message = "Error at adding topic: title is null";
        Topic topic = new Topic(1,null,"Description");

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.addTopic(topic));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_addTopic_descriptionIsNull() {
        String message = "Error at adding topic: description is null";
        Topic topic = new Topic(1,"Title",null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.addTopic(topic));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_addTopic_daoExceptionAtAdd() {
        Topic topic = topics.get(1);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(topicDao).add(topic);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> topicService.addTopic(topic));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicServiceImpl_findTopicById() {
        Topic topic = topics.get(0);
        Integer id = topic.getId();

        Mockito.doReturn(topic).when(topicDao).findById(id);
        Assertions.assertEquals(topic, topicService.findTopicById(id));
    }

    @Test
    void TopicServiceImpl_findTopicById_noTopic() {
        String message = "Error at finding topic by id: no such topic";
        Integer id = 0;

        Mockito.doReturn(null).when(topicDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.findTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_findTopicById_daoExceptionAtFindTopic() {
        String message = "Error at finding topic by id";
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(message)).when(topicDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> topicService.findTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TopicServiceImpl_findTopics() {
        Mockito.doReturn(topics).when(topicDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> topicService
                .findTopics(TopicSortCriterion.NATURAL, "Topic"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TopicServiceImpl_findTopics_PersistenceException_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> topicService.findTopics(TopicSortCriterion.NATURAL, "Topic"));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicServiceImpl_updateTopic() {
        Topic topic = topics.get(0);

        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Assertions.assertDoesNotThrow(() -> topicService.updateTopic(topic, topic.getId()));
    }

    @Test
    void TopicServiceImpl_updateTopic_noTopic() {
        String message = "Error at updating topic by id: no such topic";
        Topic topic = new Topic(1,"Title","Desc");
        Integer id = topic.getId();

        Mockito.doReturn(null).when(topicDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.updateTopic(topic, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_updateTopic_titleIsNull() {
        String message = "Error at updating topic: title is null";
        Topic topic = new Topic(1,null,"Desc");

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.updateTopic(topic, topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_updateTopic_descriptionIsNull() {
        String message = "Error at updating topic: description is null";
        Topic topic = new Topic(1,"Title",null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.updateTopic(topic, topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_updateTopic_daoExceptionAtFindTopic() {
        Topic topic = topics.get(1);
        Integer id = topic.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(topicDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> topicService.updateTopic(topic, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicServiceImpl_updateTopic_daoExceptionAtUpdate() {
        Topic topic = topics.get(1);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).update(topic);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> topicService.updateTopic(topic, topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicServiceImpl_deleteTopicById() {
        Topic topic = topics.get(0);
        Integer id = topic.getId();

        Mockito.doReturn(topic).when(topicDao).findById(id);
        Assertions.assertDoesNotThrow(() -> topicService.deleteTopicById(id));
    }

    @Test
    void TopicServiceImpl_deleteTopicById_noTopic() {
        String message = "Error at deleting topic by id: no such topic";
        Integer id = -2;

        Mockito.doReturn(null).when(topicDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicService.deleteTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_deleteTopicById_daoExceptionAtFindTopic() {
        String message = "Error at deleting topic by id: no such topic";
        Integer id = -3;

        Mockito.doThrow(new IllegalStateException(message)).when(topicDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> topicService.deleteTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_deleteTopicById_daoExceptionAtDelete() {
        Topic topic = topics.get(1);
        Integer id = topic.getId();

        Mockito.doReturn(topic).when(topicDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).delete(topic);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> topicService.deleteTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
