package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonReviewSortCriterion;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.review.lesson.LessonReviewDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.model.review.LessonReview;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.service.review.lesson.LessonReviewService;
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
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class LessonReviewServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<LessonReview> reviews;
    @Autowired
    private LessonReviewService lessonReviewService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LessonReviewDao lessonReviewDao;
    @Autowired
    private LessonDao lessonDao;

    @BeforeAll
    public static void setUp() {
        User user = new User(1, "login", "pass", RoleType.ROLE_USER,
                new UserProfile());
        Lesson lesson = new Lesson(1,"Title", "Lesson", LessonType.GROUP,
                new Course());
        LessonReview lessonReview = new LessonReview(1, "Great", new Date(),
                user, lesson);
        LessonReview lessonReviewTwo = new LessonReview(2, "Good", new Date(),
                user, lesson);
        reviews = Arrays.asList(lessonReview, lessonReviewTwo);
    }

    @Test
    void LessonReviewServiceImpl_addReview() {
        LessonReview review = reviews.get(0);
        User user = review.getUser();
        Lesson lesson = review.getLesson();

        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Assertions.assertDoesNotThrow(() -> lessonReviewService.addReview(review));
    }

    @Test
    void LessonReviewServiceImpl_addReview_messageIsNull() {
        String message = "Error at adding lesson review: message is null";
        LessonReview review = new LessonReview(null, new Date(), new User(), new Lesson());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_addReview_dateIsNull() {
        String message = "Error at adding lesson review: date is null";
        LessonReview review = new LessonReview("message", null,
                new User(), new Lesson());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_addReview_lessonIsNull() {
        String message = "Error at adding lesson review: lesson is null";
        LessonReview review = new LessonReview("Great", new Date(), new User(), null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_addReview_userIsNull() {
        String message = "Error at adding lesson review: user is null";
        LessonReview review = new LessonReview("Great", new Date(),
                null, new Lesson());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_addReview_noLesson() {
        String message = "Error at adding lesson review: no such lesson";
        LessonReview review = reviews.get(0);
        Lesson lesson = review.getLesson();

        Mockito.doReturn(null).when(lessonDao).findById(lesson.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_addReview_noUser() {
        String message = "Error at adding lesson review: no such user";
        LessonReview review = reviews.get(0);
        User user = review.getUser();
        Lesson lesson = review.getLesson();

        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(null).when(userDao).findById(user.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_addReview_daoExceptionAtFindLesson() {
        LessonReview review = reviews.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findById(reviews.get(0).getLesson().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_addReview_daoExceptionAtFindUser() {
        LessonReview review = reviews.get(0);
        Lesson lesson = review.getLesson();

        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_addReview_daoExceptionAtAdd() {
        LessonReview review = reviews.get(1);
        User user = review.getUser();
        Lesson lesson = review.getLesson();

        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).add(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonReviewService.addReview(review));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_findReviewById() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Assertions.assertEquals(review, lessonReviewService.findReviewById(id));
    }

    @Test
    void LessonReviewImpl_findReviewById_noReview() {
        String message = "Error at finding lesson review by id: no such review";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.findReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewImpl_findReviewById_daoExceptionAtFind() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonReviewService.findReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonReviewImpl_findReviews() {
        Mockito.doReturn(reviews).when(lessonReviewDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> lessonReviewService
                .findReviews(LessonReviewSortCriterion.NATURAL, null, null,
                        new Date(), new Date()));
    }

    @Test
    void LessonReviewImpl_findReviews_daoExceptionAtFindUser() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonReviewService.findReviews(LessonReviewSortCriterion.NATURAL,
                                id, null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewImpl_findReviews_daoExceptionAtFindLesson() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> lessonReviewService.findReviews(LessonReviewSortCriterion.NATURAL,
                                null, id, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonReviewImpl_findReviews_daoExceptionAtFindAll() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> lessonReviewService.findReviews(LessonReviewSortCriterion.NATURAL,
                                null, null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_updateReview() {
        LessonReview review = reviews.get(0);
        User user = review.getUser();
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Assertions.assertDoesNotThrow(() -> lessonReviewService.updateReview(review, id));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_messageIsNull() {
        String message = "Error at updating lesson review: message is null";
        LessonReview review = new LessonReview(1, null, new Date(),
                new User(), new Lesson());
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_dateIsNull() {
        String message = "Error at updating lesson review: date is null";
        LessonReview review = new LessonReview(1, "Message", null,
                new User(), new Lesson());
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_lessonIsNull() {
        String message = "Error at updating lesson review: lesson is null";
        LessonReview review = new LessonReview(1, "Great", new Date(),
                new User(), null);
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_userIsNull() {
        String message = "Error at updating lesson review: user is null";
        LessonReview review = new LessonReview(1, "Great", new Date(),
                null, new Lesson());
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_noLesson() {
        String message = "Error at updating lesson review: no such lesson";
        LessonReview review = reviews.get(0);
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doReturn(null).when(lessonDao).findById(lesson.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_noUser() {
        String message = "Error at updating lesson review: no such user";
        LessonReview review = reviews.get(0);
        User user = review.getUser();
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(null).when(userDao).findById(user.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_noReview() {
        String message = "Error at updating lesson review by id: no such review";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_daoExceptionAtFindReview() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_daoExceptionAtFindLesson() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findById(reviews.get(0).getLesson().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_daoExceptionAtFindUser() {
        LessonReview review = reviews.get(0);
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(reviews.get(0).getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_updateReview_daoExceptionAtUpdate() {
        LessonReview review = reviews.get(1);
        User user = review.getUser();
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).update(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonReviewService.updateReview(review, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewServiceImpl_deleteReviewById() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Assertions.assertDoesNotThrow(() -> lessonReviewService.deleteReviewById(id));
    }

    @Test
    void LessonReviewImpl_deleteReviewById_noReview() {
        String message = "Error at deleting lesson review by id: no such review";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(lessonReviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> lessonReviewService.deleteReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewImpl_deleteReviewById_daoExceptionAtFind() {
        Integer id = -2;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> lessonReviewService.deleteReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewImpl_deleteReviewById_daoExceptionAtDelete() {
        LessonReview review = reviews.get(1);
        Integer id = review.getId();

        Mockito.doReturn(review).when(lessonReviewDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonReviewDao).delete(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> lessonReviewService.deleteReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
