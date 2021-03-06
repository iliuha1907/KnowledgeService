package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.service.service.lesson.LessonService;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class LessonServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Lesson> lessons;
    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private CourseDao courseDao;

    @BeforeAll
    public static void setUp() {
        Course course = new Course(1, "Title", "Course", 3, BigDecimal.TEN,
                new Section());
        Lesson lessonOne = new Lesson(1, "Title", "Lesson", LessonType.INDIVIDUAL,
                course);
        Lesson lessonTwo = new Lesson(2, "Title", "Lesson two", LessonType.INDIVIDUAL,
                course);
        lessons = Arrays.asList(lessonOne, lessonTwo);
    }

    @Test
    void LessonServiceImpl_addLesson() {
        Lesson lesson = lessons.get(0);
        Course course = lesson.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Assertions.assertDoesNotThrow(() -> lessonService.addLesson(lesson));
    }

    @Test
    void LessonServiceImpl_addLesson_titleIsNull() {
        String message = "Error at adding lesson: title is null";
        Lesson lesson = new Lesson(1, null, "Lesson", LessonType.INDIVIDUAL,
                new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_addLesson_descriptionIsNull() {
        String message = "Error at adding lesson: description is null";
        Lesson lesson = new Lesson(1, "Title", null, LessonType.INDIVIDUAL,
                new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_addLesson_typeIsNull() {
        String message = "Error at adding lesson: type is null";
        Lesson lesson = new Lesson(1, "Title", "description", null,
                new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_addLesson_courseIsNull() {
        String message = "Error at adding lesson: course is null";
        Lesson lesson = new Lesson(1, "Title", "Lesson", LessonType.INDIVIDUAL,
                null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_addLesson_noCourse() {
        String message = "Error at adding lesson: no such course for lesson";
        Lesson lesson = lessons.get(0);

        Mockito.doReturn(null).when(courseDao).findById(lesson.getCourse().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_addLesson_daoExceptionAtFindCourse() {
        Lesson lesson = lessons.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(lesson.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_addLesson_daoExceptionAtAdd() {
        Lesson lesson = lessons.get(1);
        Course course = lesson.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).add(lesson);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonService.addLesson(lesson));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_findLessonById() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Assertions.assertEquals(lesson, lessonService.findLessonById(id));
    }

    @Test
    void LessonServiceImpl_findLessonById_noLesson() {
        String message = "Error at finding lesson by id: no such lesson";
        Integer id = 0;

        Mockito.doReturn(null).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.findLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_findLessonById_daoExceptionAtFindLesson() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonService.findLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonServiceImpl_findLessons() {
        Mockito.doReturn(lessons).when(lessonDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> lessonService
                .findLessons(LessonSortCriterion.NATURAL, LessonType.INDIVIDUAL, "",
                        null));
    }

    @Test
    void LessonServiceImpl_findLessons_daoExceptionAtFindCourse() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonService.findLessons(LessonSortCriterion.NATURAL,
                        LessonType.INDIVIDUAL, "", id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonServiceImpl_findLessons_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> lessonService.findLessons(LessonSortCriterion.NATURAL,
                        LessonType.INDIVIDUAL, "", null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_updateLesson() {
        Lesson lesson = lessons.get(0);
        Course course = lesson.getCourse();
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Assertions.assertDoesNotThrow(() -> lessonService.updateLesson(lesson, id));
    }

    @Test
    void LessonServiceImpl_updateLesson_titleIsNull() {
        String message = "Error at updating lesson: title is null";
        Lesson lesson = new Lesson(1, null, "Lesson", LessonType.INDIVIDUAL,
                new Course());
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_descriptionIsNull() {
        String message = "Error at updating lesson: description is null";
        Lesson lesson = new Lesson(1, "Title", null, LessonType.INDIVIDUAL,
                new Course());
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_typeIsNull() {
        String message = "Error at updating lesson: type is null";
        Lesson lesson = new Lesson(1, "Title", "Description", null,
                new Course());
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_courseIsNull() {
        String message = "Error at updating lesson: course is null";
        Lesson lesson = new Lesson(1, "Title", "Lesson", LessonType.INDIVIDUAL, null);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_noCourse() {
        String message = "Error at updating lesson: no such course for lesson";
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(null).when(courseDao)
                .findById(lessons.get(0).getCourse().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_noLesson() {
        String message = "Error at updating lesson: no such lesson";
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(null).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_updateLesson_daoExceptionAtFindLesson() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_updateLesson_daoExceptionAtFindCourse() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(lesson.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_updatingLesson_daoExceptionAtUpdate() {
        Lesson lesson = lessons.get(1);
        Course course = lesson.getCourse();
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).update(lesson);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonService.updateLesson(lesson, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_deleteLessonById() {
        Lesson lesson = lessons.get(0);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Assertions.assertDoesNotThrow(() -> lessonService.deleteLessonById(id));
    }

    @Test
    void LessonServiceImpl_deleteLessonById_noLesson() {
        String message = "Error at deleting lesson by id: no such lesson";
        Integer id = 0;

        Mockito.doReturn(null).when(lessonDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonService.deleteLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonServiceImpl_deleteLessonById_daoExceptionAtFindLesson() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonService.deleteLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonServiceImpl_deleteLessonById_daoExceptionAtDelete() {
        Lesson lesson = lessons.get(1);
        Integer id = lesson.getId();

        Mockito.doReturn(lesson).when(lessonDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).delete(lesson);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonService.deleteLessonById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
