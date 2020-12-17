package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.SectionSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.SectionController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.section.SectionDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.section.SectionMapper;
import com.senla.training.knowledgeservice.model.section.Section;
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
public class SectionControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Section> sections;
    private static List<SectionDto> sectionDtos;
    private static SectionDto sectionDtoForAll;
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private SectionController sectionController;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private TopicDao topicDao;
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Topic topicOne = new Topic(1, "Title", "Topic");
        Topic topicTwo = new Topic(2, "Title", "Topic two");
        Section sectionOne = new Section(1, "Title", "Section", topicOne);
        Section sectionTwo = new Section(2, "Title", "Section two", topicTwo);
        sections = Arrays.asList(sectionOne, sectionTwo);
        sectionDtos = Arrays.asList(new SectionDto(), new SectionDto());
        sectionDtoForAll = new SectionDto();
    }

    @Test
    void SectionController_addSection() {
        Section section = sections.get(0);
        Topic topic = section.getTopic();

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added section");
        Assertions.assertEquals(messageDtoForAll, sectionController.addSection(sectionDtoForAll));
    }

    @Test
    void SectionController_addSection_titleIsNull() {
        String message = "Error at adding section: title is null";
        Section section = new Section(null, "Section", new Topic());

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.addSection(sectionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_addSection_descriptionIsNull() {
        String message = "Error at adding section: description is null";
        Section section = new Section("Title", null, new Topic());

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.addSection(sectionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_addSection_topicIsNull() {
        String message = "Error at adding section: topic is null";
        Section section = new Section("Title", "Section", null);

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.addSection(sectionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_addSection_noTopic() {
        String message = "Error at adding section: no such topic for section";
        Section section = sections.get(0);

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doReturn(null).when(topicDao).findById(section.getTopic().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.addSection(sectionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_addSection_daoExceptionAtFind() {
        Section section = sections.get(0);

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(section.getTopic().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionController.addSection(sectionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_addSection_PersistenceException_daoExceptionAtAdd() {
        Section section = sections.get(1);
        Topic topic = section.getTopic();

        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(sectionDao)
                .add(section);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> sectionController.addSection(sectionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_findSectionById() {
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(sectionDtoForAll).when(sectionMapper).toDto(section);
        Mockito.doReturn(section).when(sectionDao).findById(id);
        Assertions.assertEquals(sectionDtoForAll, sectionController.findSectionById(id));
    }

    @Test
    void SectionController_findSectionById_noSection() {
        String message = "Error at finding section by id: no such section";
        Integer id = 0;

        Mockito.doReturn(null).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.findSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_findSectionById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(sectionDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionController.findSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void SectionController_findSections() {
        Mockito.doReturn(sectionDtos).when(sectionMapper).listToDto(sections);
        Mockito.doReturn(sections).when(sectionDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> sectionController
                .findSections(SectionSortCriterion.NATURAL, "Section", null));
    }

    @Test
    void SectionServiceImpl_findSections_daoExceptionAtFindTopic() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> sectionController.findSections(SectionSortCriterion.NATURAL, "Section",
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void SectionController_findSections_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> sectionController.findSections(SectionSortCriterion.NATURAL, "Section",
                        null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_updateSection() {
        Section section = sections.get(0);
        Topic topic = section.getTopic();
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated section");
        Assertions.assertEquals(messageDtoForAll,
                sectionController.updateSection(sectionDtoForAll, id));
    }

    @Test
    void SectionController_updateSection_titleIsNull() {
        String message = "Error at updating section: title is null";
        Section section = new Section(1, null, "Section", new Topic());
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_updateSection_descriptionIsNull() {
        String message = "Error at updating section: description is null";
        Section section = new Section(1, "Title", null, new Topic());
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_updateSection_topicIsNull() {
        String message = "Error at updating section: topic is null";
        Section section = new Section(1, "Title", "Section", null);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_updateSection_noTopic() {
        String message = "Error at updating section: no such topic for section";
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doReturn(null).when(topicDao).findById(section.getTopic().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_updateSection_noSection() {
        String message = "Error at updating section: no such section";
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(null).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.updateSection(sectionDtoForAll,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_updateSection_daoExceptionAtFindSection() {
        Section section = sections.get(1);
        Integer id = section.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_updateSection_daoExceptionAtFindTopic() {
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(section.getTopic().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_updateSection_daoExceptionAtUpdate() {
        Section section = sections.get(0);
        Topic topic = section.getTopic();
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(section).when(sectionMapper).toEntity(sectionDtoForAll);
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).update(section);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> sectionController.updateSection(sectionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_deleteSectionById() {
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully deleted section");
        Assertions.assertEquals(messageDtoForAll, sectionController.deleteSectionById(id));
    }

    @Test
    void SectionController_deleteSectionById_noSection() {
        String message = "Error at deleting section by id: no such section";
        Integer id = 0;

        Mockito.doReturn(null).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionController.deleteSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionController_deleteSectionById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(sectionDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionController.deleteSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionController_deleteSectionById_daoExceptionAtDelete() {
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(sectionDao)
                .delete(section);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> sectionController.deleteSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
