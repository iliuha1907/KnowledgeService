package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.common.util.DateAddingValueType;
import com.senla.training.knowledgeservice.common.util.DateUtil;
import com.senla.training.knowledgeservice.controller.controller.LessonSubscriptionController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.subscription.course.CourseSubscriptionDao;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.teacher.TeacherDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.dto.subscription.LessonSubscriptionDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.subscription.LessonSubscriptionMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ControllerTestConfigurator.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LessonSubscriptionControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private List<LessonSubscription> subscriptions;
    private List<LessonSubscriptionDto> subscriptionDtos;
    @Autowired
    private LessonSubscriptionController subscriptionController;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;
    @Autowired
    private LessonSubscriptionMapper lessonSubscriptionMapper;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private CourseSubscriptionDao courseSubscriptionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private CurrentUserProvider currentUserProvider;
    private MessageDto messageDtoForAll;
    private CourseSubscription longCourseSubscription;
    private CourseSubscription wrongCourseSubscription;
    private LessonSubscription subscriptionFindNull;
    private LessonSubscription subscriptionFindException;
    private LessonSubscriptionDto subscriptionDtoForAll;
    private User userFindOk;
    private User userFindNull;
    private User userFindException;
    private Lesson lessonFindOk;
    private Lesson lessonFindNull;
    private Lesson lessonFindException;
    private Teacher teacherFindOk;
    private Teacher teacherFindNull;
    private Teacher teacherFindException;

    @BeforeAll
    public void setUp() {
        Date date = new Date();
        User user = new User(4, "log", "pass", RoleType.ROLE_USER,
                new UserProfile());
        longCourseSubscription = new CourseSubscription(
                DateUtil.addPeriodToDate(date, DateAddingValueType.DAYS, -5),
                DateUtil.addPeriodToDate(date, DateAddingValueType.DAYS, 5),
                new User(), new Course());
        wrongCourseSubscription = new CourseSubscription(
                DateUtil.addPeriodToDate(date, DateAddingValueType.DAYS, +5),
                DateUtil.addPeriodToDate(date, DateAddingValueType.DAYS, -5),
                new User(), new Course());
        LessonSubscription subscriptionOne = new LessonSubscription(4, date,
                false, user, new Lesson(), new Teacher());
        LessonSubscription subscriptionTwo = new LessonSubscription(5, date,
                false, user, new Lesson(), new Teacher());

        subscriptions = Arrays.asList(subscriptionOne, subscriptionTwo);
        subscriptionDtos = Arrays.asList(new LessonSubscriptionDto(), new LessonSubscriptionDto());
        setUpSubscriptions();
        setUpUsers();
        setUpTeachers();
        setUpLessons();
        setUpDtoMappers();
    }

    @Test
    void LessonController_addSubscription() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(null).when(lessonSubscriptionDao)
                .findSubscriptionByUserAndLesson(subscription.getUser(),
                        subscription.getLesson());
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        Assertions.assertEquals(messageDtoForAll,
                subscriptionController.addSubscription(subscriptionDtoForAll));
    }

    @Test
    void LessonSubscriptionController_addSubscription_lessonIsNull() {
        String message = "Error at adding lesson subscription: lesson is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                new User(), null, new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_teacherIsNull() {
        String message = "Error at adding lesson subscription: teacher is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                new User(), new Lesson(), null);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_userIsNull() {
        String message = "Error at adding lesson subscription: user is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                null, new Lesson(), new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_dateIsNull() {
        String message = "Error at adding lesson subscription: date is null";
        LessonSubscription subscription = new LessonSubscription(null, false,
                new User(), new Lesson(), new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_tookPlaceIsNull() {
        String message = "Error at adding lesson subscription: took place is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), null,
                new User(), new Lesson(), new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_noTeacher() {
        String message = "Error at adding lesson subscription: no such teacher";
        LessonSubscription subscription = new LessonSubscription(1, new Date(), false,
                userFindOk, lessonFindOk, teacherFindNull);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_noLesson() {
        String message = "Error at adding lesson subscription: no such lesson";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                userFindOk, lessonFindNull, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_noUser() {
        String message = "Error at adding lesson subscription: no such user";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                userFindNull, new Lesson(), teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_notUnique() {
        String message = "Error at adding lesson subscription:" +
                " subscription already exists";
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao)
                .findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_noCourseSubscription() {
        String message = "Error at adding lesson subscription:" +
                " no required course subscription";
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(null).when(lessonSubscriptionDao)
                .findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doReturn(null).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_wrongDate() {
        String message = "Error at adding lesson subscription:" +
                " incompatible dates for course subscription";
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(null).when(lessonSubscriptionDao)
                .findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doReturn(wrongCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_teacherInActive() {
        String message = "Error at adding lesson subscription: teacher should be active";
        Teacher teacher = new Teacher(4, "A", "B", BigDecimal.TEN,
                RewardType.FIXED, false);
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacher);

        Mockito.doReturn(teacher).when(teacherDao).findById(teacher.getId());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_addSubscription_daoExceptionAtFindTeacher() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindException);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_addSubscription_daoExceptionAtFindUser() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindException, lessonFindOk, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_addSubscription_daoExceptionAtFindLesson() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindException, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_addSubscription_daoExceptionAtFindSubscription() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).findSubscriptionByUserAndLesson(
                subscription.getUser(), subscription.getLesson());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_addSubscription_daoExceptionAtFindCourseSubscription() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(null)
                .when(lessonSubscriptionDao).findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).findSubscriptionByUserAndCourse(user,
                lesson.getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_addSubscription_daoExceptionAtAdd() {
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(null)
                .when(lessonSubscriptionDao).findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).add(subscription);
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_findSubscriptionById() {
        LessonSubscription subscription = subscriptions.get(0);
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), subscription.getUser().getId(), RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(subscriptionDtoForAll).when(lessonSubscriptionMapper).toDto(subscription);
        Assertions.assertEquals(subscriptionDtoForAll,
                subscriptionController.findLessonSubscriptionById(subscription.getId()));
    }

    @Test
    void LessonSubscriptionController_findSubscriptionById_noSubscription() {
        String message = "Error at finding lesson subscription by id:" +
                " no such subscription";
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), subscriptionFindNull.getUser().getId(), RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findLessonSubscriptionById(subscriptionFindNull
                        .getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_findSubscriptionById_exceptionAtExtractUser() {
        String message = "Error at extracting current user";
        LessonSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findLessonSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_findSubscriptionById_noRights() {
        String message = "Error at finding lesson subscription by id:"
                + " id of user in subscription is different from given";
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 10, RoleType.ROLE_USER);
        LessonSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(id);
        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findLessonSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_findSubscriptionById_daoExceptionAtFind() {
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), subscriptionFindException.getUser().getId(), RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.findLessonSubscriptionById(
                        subscriptionFindException.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonSubscriptionController_findSubscriptions() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(subscriptions).when(lessonSubscriptionDao)
                .findSortedEntities(Matchers.eq(null), Matchers.anyList(), Matchers.anyList());
        Mockito.doReturn(subscriptionDtos).when(lessonSubscriptionMapper).listToDto(subscriptions);
        Assertions.assertIterableEquals(subscriptionDtos, subscriptionController
                .findSubscriptions(LessonSubscriptionSortCriterion.NATURAL, date, date,
                        null, null, null, null));
    }

    @Test
    void LessonSubscriptionController_findSubscriptions_exceptionAtExtractUser() {
        String message = "Error at extracting";
        Date date = new Date();

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findSubscriptions(
                        LessonSubscriptionSortCriterion.NATURAL, date, date,
                        null, null, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_findSubscriptions_wrongUserRole() {
        String message = "Error at finding lesson subscriptions: wrong user role";
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass", new HashSet<>(),
                1, null);

        Mockito.doReturn(userDetails).when(currentUserProvider).extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findSubscriptions(
                        LessonSubscriptionSortCriterion.NATURAL, date, date,
                        null, null, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_findSubscriptions_daoExceptionAtFindUser() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> subscriptionController.findSubscriptions(
                        LessonSubscriptionSortCriterion.NATURAL, date, date,
                        userFindException.getId(), null, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_findSubscriptions_daoExceptionAtFindLesson() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> subscriptionController.findSubscriptions(
                        LessonSubscriptionSortCriterion.NATURAL, date, date,
                        null, lessonFindException.getId(), null, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_findSubscriptions_daoExceptionAtFindTeacher() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> subscriptionController.findSubscriptions(
                        LessonSubscriptionSortCriterion.NATURAL, date, date,
                        null, null, teacherFindException.getId(), null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void LessonSubscriptionController_findSubscriptions_daoExceptionAtFindAll() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> subscriptionController.findSubscriptions(
                        LessonSubscriptionSortCriterion.NATURAL, date, date, null,
                        null, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        Assertions.assertEquals(messageDtoForAll, subscriptionController
                .updateLessonSubscription(subscriptionDtoForAll, subscription.getId()));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_wrongTookPlace() {
        String message = "Error at updating lesson subscription: took place is null";
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                null, userFindOk, lessonFindOk, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(subscription.getUser(),
                        subscription.getLesson().getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_lessonIsNull() {
        String message = "Error at updating lesson subscription: lesson is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                new User(), null, new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_teacherIsNull() {
        String message = "Error at updating lesson subscription: teacher is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                new User(), new Lesson(), null);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_userIsNull() {
        String message = "Error at updating lesson subscription: user is null";
        LessonSubscription subscription = new LessonSubscription(new Date(), false,
                null, new Lesson(), new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_dateIsNull() {
        String message = "Error at updating lesson subscription: date is null";
        LessonSubscription subscription = new LessonSubscription(null, false,
                new User(), new Lesson(), new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_tookPlaceIsNull() {
        String message = "Error at updating lesson subscription: took place is null";
        LessonSubscription subscription = new LessonSubscription(1, new Date(), null,
                new User(), new Lesson(), new Teacher());

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_teacherInActive() {
        String message = "Error at updating lesson subscription: teacher should be active";
        Teacher teacher = new Teacher(4, "A", "B", BigDecimal.TEN,
                RewardType.FIXED, false);
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacher);

        Mockito.doReturn(teacher).when(teacherDao).findById(teacher.getId());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(subscriptionDtoForAll,
                        subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionServiceImpl_updateSubscription_noSubscription() {
        String message = "Error at updating lesson subscription: no such subscription";

        Mockito.doReturn(subscriptionFindNull).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscriptionFindNull.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_noTeacher() {
        String message = "Error at updating lesson subscription: no such teacher";
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindOk, lessonFindOk, teacherFindNull);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_noLesson() {
        String message = "Error at updating lesson subscription: no such lesson";
        LessonSubscription subscription = new LessonSubscription(1, new Date(), false,
                userFindOk, lessonFindNull, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_noUser() {
        String message = "Error at updating lesson subscription: no such user";
        LessonSubscription subscription = new LessonSubscription(1, new Date(), false,
                userFindNull, new Lesson(), teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_noCourseSubscription() {
        String message = "Error at updating lesson subscription:"
                + "no course subscription for user and course";
        LessonSubscription subscription = new LessonSubscription(1, new Date(), false,
                userFindOk, lessonFindOk, teacherFindOk);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(id);
        Mockito.doReturn(null).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(subscription.getUser(),
                        subscription.getLesson().getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(subscriptionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_wrongCourseSubscription() {
        String message = "Error at updating lesson subscription:"
                + "no course subscription for user and course";
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(wrongCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(subscription.getUser(),
                        subscription.getLesson().getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtFindTeacher() {
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindOk, lessonFindOk, teacherFindException);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtFindUser() {
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindException, lessonFindOk, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtFindLesson() {
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindOk, lessonFindException, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtFindSubscription() {
        Mockito.doReturn(subscriptionFindException).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscriptionFindException.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtTeacherUpdate() {
        LessonSubscription subscriptionNew = new LessonSubscription(new Date(),
                true, userFindOk, lessonFindOk, teacherFindOk);
        LessonSubscription subscriptionOld = new LessonSubscription();
        subscriptionOld.setTookPlace(!subscriptionNew.getTookPlace());

        Mockito.doReturn(subscriptionOld).when(lessonSubscriptionDao)
                .findById(subscriptionNew.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).update(subscriptionNew.getTeacher());
        Mockito.doReturn(subscriptionNew).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscriptionNew.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtFindCourseSubscription() {
        LessonSubscription subscription = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(subscription.getUser(),
                        subscription.getLesson().getCourse());
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtCheckUnique() {
        User user = new User();
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, user, lessonFindOk, teacherFindOk);
        Lesson lesson = subscription.getLesson();
        user.setId(-1);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(userFindOk).when(userDao).findById(subscription.getUser().getId());
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(userFindOk, lesson.getCourse());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).findSubscriptionByUserAndLesson(userFindOk, lesson);
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper).toEntity(
                subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionServiceImpl_updateSubscription_notUniqueSubscription() {
        String message = "Error at updating lesson subscription:" +
                " subscription for new user and lesson exists";
        LessonSubscription subscriptionOne = new LessonSubscription(new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        LessonSubscription subscriptionTwo = subscriptions.get(1);
        User user = subscriptionOne.getUser();
        Lesson lesson = subscriptionOne.getLesson();

        Mockito.doReturn(subscriptionTwo).when(lessonSubscriptionDao).findById(
                subscriptionOne.getId());
        Mockito.doReturn(subscriptionTwo).when(lessonSubscriptionDao)
                .findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doReturn(subscriptionOne).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.updateLessonSubscription(
                        subscriptionDtoForAll, subscriptionOne.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_updateSubscription_daoExceptionAtUpdate() {
        LessonSubscription subscription = new LessonSubscription(1, new Date(),
                false, userFindOk, lessonFindOk, teacherFindOk);
        Integer id = subscription.getId();
        User user = subscription.getUser();
        Lesson lesson = subscription.getLesson();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(id);
        Mockito.doReturn(null)
                .when(lessonSubscriptionDao).findSubscriptionByUserAndLesson(user, lesson);
        Mockito.doReturn(longCourseSubscription).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).update(subscription);
        Mockito.doReturn(subscription).when(lessonSubscriptionMapper)
                .toEntity(subscriptionDtoForAll);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.updateLessonSubscription(subscriptionDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void LessonSubscriptionController_deleteSubscriptionById() {
        LessonSubscription subscription = subscriptions.get(0);

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(subscription.getId());
        Mockito.doReturn(subscriptionDtoForAll).when(lessonSubscriptionMapper).toDto(subscription);
        Assertions.assertEquals(messageDtoForAll,
                subscriptionController.deleteLessonSubscriptionById(subscription.getId()));
    }

    @Test
    void LessonSubscriptionController_deleteSubscriptionById_noSubscription() {
        String message = "Error at deleting subscription: no such subscription";
        LessonSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(null).when(lessonSubscriptionDao).findById(id);
        Mockito.doReturn(subscriptionDtoForAll).when(lessonSubscriptionMapper).toDto(subscription);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.deleteLessonSubscriptionById(subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_deleteSubscriptionById_wrongTookPlace() {
        String message = "Error at deleting subscription: subscription took place";
        LessonSubscription subscription = new LessonSubscription(1, new Date(), true,
                new User(), new Lesson(), new Teacher());
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(id);
        Mockito.doReturn(subscriptionDtoForAll).when(lessonSubscriptionMapper).toDto(subscription);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.deleteLessonSubscriptionById(subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_deleteSubscriptionById_daoExceptionAtFind() {
        String message = "Error at deleting subscription: no such subscription";
        LessonSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doThrow(new IllegalStateException(message)).when(lessonSubscriptionDao)
                .findById(id);
        Mockito.doReturn(subscriptionDtoForAll).when(lessonSubscriptionMapper).toDto(subscription);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.deleteLessonSubscriptionById(subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void LessonSubscriptionController_deleteSubscriptionById_daoExceptionAtDelete() {
        LessonSubscription subscription = subscriptions.get(1);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(lessonSubscriptionDao).findById(id);
        Mockito.doReturn(subscriptionDtoForAll).when(lessonSubscriptionMapper).toDto(subscription);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao)
                .delete(subscription);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.deleteLessonSubscriptionById(subscription.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    private void setUpSubscriptions() {
        subscriptionFindNull = new LessonSubscription(-1, new Date(),
                false, new User(), new Lesson(), new Teacher());
        subscriptionFindException = new LessonSubscription(0, new Date(),
                false, new User(), new Lesson(), new Teacher());
        Mockito.doReturn(null).when(lessonSubscriptionDao)
                .findById(subscriptionFindNull.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).findById(subscriptionFindException.getId());
    }

    private void setUpLessons() {
        Course course = new Course();
        course.setId(1);
        lessonFindOk = new Lesson(1, "Title", "Lesson", LessonType.INDIVIDUAL,
                course);
        lessonFindException = new Lesson(2, "Title", "Lesson",
                LessonType.INDIVIDUAL, course);
        lessonFindNull = new Lesson(3, "Title", "Lesson", LessonType.INDIVIDUAL,
                course);
        Mockito.doReturn(lessonFindOk).when(lessonDao).findById(lessonFindOk.getId());
        Mockito.doReturn(null).when(lessonDao).findById(lessonFindNull.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(lessonDao).findById(lessonFindException.getId());
    }

    private void setUpTeachers() {
        teacherFindOk = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FLEXIBLE, true);
        teacherFindException = new Teacher(2, "Serge", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);
        teacherFindNull = new Teacher(3, "Nick", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);
        Mockito.doReturn(teacherFindOk).when(teacherDao).findById(teacherFindOk.getId());
        Mockito.doReturn(null).when(teacherDao).findById(teacherFindNull.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).findById(teacherFindException.getId());
    }

    private void setUpUsers() {
        UserProfile profile = new UserProfile();
        userFindOk = new User(1, "login", "pass", RoleType.ROLE_USER, profile);
        userFindException = new User(2, "login2", "pass2", RoleType.ROLE_USER,
                profile);
        userFindNull = new User(3, "login3", "pass3", RoleType.ROLE_USER, profile);
        Mockito.doReturn(userFindOk).when(userDao).findById(userFindOk.getId());
        Mockito.doReturn(null).when(userDao).findById(userFindNull.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(userFindException.getId());
    }

    private void setUpDtoMappers() {
        subscriptionDtoForAll = new LessonSubscriptionDto();
        messageDtoForAll = new MessageDto();
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added lesson subscription");
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated lesson subscription");
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully deleted lesson subscription");
    }
}
