package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.LessonController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dto.dto.lesson.LessonDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.lesson.LessonMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.model.section.Section;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class LessonControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Lesson> lessons;
    private static List<LessonDto> lessonDtos;
    private static LessonDto lessonDtoForAll;
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private LessonController lessonController;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Course courseOne = new Course(1, "Title", "Course", 3,
                BigDecimal.TEN, new Section());
        Course courseTwo = new Course(2, "Title", "Course two", 3,
                BigDecimal.TEN, new Section());
        Lesson lessonOne = new Lesson(1, "Title", "Lesson",
                LessonType.GROUP, courseOne);
        Lesson lessonTwo = new Lesson(2, "Title", "Lesson two",
                LessonType.GROUP, courseTwo);

        lessons = Arrays.asList(lessonOne, lessonTwo);
        lessonDtos = Arrays.asList(new LessonDto(), new LessonDto());
        lessonDtoForAll = new LessonDto();
    }

    @Test
    void LessonController_addLesson() {
        Lesson lesson = lessons.get(0);
        Course course = lesson.getCourse();

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added lesson");
        Assertions.assertEquals(messageDtoForAll,
                lessonController.addLesson(lessonDtoForAll));
    }

    @Test
    void LessonController_addLesson_titleIsNull() {
        String message = "Error at adding lesson: title is null";
        Lesson lesson = new Lesson(null, "Lesson", LessonType.INDIVIDUAL,
                new Course());

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_addLesson_descriptionIsNull() {
        String message = "Error at adding lesson: description is null";
        Lesson lesson = new Lesson("Title", null, LessonType.INDIVIDUAL, new Course());

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_addLesson_typeIsNull() {
        String message = "Error at adding lesson: type is null";
        Lesson lesson = new Lesson("Title", "Desc", null, new Course());

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_addLesson_courseIsNull() {
        String message = "Error at adding lesson: course is null";
        Lesson lesson = new Lesson("Title", "Lesson", LessonType.INDIVIDUAL,
                null);

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_addLesson_noCourse() {
        String message = "Error at adding lesson: no such course for lesson";
        Lesson lesson = lessons.get(0);

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doReturn(null).when(courseDao).findById(lesson.getCourse().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_addLesson_daoExceptionAtFind() {
        Lesson lesson = lessons.get(0);

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(lesson.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_addLesson_daoExceptionAtAdd() {
        Lesson lesson = lessons.get(1);
        Course course = lesson.getCourse();

        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(lessonDao).add(lesson);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonController.addLesson(lessonDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_findLessonById() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lessonDtoForAll).when(lessonMapper).toDto(lesson);
        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Assertions.assertEquals(lessonDtoForAll, lessonController.findLessonById(id));
    }

    @Test
    void LessonController_findLessonById_noLesson() {
        String message = "Error at finding lesson by id: no such lesson";
        Integer id = 0;

        Mockito.doReturn(null).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.findLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_findLessonById_daoExceptionAtFind() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonController.findLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonController_findCourses() {
        Mockito.doReturn(lessonDtos).when(lessonMapper).listToDto(lessons);
        Mockito.doReturn(lessons).when(lessonDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> lessonController
                .findLessons(LessonSortCriterion.NATURAL, LessonType.INDIVIDUAL, "",
                        null));
    }

    @Test
    void LessonController_findLessons_daoExceptionAtFindCourse() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonController.findLessons(LessonSortCriterion.NATURAL,
                        LessonType.INDIVIDUAL, "", id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonController_findLessons_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> lessonController.findLessons(LessonSortCriterion.NATURAL,
                        LessonType.INDIVIDUAL, "", null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_updateLesson() {
        Lesson lesson = lessons.get(0);
        Course course = lesson.getCourse();
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated lesson");
        Assertions.assertEquals(messageDtoForAll,
                lessonController.updateLesson(lessonDtoForAll, id));
    }

    @Test
    void LessonController_updateLesson_titleIsNull() {
        String message = "Error at updating lesson: title is null";
        Lesson lesson = new Lesson(null, "Lesson", LessonType.INDIVIDUAL,
                new Course());
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_updateLesson_descriptionIsNull() {
        String message = "Error at updating lesson: description is null";
        Lesson lesson = new Lesson("Title", null, LessonType.INDIVIDUAL, new Course());
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_updateLesson_typeIsNull() {
        String message = "Error at updating lesson: type is null";
        Lesson lesson = new Lesson("Title", "Desc", null, new Course());
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_updateLesson_courseIsNull() {
        String message = "Error at updating lesson: course is null";
        Lesson lesson = new Lesson("Title", "Lesson", LessonType.INDIVIDUAL, null);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_noCourse() {
        String message = "Error at updating lesson: no such course for lesson";
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doReturn(null).when(courseDao).findById(lesson.getCourse().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_updateLesson_noLesson() {
        String message = "Error at updating lesson: no such lesson";
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(null).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_updateLesson_daoExceptionAtFindLesson() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao)
                .findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_updateLesson_daoExceptionAtFindCourse() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(lesson.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_updateLesson_daoExceptionAtUpdate() {
        Lesson lesson = lessons.get(1);
        Course course = lesson.getCourse();
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(lesson).when(lessonMapper).toEntity(lessonDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(lessonDao)
                .update(lesson);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> lessonController.updateLesson(lessonDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_deleteLessonById() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully deleted lesson");
        Assertions.assertEquals(messageDtoForAll, lessonController.deleteLessonById(id));
    }

    @Test
    void LessonController_deleteLessonById_noLesson() {
        String message = "Error at deleting lesson by id: no such lesson";
        Integer id = 0;

        Mockito.doReturn(null).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonController.deleteLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonController_deleteLessonById_daoExceptionAtFind() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonController.deleteLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonController_deleteLessonById_daoExceptionAtDelete() {
        Lesson lesson = lessons.get(1);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(lessonDao).delete(lesson);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonController.deleteLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
