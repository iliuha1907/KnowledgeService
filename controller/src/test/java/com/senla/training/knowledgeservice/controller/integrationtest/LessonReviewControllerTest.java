package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonReviewSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.LessonReviewController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.review.lesson.LessonReviewDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.dto.review.LessonReviewDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.review.LessonReviewMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.model.review.LessonReview;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class LessonReviewControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<LessonReview> reviews;
    private static List<LessonReviewDto> reviewDtos;
    private static LessonReviewDto reviewDtoForAll;
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private LessonReviewController reviewController;
    @Autowired
    private LessonReviewMapper reviewMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private LessonReviewDao reviewDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private LessonDao lessonDao;

    @BeforeAll
    public static void setUp() {
        User user = new User(1, "login", "pass", RoleType.ROLE_USER,
                new UserProfile());
        Lesson lesson = new Lesson(1, "Title", "Lesson", LessonType.GROUP,
                new Course());
        LessonReview lessonReview = new LessonReview(1, "Great", new Date(),
                user, lesson);
        LessonReview lessonReviewTwo = new LessonReview(2, "Good", new Date(),
                user, lesson);
        reviews = Arrays.asList(lessonReview, lessonReviewTwo);
        reviewDtos = Arrays.asList(new LessonReviewDto(), new LessonReviewDto());
        reviewDtoForAll = new LessonReviewDto();
    }

    @Test
    void LessonReviewController_addReview() {
        LessonReview review = reviews.get(0);
        User user = review.getUser();
        Lesson lesson = review.getLesson();

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added lesson review");
        Assertions.assertEquals(messageDtoForAll, reviewController.addReview(reviewDtoForAll));
    }

    @Test
    void LessonReviewController_addReview_messageIsNull() {
        String message = "Error at adding lesson review: message is null";
        LessonReview review = new LessonReview(null, new Date(), new User(), new Lesson());

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_addReview_dateIsNull() {
        String message = "Error at adding lesson review: date is null";
        LessonReview review = new LessonReview("Mes", null,
                new User(), new Lesson());

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_addReview_lessonIsNull() {
        String message = "Error at adding lesson review: lesson is null";
        LessonReview review = new LessonReview("Great", new Date(),
                new User(), null);

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_addReview_userIsNull() {
        String message = "Error at adding lesson review: user is null";
        LessonReview review = new LessonReview("Great", new Date(), null, new Lesson());

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_addReview_noLesson() {
        String message = "Error at adding lesson review: no such lesson";
        LessonReview review = reviews.get(0);

        Mockito.doReturn(null).when(lessonDao).findById(review.getLesson().getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_addReview_noUser() {
        String message = "Error at adding lesson review: no such user";
        LessonReview review = reviews.get(0);
        Lesson lesson = review.getLesson();

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(null).when(userDao).findById(review.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_addReview_daoExceptionAtFindLesson() {
        LessonReview review = reviews.get(0);

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findById(review.getLesson().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_addReview_daoExceptionAtFindUser() {
        LessonReview review = reviews.get(0);

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_addReview_daoExceptionAtAdd() {
        LessonReview review = reviews.get(1);
        User user = review.getUser();
        Lesson lesson = review.getLesson();

        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(reviewDao).add(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> reviewController.addReview(reviewDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_findReviewById() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(reviewDtoForAll).when(reviewMapper).toDto(review);
        Mockito.doReturn(review).when(reviewDao).findById(id);
        Assertions.assertEquals(reviewDtoForAll, reviewController.findLessonReviewById(id));
    }

    @Test
    void LessonReviewController_findReviewById_noReview() {
        String message = "Error at finding lesson review by id: no such review";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(reviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.findLessonReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_findReviewById_daoExceptionAtFind() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(reviewDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.findLessonReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonReviewController_findReviews() {
        Mockito.doReturn(reviewDtos).when(reviewMapper).listToDto(reviews);
        Mockito.doReturn(reviews).when(reviewDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertIterableEquals(reviewDtos, reviewController
                .findReviews(LessonReviewSortCriterion.NATURAL, null, null,
                        new Date(), new Date()));
    }

    @Test
    void LessonReviewController_findReviews_daoExceptionAtFindUser() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.findReviews(LessonReviewSortCriterion.NATURAL, id,
                        null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_findReviews_daoExceptionAtFindLesson() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(lessonDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.findReviews(LessonReviewSortCriterion.NATURAL, null,
                        id, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonReviewController_findReviews_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(reviewDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());

        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> reviewController.findReviews(LessonReviewSortCriterion.NATURAL,
                        null, null, new Date(), new Date()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_updateReview() {
        LessonReview review = reviews.get(0);
        User user = review.getUser();
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewDao).findById(review.getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated lesson review");
        Assertions.assertEquals(messageDtoForAll,
                reviewController.updateLessonReview(reviewDtoForAll, id));
    }

    @Test
    void LessonReviewController_updateReview_lessonIsNull() {
        String message = "Error at updating lesson review: lesson is null";
        LessonReview review = new LessonReview("Great", new Date(),
                new User(), null);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_messageIsNull() {
        String message = "Error at updating lesson review: message is null";
        LessonReview review = new LessonReview(null, new Date(), new User(), new Lesson());
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_dateIsNull() {
        String message = "Error at updating lesson review: date is null";
        LessonReview review = new LessonReview("Mes", null, new User(), new Lesson());
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_userIsNull() {
        String message = "Error at updating lesson review: user is null";
        LessonReview review = new LessonReview("Great", new Date(), null, new Lesson());
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_noLesson() {
        String message = "Error at updating lesson review: no such lesson";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewDao).findById(review.getId());
        Mockito.doReturn(null).when(lessonDao).findById(review.getLesson().getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_noUser() {
        String message = "Error at updating lesson review: no such user";
        LessonReview review = reviews.get(0);
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewDao).findById(review.getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(null).when(userDao).findById(review.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_daoExceptionAtFindLesson() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewDao).findById(review.getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findById(review.getLesson().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_updateReview_daoExceptionAtFindUser() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewDao).findById(review.getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(review.getUser().getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_updateReview_noReview() {
        String message = "Error at updating lesson review by id: no such review";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(reviewDao).findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_updateReview_daoExceptionAtFindReview() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(reviewDao)
                .findById(id);
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_updateReview_daoExceptionAtUpdate() {
        LessonReview review = reviews.get(1);
        User user = review.getUser();
        Lesson lesson = review.getLesson();
        Integer id = review.getId();

        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(lesson).when(lessonDao).findById(lesson.getId());
        Mockito.doReturn(review).when(reviewMapper).toEntity(reviewDtoForAll);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(reviewDao)
                .update(review);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> reviewController.updateLessonReview(reviewDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonReviewController_deleteReviewById() {
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(reviewDtoForAll).when(reviewMapper).toDto(review);
        Mockito.doReturn(review).when(reviewDao).findById(id);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully deleted lesson review");
        Assertions.assertEquals(messageDtoForAll, reviewController.deleteLessonReviewById(id));
    }

    @Test
    void LessonReviewController_deleteReviewById_noReview() {
        String message = "Error at deleting lesson review by id: no such review";
        LessonReview review = reviews.get(0);
        Integer id = review.getId();

        Mockito.doReturn(null).when(reviewDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> reviewController.deleteLessonReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonReviewController_deleteReviewById_daoExceptionAtFind() {
        Integer id = 2;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(reviewDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> reviewController.deleteLessonReviewById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
