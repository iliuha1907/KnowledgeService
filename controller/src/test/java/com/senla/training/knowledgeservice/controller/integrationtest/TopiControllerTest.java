package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.TopicSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.TopicController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.topic.TopicDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.topic.TopicMapper;
import com.senla.training.knowledgeservice.model.topic.Topic;
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
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class TopiControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private final TopicDto topicDtoForAll = new TopicDto();
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private TopicController topicController;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    private static List<Topic> topics;
    private static List<TopicDto> topicDtos;

    @BeforeAll
    public static void setUp() {
        Topic topicOne = new Topic(1,"Title", "Topic");
        Topic topicTwo = new Topic(2,"Title", "Topic two");
        topics = Arrays.asList(topicOne, topicTwo);
        topicDtos = Arrays.asList(new TopicDto(), new TopicDto());
    }

    @Test
    void TopicController_addTopic() {
        Topic topic = topics.get(0);

        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added topic");
        Assertions.assertEquals(messageDtoForAll, topicController.addTopic(topicDtoForAll));
    }

    @Test
    void TopicController_addTopic_titleIsNull() {
        String message = "Error at adding topic: title is null";
        Topic topic = new Topic(1,null,"Description");

        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicController.addTopic(topicDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicController_addTopic_descriptionIsNull() {
        String message = "Error at adding topic: description is null";
        Topic topic = new Topic(1,"Title",null);

        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicController.addTopic(topicDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicController_addTopic_daoExceptionAtAdd() {
        Topic topic = topics.get(1);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(topicDao).add(topic);
        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> topicController.addTopic(topicDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicController_findTopicById() {
        Topic topic = topics.get(0);

        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doReturn(topicDtoForAll).when(topicMapper).toDto(topic);
        Assertions.assertEquals(topicDtoForAll, topicController.findTopicById(topic.getId()));
    }

    @Test
    void TopicController_findTopicById_noTopic() {
        String message = "Error at finding topic by id: no such topic";
        Topic topic = topics.get(0);
        Integer id = topic.getId();

        Mockito.doReturn(null).when(topicDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicController.findTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicController_findTopicById_daoExceptionAtFind() {
        Topic topic = topics.get(0);
        Integer id = topic.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> topicController.findTopicById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TopicController_findTopics() {
        Mockito.doReturn(topics).when(topicDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Mockito.doReturn(topicDtos).when(topicMapper).listToDto(topics);
        Assertions.assertIterableEquals(topicDtos, topicController
                .findTopics(TopicSortCriterion.NATURAL, "Topic"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TopicController_findTopics_PersistenceException_daoExceptionAtFind() {
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> topicController.findTopics(TopicSortCriterion.NATURAL, "Topic"));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicController_updateTopic() {
        Topic topic = topics.get(0);

        Mockito.doReturn(messageDtoForAll).when(messageDtoMapper).toDto("Successfully" +
                " updated topic");
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        Assertions.assertEquals(messageDtoForAll,
                topicController.updateTopic(topicDtoForAll, topic.getId()));
    }

    @Test
    void TopicController_updateTopic_titleIsNull() {
        String message = "Error at updating topic: title is null";
        Topic topic = new Topic(1,null,"Desc");

        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> topicController.updateTopic(topicDtoForAll, topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicController_updateTopic_descriptionIsNull() {
        String message = "Error at updating topic: description is null";
        Topic topic = new Topic(1,"Title",null);

        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> topicController.updateTopic(topicDtoForAll, topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicController_updateTopic_noTopic() {
        String message = "Error at updating topic by id: no such topic";
        Topic topic = new Topic(1,"Title","Desc");
        Integer id =topic.getId();

        Mockito.doReturn(null).when(topicDao).findById(id);
        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicController.updateTopic(topicDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicController_updateTopic_daoExceptionAtFindTopic() {
        Topic topic = topics.get(0);
        Integer id =topic.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(topicDao)
                .findById(id);
        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> topicController.updateTopic(topicDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicController_updateTopic_daoExceptionAtUpdate() {
        Topic topic = topics.get(0);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(topicDao)
                .update(topic);
        Mockito.doReturn(topic).when(topicMapper).toEntity(topicDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> topicController.updateTopic(topicDtoForAll, topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicController_deleteTopicById() {
        Topic topic = topics.get(0);

        Mockito.doReturn(messageDtoForAll).when(messageDtoMapper).toDto("Successfully"
                + " deleted topic");
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Assertions.assertEquals(messageDtoForAll, topicController.deleteTopicById(topic.getId()));
    }

    @Test
    void TopicServiceImpl_deleteTopicById_noTopic() {
        String message = "Error at deleting topic by id: no such topic";
        Topic topic = topics.get(0);

        Mockito.doReturn(null).when(topicDao).findById(topic.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> topicController.deleteTopicById(topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TopicServiceImpl_deleteTopicById_daoExceptionAtFind() {
        Topic topic = topics.get(1);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(topic.getId());
     IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> topicController.deleteTopicById(topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TopicServiceImpl_deleteTopicById_daoExceptionAtDelete() {
        Topic topic = topics.get(1);

        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(topicDao)
                .delete(topic);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> topicController.deleteTopicById(topic.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
