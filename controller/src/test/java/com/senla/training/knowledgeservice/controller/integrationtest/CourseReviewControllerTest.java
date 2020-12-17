package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseReviewSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.CourseReviewController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.review.course.CourseReviewDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.review.CourseReviewDto;
import com.senla.training.knowledgeservice.dto.mapper.review.CourseReviewMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
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
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class CourseReviewControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<CourseReview> reviews;
    private static List<CourseReviewDto> reviewDtos;
    private static CourseReviewDto reviewDtoForAll;
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private CourseReviewController reviewController;
    @Autowired
    private CourseReviewMapper reviewMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private CourseReviewDao reviewDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseDao courseDao;

    @BeforeAll
    public static void setUp() {
        User user = new User(1, "login", "pass", RoleType.ROLE_USER,
                new UserProfile());
        Course course = new Course(1, "Title", "Course", 3, BigDecimal.TEN,
                new Section());
        CourseReview courseReview = new CourseReview(1, "Great", new Date(),
                user, course);
        CourseReview courseReviewTwo = new CourseReview(2, "Good", new Date(),
                user, course);
        reviews = Arrays.asList(courseReview, courseReviewTwo);
        reviewDtos = Arrays.asList(new CourseReviewDto(), new CourseReviewDto());
        reviewDtoForAll = new CourseReviewDto();
    }

    @Test
    void CourseReviewController_addReview() {
        CourseReview review = reviews.get(0);
        User user = review.getUser();
        Course course = review.getCourse();

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added course review");
        Assertions.assertEquals(messageDtoForAll, reviewController.addReview(reviewDtoForAll));
    }

    @Test
    void CourseReviewController_addReview_messageIsNull() {
        String message = "Error at adding course review: message is null";
        CourseReview review = new CourseReview(null, new Date(),
                new User(), new Course());

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_addReview_dateIsNull() {
        String message = "Error at adding course review: date is null";
        CourseReview review = new CourseReview("", null, new User(), new Course());

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_addReview_courseIsNull() {
        String message = "Error at adding course review: course is null";
        CourseReview review = new CourseReview("Great", new Date(),
                new User(), null);

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_addReview_userIsNull() {
        String message = "Error at adding course review: user is null";
        CourseReview review = new CourseReview("Great", new Date(),
                null, new Course());

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_addReview_noUser() {
        String message = "Error at adding course review: no such user";
        CourseReview review = reviews.get(0);
        Course course = review.getCourse();

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(userDao).findById(review.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_addReview_daoExceptionAtFindCourse() {
        CourseReview review = reviews.get(0);

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(review.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_addReview_daoExceptionAtFindUser() {
        CourseReview review = reviews.get(0);

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_addReview_daoExceptionAtAdd() {
        CourseReview review = reviews.get(1);
        User user = review.getUser();
        Course course = review.getCourse();

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(reviewDao).add(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_findReviewById() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(reviewDtoForAll).when(reviewMapper).toDto(review);
        Mockito.doReturn(review).when(reviewDao).findById(id);
        Assertions.assertEquals(reviewDtoForAll, reviewController.findCourseReviewById(id));
    }

    @Test
    void CourseReviewController_findReviewById_noReview() {
        String message = "Error at finding course review by id: no such review";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(reviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.findCourseReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_findReviewById_daoException() {
        Integer id = -1;

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(reviewDao).findById(id);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> reviewController.findCourseReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseReviewController_findReviews() {
        Mockito.doReturn(reviewDtos).when(reviewMapper).listToDto(reviews);
        Mockito.doReturn(reviews).when(reviewDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());

        Assertions.assertIterableEquals(reviewDtos, reviewController
                .findReviews(CourseReviewSortCriterion.NATURAL, null, null,
                        new Date(), new Date()));
    }

    @Test
    void CourseReviewController_findReviews_daoExceptionAtFindUser() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.findReviews(CourseReviewSortCriterion.NATURAL, id,
                        null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_findReviews_daoExceptionAtFindCourse() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.findReviews(CourseReviewSortCriterion.NATURAL, null,
                        id, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseReviewController_findReviews_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(reviewDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> reviewController.findReviews(CourseReviewSortCriterion.NATURAL,
                        null, null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_updateReview() {
        CourseReview review = reviews.get(0);
        User user = review.getUser();
        Course course = review.getCourse();
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated course review");
        Assertions.assertEquals(messageDtoForAll,
                reviewController.updateCourseReview(reviewDtoForAll, id));
    }

    @Test
    void CourseReviewController_updateReview_messageIsNull() {
        String message = "Error at updating course review: message is null";
        CourseReview review = new CourseReview(1, null, new Date(),
                new User(), new Course());
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_updateReview_dateIsNull() {
        String message = "Error at updating course review: date is null";
        CourseReview review = new CourseReview(1, "Message", null,
                new User(), new Course());
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_updateReview_courseIsNull() {
        String message = "Error at updating course review: course is null";
        CourseReview review = new CourseReview("Great", new Date(),
                new User(), null);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_updateReview_userIsNull() {
        String message = "Error at updating course review: user is null";
        CourseReview review = new CourseReview("Great", new Date(), null, new Course());
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_updateReview_noCourse() {
        String message = "Error at updating course review: no such course";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(null).when(courseDao).findById(review.getCourse().getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_updateReview_noUser() {
        String message = "Error at updating course review: no such user";
        CourseReview review = reviews.get(0);
        Course course = review.getCourse();
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(userDao).findById(review.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateReview_noReview() {
        String message = "Error at updating course review by id: no such review";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseController_updateReview_daoExceptionAtFindReview() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_updatedReview_daoExceptionAtFindCourse() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(review.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_updateReview_daoExceptionAtFindUser() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.updateCourseReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewController_deleteReviewById() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(reviewDtoForAll).when(reviewMapper).toDto(review);
        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully deleted course review");
        Assertions.assertEquals(messageDtoForAll, reviewController.deleteCourseReviewById(id));
    }

    @Test
    void CourseReviewController_deleteReviewById_noReview() {
        String message = "Error at deleting course review by id: no such review";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(reviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.deleteCourseReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewController_deleteReviewById_daoExceptionAtFind() {
        Integer id = 2;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(reviewDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.deleteCourseReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
