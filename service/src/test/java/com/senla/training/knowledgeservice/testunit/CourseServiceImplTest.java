package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.service.service.course.CourseService;
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
class CourseServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Course> courses;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private SectionDao sectionDao;

    @BeforeAll
    public static void setUp() {
        Section section = new Section();
        Course courseOne = new Course(1, "Title", "Course",
                1, BigDecimal.TEN, section);
        Course courseTwo = new Course(2, "Title", "Course two",
                10, BigDecimal.TEN, section);
        courses = Arrays.asList(courseOne, courseTwo);
    }

    @Test
    void CourseServiceImpl_addCourse() {
        Course course = courses.get(0);
        Section section = course.getSection();

        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Assertions.assertDoesNotThrow(() -> courseService.addCourse(course));
    }

    @Test
    void CourseServiceImpl_addCourse_titleIsNull() {
        String message = "Error at adding course: title is null";
        Course course = new Course(1, null, "Course", 1, BigDecimal.TEN,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_descriptionIsNull() {
        String message = "Error at adding course: description is null";
        Course course = new Course(1, "", null, 1, BigDecimal.TEN,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_priceIsNull() {
        String message = "Error at adding course: price is null";
        Course course = new Course(1, "", "Course", 1, null,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_durationIsNull() {
        String message = "Error at adding course: duration is null";
        Course course = new Course(1, "", "Course", null, BigDecimal.TEN,
                new Section());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_sectionIsNull() {
        String message = "Error at adding course: section is null";
        Course course = new Course(1, "", "Course", 1, BigDecimal.TEN,
                null);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_noSection() {
        String message = "Error at adding course: no such section for course";
        Course course = courses.get(0);

        Mockito.doReturn(null).when(sectionDao).findById(
                course.getSection().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_wrongPrice() {
        String message = "Error at adding course: invalid price";
        Section section = new Section();
        Course course = new Course("Course", "", 1, BigDecimal.ZERO,
                section);
        section.setId(1);

        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_wrongDuration() {
        String message = "Error at adding course: invalid duration";
        Section section = new Section();
        Course course = new Course("", "Course", -1, BigDecimal.TEN, section);
        section.setId(1);

        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_addCourse_daoExceptionAtFindSection() {
        Course course = courses.get(0);
        Section section = course.getSection();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(section.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_addCourse_daoExceptionAtAdd() {
        Course course = courses.get(1);
        Section section = course.getSection();

        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).add(course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseService.addCourse(course));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_findCourseById() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Assertions.assertEquals(course, courseService.findCourseById(id));
    }

    @Test
    void CourseServiceImpl_findCourseById_noCourse() {
        String message = "Error at finding course by id: no such course";
        Integer id = 0;

        Mockito.doReturn(null).when(courseDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.findCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_findCourseById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseService.findCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseServiceImpl_findCourses() {
        Mockito.doReturn(courses).when(courseDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> courseService
                .findCourses(CourseSortCriterion.PRICE, "", BigDecimal.ZERO,
                        BigDecimal.ONE, 1, 2, null));
    }

    @Test
    void CourseServiceImpl_findCourses_daoExceptionAtFindSection() {
        Integer id = 0;

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(id);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseService
                        .findCourses(CourseSortCriterion.PRICE, "", BigDecimal.ZERO,
                                BigDecimal.ONE, 1, 2, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseServiceImpl_findCourses_daoExceptionAtFindAll() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseService
                        .findCourses(CourseSortCriterion.PRICE, "", BigDecimal.ZERO,
                                BigDecimal.ONE, 1, 2, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_updateCourse() {
        Course course = courses.get(0);
        Section section = course.getSection();
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Assertions.assertDoesNotThrow(() -> courseService.updateCourse(course, id));
    }

    @Test
    void CourseServiceImpl_updateCourse_titleIsNull() {
        String message = "Error at updating course: title is null";
        Course course = new Course(1, null, "Course", 1, null,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, course.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_descriptionIsNull() {
        String message = "Error at updating course: description is null";
        Course course = new Course(1, "", null, 1, null,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, course.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_priceIsNull() {
        String message = "Error at updating course: price is null";
        Course course = new Course(1, "", "Course", 1, null,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, course.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_durationIsNull() {
        String message = "Error at updating course: duration is null";
        Course course = new Course(1, "", "Course", null, BigDecimal.TEN,
                new Section());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, course.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_sectionIsNull() {
        String message = "Error at updating course: section is null";
        Course course = new Course(1, "", "Course", 1, BigDecimal.TEN,
                null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, course.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_noSection() {
        String message = "Error at updating course: no such section for course";
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(null).when(sectionDao).findById(
                course.getSection().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_wrongPrice() {
        String message = "Error at updating course: invalid price";
        Section section = new Section();
        Course course = new Course("", "Course", 1, BigDecimal.ZERO, section);
        section.setId(1);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_wrongDuration() {
        String message = "Error at updating course: invalid duration";
        Section section = new Section();
        Course course = new Course("", "Course", -1, BigDecimal.TEN, section);
        section.setId(1);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_noCourse() {
        String message = "Error at updating course: no such course";
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(null).when(courseDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_updateCourse_daoExceptionAtFindCourse() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_updateCourse_daoExceptionAtFindSection() {
        Course course = courses.get(1);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(course.getSection().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_updateCourse_daoExceptionAtUpdate() {
        Course course = courses.get(1);
        Section section = course.getSection();
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).update(course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseService.updateCourse(course, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_deleteCourseById() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Assertions.assertDoesNotThrow(() -> courseService.deleteCourseById(id));
    }

    @Test
    void CourseServiceImpl_deleteCourseById_noCourse() {
        String message = "Error at deleting course by id: no such course";
        Integer id = -1;

        Mockito.doReturn(null).when(courseDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseService.deleteCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseServiceImpl_deleteCourseById_daoExceptionAtFindCourse() {
        Integer id = 2;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseService.deleteCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseServiceImpl_deleteCourseById_daoExceptionAtDelete() {
        Course course = courses.get(1);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).delete(course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseService.deleteCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
