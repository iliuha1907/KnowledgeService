package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.CourseController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.dto.dto.course.CourseDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.course.CourseMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.course.Course;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class CourseControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Course> courses;
    private static List<CourseDto> courseDtos;
    private static CourseDto courseDtoForAll;
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private CourseController courseController;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @BeforeAll
    public static void setUp() {
        Section sectionOne = new Section(1,"Title", "Section", new Topic());
        Section sectionTwo = new Section(2,"Title", "Section two", new Topic());
        Course courseOne = new Course(1,"Title", "Course", 3,
                BigDecimal.TEN, sectionOne);
        Course courseTwo = new Course(2,"Title", "Course two", 3,
                BigDecimal.TEN, sectionTwo);
        courses = Arrays.asList(courseOne, courseTwo);
        courseDtos = Arrays.asList(new CourseDto(), new CourseDto());
        courseDtoForAll = new CourseDto();
    }

    @Test
    void CourseController_addCourse() {
        Course course = courses.get(0);
        Section section = course.getSection();

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Mockito.doReturn(messageDtoForAll).when(messageDtoMapper).toDto("Successfully" +
                " added course");
        Assertions.assertEquals(messageDtoForAll, courseController.addCourse(courseDtoForAll));
    }

    @Test
    void CourseController_addCourse_titleIsNull() {
        String message = "Error at adding course: title is null";
        Course course = new Course(1,null, "Course", 1, BigDecimal.TEN,
                new Section());

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_descriptionIsNull() {
        String message = "Error at adding course: description is null";
        Course course = new Course(1,"title", null, 1, BigDecimal.TEN,
                new Section());

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_priceIsNull() {
        String message = "Error at adding course: price is null";
        Course course = new Course(1,"", "Course", 1, null,
                new Section());

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_durationIsNull() {
        String message = "Error at adding course: duration is null";
        Course course = new Course(1,"", "Course", null, BigDecimal.TEN,
                new Section());

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_sectionIsNull() {
        String message = "Error at adding course: section is null";
        Course course = new Course(1,"", "Course", 1, BigDecimal.TEN,
                null);

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_noSection() {
        String message = "Error at adding course: no such section for course";
        Course course = courses.get(0);

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(null).when(sectionDao).findById(
                course.getSection().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_wrongPrice() {
        String message = "Error at adding course: invalid price";
        Section section = courses.get(0).getSection();
        Course course = new Course("Title","Course", 1, BigDecimal.ZERO,
                section);

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_wrongDuration() {
        String message = "Error at adding course: invalid duration";
        Section section = courses.get(0).getSection();
        Course course = new Course("Title","Course", -1, BigDecimal.TEN,
                section);

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_addCourse_daoExceptionAtFind() {
        Course course = courses.get(0);

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(course.getSection().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_addCourse_daoExceptionAtAdd() {
        Course course = courses.get(1);
        Section section = course.getSection();

        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(courseDao).add(course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> courseController.addCourse(courseDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_findCourseById() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(courseDtoForAll).when(courseMapper).toDto(course);
        Mockito.doReturn(course).when(courseDao).findById(id);
        Assertions.assertEquals(courseDtoForAll, courseController.findCourseById(id));
    }

    @Test
    void CourseController_findCourseById_noCourse() {
        String message = "Error at finding course by id: no such course";
        Integer id = 0;

        Mockito.doReturn(null).when(courseDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.findCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_findCourseById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> courseController.findCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseController_findCourses() {
        Mockito.doReturn(courseDtos).when(courseMapper).listToDto(courses);
        Mockito.doReturn(courses).when(courseDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertIterableEquals(courseDtos, courseController
                .findCourses(CourseSortCriterion.PRICE, "", BigDecimal.ZERO,
                        BigDecimal.ONE, 1, 2, null));
    }

    @Test
    void CourseController_findCourses_daoExceptionAtFindSection() {
        Integer id = 0;

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(id);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseController.findCourses(CourseSortCriterion.PRICE, "",
                        BigDecimal.ZERO, BigDecimal.ONE, 1, 2, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseController_findCourses_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseController.findCourses(CourseSortCriterion.PRICE,
                        "", BigDecimal.ZERO, BigDecimal.ONE, 1, 2,
                        null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_updateCourse() {
        Course course = courses.get(0);
        Section section = course.getSection();
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Assertions.assertDoesNotThrow(() ->
                courseController.updateCourse(courseDtoForAll, id));
    }

    @Test
    void CourseController_updateCourse_titleIsNull() {
        String message = "Error at updating course: title is null";
        Course course = new Course(null,"Course", 1, BigDecimal.TEN,
                new Section());
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_descriptionIsNull() {
        String message = "Error at updating course: description is null";
        Course course = new Course("Title",null, 1, BigDecimal.TEN,
                new Section());
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_priceIsNull() {
        String message = "Error at updating course: price is null";
        Course course = new Course("Title","Course", 1, null,
                new Section());
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_durationIsNull() {
        String message = "Error at updating course: duration is null";
        Course course = new Course("Title","Course", null, BigDecimal.TEN,
                new Section());
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_sectionIsNull() {
        String message = "Error at updating course: section is null";
        Course course = new Course("Title","Course", 1, BigDecimal.TEN,
                null);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_noSection() {
        String message = "Error at updating course: no such section for course";
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(null).when(sectionDao).findById(course.getSection().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_wrongPrice() {
        String message = "Error at updating course: invalid price";
        Section section = courses.get(0).getSection();
        Course course = new Course("Title","Course", 1, BigDecimal.ZERO,
                section);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_wrongDuration() {
        String message = "Error at updating course: invalid duration";
        Section section = courses.get(0).getSection();
        Course course = new Course(1,"Title", "Course", -1, BigDecimal.TEN,
                section);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_noCourse() {
        String message = "Error at updating course: no such course";
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(null).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateCourse_daoExceptionAtFindCourse() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_updateCourse_daoExceptionAtFindSection() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(sectionDao).findById(course.getSection().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_updateCourse_daoExceptionAtUpdate() {
        Course course = courses.get(1);
        Section section = course.getSection();
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(course).when(courseMapper).toEntity(courseDtoForAll);
        Mockito.doReturn(section).when(sectionDao).findById(section.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(courseDao).update(
                course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseController.updateCourse(courseDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_deleteCourseById() {
        Course course = courses.get(0);
        Integer id = course.getId();

        Mockito.doReturn(courseDtoForAll).when(courseMapper).toDto(course);
        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully deleted course");
        Assertions.assertEquals(messageDtoForAll, courseController.deleteCourseById(id));
    }

    @Test
    void CourseController_deleteCourseById_noCourse() {
        String message = "Error at deleting course by id: no such course";
        Integer id = 0;

        Mockito.doReturn(null).when(courseDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseController.deleteCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_deleteCourseById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao).findById(
                id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> courseController.deleteCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseController_deleteCourseById_daoExceptionAtDelete() {
        Course course = courses.get(1);
        Integer id = course.getId();

        Mockito.doReturn(course).when(courseDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).delete(course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseController.deleteCourseById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
