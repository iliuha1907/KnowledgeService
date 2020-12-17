package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.CourseSubscriptionController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.subscription.course.CourseSubscriptionDao;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.dto.subscription.CourseSubscriptionDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.subscription.CourseSubscriptionMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
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
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class CourseSubscriptionControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<CourseSubscription> subscriptions;
    private static List<CourseSubscriptionDto> subscriptionDtos;
    private static CourseSubscriptionDto subscriptionDtoForAll;
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private CourseSubscriptionController subscriptionController;
    @Autowired
    private CourseSubscriptionMapper subscriptionMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private CourseSubscriptionDao subscriptionDao;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CurrentUserProvider currentUserProvider;

    @BeforeAll
    public static void setUp() {
        User userOne = new User(1, "login", "pass", RoleType.ROLE_USER,
                new UserProfile());
        Course courseOne = new Course(1, "Title", "Course", 3,
                BigDecimal.TEN, new Section());
        Course courseTwo = new Course(2, "Title", "Course", 3,
                BigDecimal.TEN, new Section());
        CourseSubscription subscriptionOne = new CourseSubscription(1, new Date(),
                new Date(), userOne, courseOne);
        CourseSubscription subscriptionTwo = new CourseSubscription(2, new Date(),
                new Date(), userOne, courseTwo);
        subscriptions = Arrays.asList(subscriptionOne, subscriptionTwo);
        subscriptionDtos = Arrays.asList(new CourseSubscriptionDto(),
                new CourseSubscriptionDto());
        subscriptionDtoForAll = new CourseSubscriptionDto();
    }

    @Test
    void CourseSubscriptionController_addSubscription() {
        CourseSubscription subscription = subscriptions.get(0);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null)
                .when(subscriptionDao).findSubscriptionByUserAndCourse(user, course);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added course subscription");
        Assertions.assertEquals(messageDtoForAll,
                subscriptionController.addSubscription(subscriptionDtoForAll));
    }

    @Test
    void CourseSubscriptionController_addSubscription_endDateIsNull() {
        String message = "Error at adding course subscription: end date is null";
        CourseSubscription subscription = new CourseSubscription(new Date(), null,
                new User(), new Course());

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_userIsNull() {
        String message = "Error at adding course subscription: user is null";
        CourseSubscription subscription = new CourseSubscription(new Date(), new Date(),
                null, new Course());

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_courseIsNull() {
        String message = "Error at adding course subscription: course is null";
        CourseSubscription subscription = new CourseSubscription(new Date(), new Date(),
                new User(), null);

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_startDateIsNull() {
        String message = "Error at adding course subscription: start date is null";
        CourseSubscription subscription = new CourseSubscription(null, new Date(),
                new User(), new Course());

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_noCourse() {
        String message = "Error at adding course subscription: no such course";
        CourseSubscription subscription = subscriptions.get(0);

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(null).when(courseDao).findById(subscription.getCourse().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_noUser() {
        String message = "Error at adding course subscription: no such user";
        CourseSubscription subscription = subscriptions.get(0);
        Course course = subscription.getCourse();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(null).when(userDao).findById(subscription.getUser().getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_notUniqueSubscription() {
        String message = "Error at adding course subscription: subscription already" +
                " exists";
        CourseSubscription subscription = subscriptions.get(0);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(subscription).when(subscriptionDao).findSubscriptionByUserAndCourse(user,
                course);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_addSubscription_daoExceptionAtFindCourse() {
        CourseSubscription subscription = subscriptions.get(0);
        Course course = subscription.getCourse();
        User user = subscription.getUser();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(courseDao).findById(course.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_addSubscription_daoExceptionAtFindUser() {
        CourseSubscription subscription = subscriptions.get(0);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(userDao).findById(user.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_addSubscription_daoExceptionAtFindSubscription() {
        CourseSubscription subscription = subscriptions.get(1);
        Course course = subscription.getCourse();
        User user = subscription.getUser();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(subscriptionDao).findSubscriptionByUserAndCourse(user, course);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_addSubscription_daoExceptionAtAdd() {
        CourseSubscription subscription = subscriptions.get(1);
        User user = subscription.getUser();
        Course course = subscription.getCourse();

        Mockito.doReturn(subscription).when(subscriptionMapper).toEntity(subscriptionDtoForAll);
        Mockito.doReturn(course).when(courseDao).findById(course.getId());
        Mockito.doReturn(user).when(userDao).findById(user.getId());
        Mockito.doReturn(null)
                .when(subscriptionDao).findSubscriptionByUserAndCourse(user, course);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(subscriptionDao).add(subscription);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController.addSubscription(subscriptionDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_findSubscriptionById() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), subscription.getUser().getId(), RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(subscriptionDtoForAll).when(subscriptionMapper).toDto(subscription);
        Mockito.doReturn(subscription).when(subscriptionDao).findById(id);
        Assertions.assertEquals(subscriptionDtoForAll,
                subscriptionController.findCourseSubscriptionById(id));
    }

    @Test
    void CourseSubscriptionController_findSubscriptionById_noSubscription() {
        String message = "Error at finding course subscription by id:" +
                " no such subscription";
        CourseSubscription subscription = subscriptions.get(1);
        Integer id = subscription.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), subscription.getUser().getId(), RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(null).when(subscriptionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController.findCourseSubscriptionById(
                        id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_findSubscriptionById_exceptionAtExtractUser() {
        String message = "Error at extracting current user";
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_findSubscriptionById_daoExceptionAtFind() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), subscription.getUser().getId(), RoleType.ROLE_USER);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(subscriptionDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> subscriptionController
                        .findCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_findSubscriptionById_noRights() {
        String message = "Error at finding course subscription by id:"
                + " id of user in subscription is different from given";
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 10, RoleType.ROLE_USER);
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(subscriptionDao).findById(id);
        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseSubscriptionController_findSubscriptions() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doReturn(subscriptionDtos).when(subscriptionMapper).listToDto(subscriptions);
        Mockito.doReturn(subscriptions).when(subscriptionDao)
                .findSortedEntities(Matchers.eq(null), Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> subscriptionController
                .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL, date, date,
                        date, date, null, null));
    }

    @Test
    void CourseSubscriptionController_findSubscriptions_exceptionAtExtractUser() {
        String message = "Error at extracting";
        Date date = new Date();

        Mockito.doThrow(new BusinessException(message)).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findSubscriptions(
                        CourseSubscriptionSortCriterion.NATURAL, date, date, date, date,
                        null, null));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_findSubscriptions_wrongUserRole() {
        String message = "Error at course subscriptions: wrong user role";
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, null);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> subscriptionController.findSubscriptions(
                        CourseSubscriptionSortCriterion.NATURAL, date, date, date, date,
                        null, null));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_findSubscriptions_daoExceptionAtFindUser() {
        Date date = new Date();
        Integer id = 0;
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(userDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController
                        .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL, date, date,
                                date, date, id, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_findSubscriptions_daoExceptionAtFindCourse() {
        Date date = new Date();
        Integer id = 0;
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(courseDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> subscriptionController
                        .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL, date, date,
                                date, date, null, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void CourseSubscriptionController_findSubscriptions_daoExceptionAtFindAll() {
        Date date = new Date();
        FullUserDetails userDetails = new FullUserDetails("user", "pass",
                new HashSet<>(), 1, RoleType.ROLE_ADMIN);

        Mockito.doReturn(userDetails).when(currentUserProvider)
                .extractFullUserDetailsFromCurrentUser();
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(subscriptionDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> subscriptionController
                        .findSubscriptions(CourseSubscriptionSortCriterion.NATURAL, date, date,
                                date, date, 1, 1));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_deleteSubscriptionById() {
        CourseSubscription subscription = subscriptions.get(0);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(subscriptionDao).findById(id);
        Mockito.doReturn(messageDtoForAll).when(messageDtoMapper)
                .toDto("Successfully deleted course subscription");
        Assertions.assertEquals(messageDtoForAll, subscriptionController
                .deleteCourseSubscriptionById(id));
    }

    @Test
    void CourseSubscriptionController_deleteSubscriptionById_noSubscription() {
        String message = "Error at deleting course subscription by id:" +
                " no such subscription";
        CourseSubscription subscription = subscriptions.get(1);
        Integer id = subscription.getId();

        Mockito.doReturn(null).when(subscriptionDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> subscriptionController
                        .deleteCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void CourseSubscriptionController_deleteSubscriptionById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(subscriptionDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> subscriptionController
                        .deleteCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_deleteSubscriptionById_daoExceptionAtDeleteLessonSubscription() {
        CourseSubscription subscription = subscriptions.get(1);
        Integer id = subscription.getId();

        Mockito.doReturn(subscription).when(subscriptionDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(lessonSubscriptionDao)
                .deleteByCourseAndUser(subscription.getCourse(), subscription.getUser());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> subscriptionController
                        .deleteCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void CourseSubscriptionController_deleteSubscriptionById_daoExceptionAtDeleteCourseSubscription() {
        CourseSubscription subscription = subscriptions.get(1);
        Integer id = subscription.getId();

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(subscriptionDao).delete(subscription);
        Mockito.doReturn(subscription).when(subscriptionDao).findById(id);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> subscriptionController
                        .deleteCourseSubscriptionById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

}
