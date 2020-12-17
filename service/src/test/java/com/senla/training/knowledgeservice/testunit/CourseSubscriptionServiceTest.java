package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.subscription.course.CourseSubscriptionDao;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import com.senla.training.knowledgeservice.service.service.subscription.course.CourseSubscriptionService;
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
import java.util.HashSet;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class CourseSubscriptionServiceTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<CourseSubscription> subscriptions;
    @Autowired
    private CourseSubscriptionService subscriptionService;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseSubscriptionDao courseSubscriptionDao;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @BeforeAll
    public static void setUp() {
        User userOne = new User(1, "login", "pass", RoleType.ROLE_USER,
                new UserProfile());
        Course courseOne = new Course(1, "Title", "Course", 3,
                BigDecimal.TEN, new Section());
        Course courseTwo = new Course(1, "Title", "Course", 3,
                BigDecimal.TEN, new Section());
        CourseSubscription subscriptionOne = new CourseSubscription(1, new Date(),
                new Date(), userOne, courseOne);
        CourseSubscription subscriptionTwo = new CourseSubscription(2, new Date(),
                new Date(), userOne, courseTwo);
        subscriptions = Arrays.asList(subscriptionOne, subscriptionTwo);
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription() {
        CourseSubscription subscription = subscriptions.get(0);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(courseSubscriptionDao)
                .findSubscriptionByUserAndCourse(user, course);
        Assertions.assertDoesNotThrow(() ->
                subscriptionService.addSubscription(subscription));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_startDateIsNull() {
        String message = "Error at adding course subscription: start date is null";
        CourseSubscription subscription = new CourseSubscription(null,
                new Date(), new User(), new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_endDateIsNull() {
        String message = "Error at adding course subscription: end date is null";
        CourseSubscription subscription = new CourseSubscription(new Date(),
                null, new User(), new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_userIsNull() {
        String message = "Error at adding course subscription: user is null";
        CourseSubscription subscription = new CourseSubscription(new Date(), new Date(),
                null, new Course());

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_courseIsNull() {
        String message = "Error at adding course subscription: course is null";
        CourseSubscription subscription = new CourseSubscription(new Date(), new Date(),
                new User(), null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_noCourse() {
        String message = "Error at adding course subscription: no such course";
        CourseSubscription subscription = subscriptions.get(0);

        Mockito.doReturn(null).when(courseDao).findById(
                subscription.getCourse().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_noUser() {
        String message = "Error at adding course subscription: no such user";
        CourseSubscription subscription = subscriptions.get(0);
        Course course = subscription.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(userDao).findById(subscription.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_notUniqueSubscription() {
        String message = "Error at adding course subscription: subscription already"
                + " exists";
        CourseSubscription subscription = subscriptions.get(0);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(subscription)
                .when(courseSubscriptionDao).findSubscriptionByUserAndCourse(user,
                course);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_daoExceptionAtFindCourse() {
        CourseSubscription subscription = subscriptions.get(0);
        Course course = subscription.getCourse();

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(course.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_daoExceptionAtFindUser() {
        CourseSubscription subscription = subscriptions.get(0);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(user.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_daoExceptionAtFindSubscription() {
        CourseSubscription subscription = subscriptions.get(0);
        Course course = subscription.getCourse();
        User user = subscription.getUser();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).findSubscriptionByUserAndCourse(user,
                course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_addSubscription_daoExceptionAtAdd() {
        CourseSubscription subscription = subscriptions.get(1);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(null)
                .when(courseSubscriptionDao).findSubscriptionByUserAndCourse(user,
                course);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).add(subscription);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionService.addSubscription(subscription));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_findSubscriptionById() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), subscription.getUser().getId(),
                RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(subscription).when(courseSubscriptionDao).findById(id);
        Assertions.assertEquals(subscription, subscriptionService
                .findSubscriptionById(id));
    }

    @Test
    void CourseSubscriptionImpl_findSubscriptionById_noSubscription() {
        String message = "Error at finding course subscription by id:" +
                " no such subscription";
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), subscription.getUser().getId(),
                RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(null).when(courseSubscriptionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.findSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionImpl_findSubscriptionById_exceptionAtExtractUser() {
        String message = "Error at extracting current user";
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.findSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionImpl_findSubscriptionById_noRights() {
        String message = "Error at finding course subscription by id:"
                + " id of user in subscription is different from given";
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), 10, RoleType.ROLE_USER);
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(courseSubscriptionDao).findById(id);
        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.findSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_findSubscriptionById_daoExceptionAtFind() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), subscription.getUser().getId(),
                RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionService.findSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseSubscriptionServiceImpl_findSubscriptions() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(subscriptions).when(courseSubscriptionDao)
                .findSortedEntities(Matchers.eq(null), Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> subscriptionService
                .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL,
                        date, date, date, date, null, null));
    }

    @Test
    void CourseSubscriptionServiceImpl_findSubscriptions_exceptionAtExtractUser() {
        String message = "Error at extracting";
        Date date = new Date();

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.findSubscriptions(
                        CourseSubscriptionSortCriterion.NATURAL, date, date, date,
                        date, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_findSubscriptions_wrongUserRole() {
        String message = "Error at course subscriptions: wrong user role";
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), 1, null);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.findSubscriptions(
                        CourseSubscriptionSortCriterion.NATURAL, date, date, date,
                        date, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_findSubscriptions_daoExceptionAtFindUser() {
        Date date = new Date();
        Integer id = 0;
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionService.findSubscriptions(
                        CourseSubscriptionSortCriterion.NATURAL, date, date, date,
                        date, id, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_findSubscriptions_daoExceptionAtFindCourse() {
        Date date = new Date();
        Integer id = 0;
        FullUserDetails userDetails = new FullUserDetails("user",
                "pass", new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionService
                        .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL,
                                date, date, date, date, null, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseSubscriptionServiceImpl_findSubscriptions_daoExceptionAtFindAll() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionService
                        .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL,
                                date, date, date, date, null, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_deleteSubscriptionById() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(courseSubscriptionDao).findById(id);
        Mockito.reset(lessonSubscriptionDao);
        Assertions.assertDoesNotThrow(() -> subscriptionService
                .deleteSubscriptionById(id));
    }

    @Test
    void CourseSubscriptionServiceImpl_deleteSubscriptionById_noSubscription() {
        String message = "Error at deleting course subscription by id:"
                + " no such subscription";
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(null).when(courseSubscriptionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionService.deleteSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionServiceImpl_deleteSubscriptionById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionService.deleteSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_deleteSubscriptionById_daoExceptionAtDeleteLessonSubscription() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(courseSubscriptionDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao)
                .deleteByCourseAndUser(subscription.getCourse(), subscription.getUser());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionService.deleteSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionServiceImpl_deleteSubscriptionById_daoExceptionAtDeleteCourseSubscription() {
        CourseSubscription subscription = subscriptions.get(1);
        Integer id = subscription.getId();

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(courseSubscriptionDao).delete(subscription);
        Mockito.doReturn(subscription).when(courseSubscriptionDao).findById(id);
        Mockito.reset(lessonSubscriptionDao);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionService.deleteSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
