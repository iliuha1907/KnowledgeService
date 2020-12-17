package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.SectionSortCriterion;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.topic.Topic;
import com.senla.training.knowledgeservice.service.service.section.SectionService;
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
public class SectionServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating "
            + "with entity";
    private static List<Section> sections;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private TopicDao topicDao;

    @BeforeAll
    public static void setUp() {
        Topic topicOne = new Topic(1, "Title", "Topic");
        Topic topicTwo = new Topic(2, "Title", "Topic two");
        Section sectionOne = new Section(1, "Title", "Section",
                topicOne);
        Section sectionTwo = new Section(2, "Title", "Section two",
                topicTwo);
        sections = Arrays.asList(sectionOne, sectionTwo);
    }

    @Test
    void SectionServiceImpl_addSection() {
        Section section = sections.get(0);
        Topic topic = section.getTopic();

        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Assertions.assertDoesNotThrow(() -> sectionService.addSection(section));
    }

    @Test
    void SectionServiceImpl_addSection_titleIsNull() {
        String message = "Error at adding section: title is null";
        Section section = new Section(1, null, "Section",
                new Topic());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.addSection(section));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_addSection_descriptionIsNull() {
        String message = "Error at adding section: description is null";
        Section section = new Section(1, "Title", null, new Topic());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.addSection(section));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_addSection_topicIsNull() {
        String message = "Error at adding section: topic is null";
        Section section = new Section(1, "Title", "Section",
                null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.addSection(section));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_addSection_noTopic() {
        String message = "Error at adding section: no such topic for section";
        Section section = sections.get(0);

        Mockito.doReturn(null).when(topicDao).findById(section.getTopic()
                .getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.addSection(section));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_addSection_daoExceptionAtFindTopic() {
        Section section = sections.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(section.getTopic().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionService.addSection(section));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_addSection_daoExceptionAtAdd() {
        Section section = sections.get(1);
        Topic topic = section.getTopic();

        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).add(section);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> sectionService.addSection(section));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_findSectionById() {
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Assertions.assertEquals(section, sectionService.findSectionById(id));
    }

    @Test
    void SectionServiceImpl_findSectionById_noSection() {
        String message = "Error at finding section by id: no such section";
        Integer id = 0;

        Mockito.doReturn(null).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.findSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_findSectionById_daoExceptionAtFind() {
        String message = "Error at finding section by id";
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(message)).when(sectionDao).findById(
                id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionService.findSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    @SuppressWarnings("unchecked")
    void SectionServiceImpl_findSections() {
        Mockito.doReturn(sections).when(sectionDao).findSortedEntities(Matchers.eq(
                null), Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> sectionService
                .findSections(SectionSortCriterion.NATURAL, "Section", null));
    }

    @Test
    void SectionServiceImpl_findSections_daoExceptionAtFindTopic() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> sectionService.findSections(SectionSortCriterion.NATURAL,
                        "Section", id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void SectionServiceImpl_findSections_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> sectionService.findSections(SectionSortCriterion.NATURAL,
                        "Section", null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_updateSection() {
        Section section = sections.get(0);
        Topic topic = section.getTopic();
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Assertions.assertDoesNotThrow(() -> sectionService.updateSection(section, id));
    }

    @Test
    void SectionServiceImpl_updateSection_titleIsNull() {
        String message = "Error at updating section: title is null";
        Section section = new Section(1, null, "Section", new Topic());
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.updateSection(section,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_updateSection_descriptionIsNull() {
        String message = "Error at updating section: description is null";
        Section section = new Section(1, "Title", null, new Topic());
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.updateSection(section,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_updateSection_topicIsNull() {
        String message = "Error at updating section: topic is null";
        Section section = new Section(1, "Title", "Section",
                null);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.updateSection(section,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_updateSection_noTopic() {
        String message = "Error at updating section: no such topic for section";
        Section section = sections.get(0);

        Integer id = section.getId();
        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doReturn(null).when(topicDao).findById(section.getTopic()
                .getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.updateSection(section,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateSection_daoExceptionAtFindTopic() {
        Section section = sections.get(0);

        Integer id = section.getId();
        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(topicDao).findById(section.getTopic().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionService.updateSection(
                        section, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_updateSection_noSection() {
        String message = "Error at updating section: no such section";
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(null).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.updateSection(section,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_updateSection_daoExceptionAtFindSection() {
        Section section = sections.get(1);
        Integer id = section.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionService.updateSection(
                        section, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_updateSection_daoExceptionAtUpdate() {
        Section section = sections.get(1);
        Topic topic = section.getTopic();

        Integer id = section.getId();
        Mockito.doReturn(topic).when(topicDao).findById(topic.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).update(section);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> sectionService.updateSection(section,
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_deleteSectionById() {
        Section section = sections.get(0);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Assertions.assertDoesNotThrow(() -> sectionService.deleteSectionById(id));
    }

    @Test
    void SectionServiceImpl_deleteSectionById_noSection() {
        String message = "Error at deleting section by id: no such section";
        Integer id = 0;

        Mockito.doReturn(null).when(sectionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> sectionService.deleteSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void SectionServiceImpl_deleteSectionById_daoExceptionAtFindSection() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(
                sectionDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> sectionService
                        .deleteSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void SectionServiceImpl_deleteSectionById_daoExceptionAtDelete() {
        Section section = sections.get(1);
        Integer id = section.getId();

        Mockito.doReturn(section).when(sectionDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).delete(section);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> sectionService.deleteSectionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}