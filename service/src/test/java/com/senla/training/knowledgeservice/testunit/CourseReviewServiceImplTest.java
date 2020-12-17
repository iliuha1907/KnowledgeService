package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseReviewSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.review.course.CourseReviewDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.service.review.course.CourseReviewService;
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
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
class CourseReviewServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<CourseReview> reviews;
    @Autowired
    private CourseReviewService courseReviewService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseReviewDao courseReviewDao;
    @Autowired
    private CourseDao courseDao;

    @BeforeAll
    public static void setUp() {
        User user = new User(1, "login", "password", RoleType.ROLE_USER,
                new UserProfile());
        Course course = new Course(1, "Title", "course", 3, BigDecimal.TEN,
                new Section());
        CourseReview courseReview = new CourseReview(1, "Great", new Date(),
                user, course);
        CourseReview courseReviewTwo = new CourseReview(2, "Good", new Date(),
                user, course);
        reviews = Arrays.asList(courseReview, courseReviewTwo);
    }

    @Test
    void CourseReviewServiceImpl_addReview() {
        CourseReview review = reviews.get(0);
        User user = review.getUser();
        Course course = review.getCourse();

        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Assertions.assertDoesNotThrow(() -> courseReviewService.addReview(review));
    }

    @Test
    void CourseReviewServiceImpl_addReview_messageIsNull() {
        String message = "Error at adding course review: message is null";
        CourseReview review = new CourseReview(null, new Date(),
                new User(), new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_addReview_dateIsNull() {
        String message = "Error at adding course review: date is null";
        CourseReview review = new CourseReview("", null, new User(), new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_addReview_courseIsNull() {
        String message = "Error at adding course review: course is null";
        CourseReview review = new CourseReview("Great", new Date(),
                new User(), null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_addReview_userIsNull() {
        String message = "Error at adding course review: user is null";
        CourseReview review = new CourseReview("Great", new Date(),
                null, new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_addReview_noCourse() {
        String message = "Error at adding course review: no such course";
        CourseReview review = reviews.get(0);
        User user = review.getUser();

        Mockito.doReturn(null).when(courseDao).findById(review.getCourse().getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_addReview_noUser() {
        String message = "Error at adding course review: no such user";
        CourseReview review = reviews.get(0);
        Course course = review.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(userDao).findById(review.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_addReview_daoExceptionAtFindCourse() {
        CourseReview review = reviews.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(review.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_addReview_daoExceptionAtFindUser() {
        CourseReview review = reviews.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_addReview_daoExceptionAtAdd() {
        CourseReview review = reviews.get(1);
        User user = review.getUser();
        Course course = review.getCourse();

        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseReviewDao).add(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_findReviewById() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(courseReviewDao).findById(id);
        Assertions.assertEquals(review, courseReviewService.findReviewById(id));
    }

    @Test
    void CourseReviewServiceImpl_findReviewById_noReview() {
        String message = "Error at finding course review by id: no such review";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(courseReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.findReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_findReviewById_daoExceptionAtFind() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseReviewDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.findReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseReviewServiceImpl_findReviews() {
        Mockito.doReturn(reviews).when(courseReviewDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> courseReviewService
                .findReviews(CourseReviewSortCriterion.NATURAL, null, null,
                        new Date(), new Date()));
    }

    @Test
    void CourseReviewServiceImpl_findReviews_daoExceptionAtFindUser() {
        Integer id = 0;

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(id);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseReviewService
                        .findReviews(CourseReviewSortCriterion.NATURAL, id,
                                null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_findReviews_daoExceptionAtFindCourse() {
        Integer id = 0;

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(id);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseReviewService
                        .findReviews(CourseReviewSortCriterion.NATURAL, null,
                                id, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseReviewServiceImpl_findReviews_daoExceptionAtFindAll() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseReviewDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseReviewService
                        .findReviews(CourseReviewSortCriterion.NATURAL, null,
                                null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_updateReview() {
        CourseReview review = reviews.get(0);
        User user = review.getUser();
        Course course = review.getCourse();
        Integer id = review.getId();

        Mockito.doReturn(review).when(courseReviewDao).findById(id);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Assertions.assertDoesNotThrow(() ->
                courseReviewService.updateReview(review, id));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_messageIsNull() {
        String message = "Error at updating course review: message is null";
        CourseReview review = new CourseReview(1, null, new Date(),
                new User(), new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_dateIsNull() {
        String message = "Error at updating course review: date is null";
        CourseReview review = new CourseReview(1, "message", null,
                new User(), new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_courseIsNull() {
        String message = "Error at updating course review: course is null";
        CourseReview review = new CourseReview(1, "Great", new Date(),
                new User(), null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_userIsNull() {
        String message = "Error at updating course review: user is null";
        CourseReview review = new CourseReview(1, "Great", new Date(),
                null, new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_noCourse() {
        String message = "Error at updating course review: no such course";
        CourseReview review = reviews.get(0);
        Course course = review.getCourse();
        User user = review.getUser();
        Integer id = review.getId();

        Mockito.doReturn(review).when(courseReviewDao).findById(id);
        Mockito.doReturn(null).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_noUser() {
        String message = "Error at updating course review: no such user";
        CourseReview review = reviews.get(0);
        Course course = review.getCourse();
        Integer id = review.getId();

        Mockito.doReturn(review).when(courseReviewDao).findById(id);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(userDao).findById(review.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_noReview() {
        String message = "Error at updating course review by id: no such review";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(courseReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_daoExceptionAtFindReview() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseReviewDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_daoExceptionAtFindCourse() {
        CourseReview review = reviews.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(review.getCourse().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_daoExceptionAtFindUser() {
        CourseReview review = reviews.get(0);
        Course course = review.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_updateReview_daoExceptionAtUpdate() {
        CourseReview review = reviews.get(1);
        Course course = review.getCourse();
        User user = review.getUser();

        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseReviewDao).update(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseReviewService.updateReview(review, review.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseReviewServiceImpl_deleteReviewById() {
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(courseReviewDao).findById(id);
        Assertions.assertDoesNotThrow(() -> courseReviewService.deleteReviewById(id));
    }

    @Test
    void CourseReviewImpl_deleteReviewById_noReview() {
        String message = "Error at deleting course review by id: no such review";
        CourseReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(courseReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> courseReviewService.deleteReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewImpl_deleteReviewById_daoExceptionAtFind() {
        String message = "Error at deleting course review by id";
        Integer id = -2;

        Mockito.doThrow(new IllegalStateException(message)).when(courseReviewDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> courseReviewService.deleteReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseReviewImpl_deleteReviewById_daoExceptionAtDelete() {
        CourseReview review = reviews.get(1);
        Integer id = review.getId();

        Mockito.doReturn(review).when(courseReviewDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseReviewDao).delete(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> courseReviewService.deleteReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
