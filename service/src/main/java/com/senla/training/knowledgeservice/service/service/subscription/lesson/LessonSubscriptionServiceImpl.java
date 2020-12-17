package com.senla.training.knowledgeservice.service.service.subscription.lesson;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.dao.subscription.course.CourseSubscriptionDao;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.teacher.TeacherDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription_;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.service.EntityOperation;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LessonSubscriptionServiceImpl implements LessonSubscriptionService {

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private CourseSubscriptionDao courseSubscriptionDao;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TeacherDao teacherDao;
    @Value("${teacher.reward:10}")
    private BigDecimal teacherReward;

    @Override
    @Transactional
    public void addSubscription(@Nonnull LessonSubscription lessonSubscription) {
        checkSubscriptionForNulls(lessonSubscription, EntityOperation.ADDING);
        User user = userDao.findById(lessonSubscription.getUser().getId());
        Lesson lesson = lessonDao.findById(lessonSubscription.getLesson().getId());
        Teacher teacher = teacherDao.findById(lessonSubscription.getTeacher().getId());
        checkSubscriptionForForeignEntities(user, lesson, teacher,
                EntityOperation.ADDING);
        if (!teacher.getActive()) {
            throw new BusinessException("Error at adding lesson subscription:"
                    + " teacher should be active");
        }
        if (lessonSubscriptionDao.findSubscriptionByUserAndLesson(user, lesson)
                != null) {
            throw new BusinessException("Error at adding lesson subscription:"
                    + " subscription already exists");
        }
        Course course = lesson.getCourse();
        CourseSubscription courseSubscription = courseSubscriptionDao
                .findSubscriptionByUserAndCourse(user, course);
        if (courseSubscription == null) {
            throw new BusinessException("Error at adding lesson subscription:"
                    + " no required course subscription");
        }
        Date date = lessonSubscription.getLessonDate();
        if (date.before(courseSubscription.getEndDate())
                && date.after(courseSubscription.getStartDate())) {
            lessonSubscription.setId(null);
            lessonSubscription.setLesson(lesson);
            lessonSubscription.setUser(user);
            lessonSubscription.setTeacher(teacher);
            lessonSubscriptionDao.add(lessonSubscription);
        } else {
            throw new BusinessException("Error at adding lesson subscription:"
                    + " incompatible dates for course subscription");
        }
    }

    @Override
    @Transactional
    @Nonnull
    public LessonSubscription findSubscriptionById(@Nonnull Integer id) {
        FullUserDetails currentUser = currentUserProvider
                .extractFullUserDetailsFromCurrentUser();
        LessonSubscription lessonSubscription = lessonSubscriptionDao.findById(id);
        if (lessonSubscription == null) {
            throw new BusinessException("Error at finding lesson subscription by id:"
                    + " no such subscription");
        }
        if (RoleType.ROLE_USER.equals(currentUser.getRoleType())
                && !lessonSubscription.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Error at finding lesson subscription by id:"
                    + " id of user in subscription is different from given");
        }
        return lessonSubscription;
    }

    @Override
    @Transactional
    @Nonnull
    public List<LessonSubscription> findSubscriptions(
            @Nonnull LessonSubscriptionSortCriterion criterion,
            @Nullable Date startDate,
            @Nullable Date endDate,
            @Nullable Integer userId,
            @Nullable Integer teacherId,
            @Nullable Integer lessonId,
            @Nullable Boolean tookPlace) {
        Integer actualId = extractActualId(userId);
        List<EqualQueryHandler<LessonSubscription, ?>> equalParameters =
                new ArrayList<>();
        List<CompareQueryHandler<LessonSubscription, Date>> compareParameters =
                new ArrayList<>();
        formEqualHandlersList(equalParameters, actualId, teacherId, lessonId,
                tookPlace);
        formCompareHandlerList(compareParameters, startDate, endDate);
        return lessonSubscriptionDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateSubscription(@Nonnull LessonSubscription lessonSubscription,
                                   @Nonnull Integer id) {
        checkSubscriptionForNulls(lessonSubscription, EntityOperation.UPDATING);
        LessonSubscription existing = lessonSubscriptionDao.findById(id);
        if (existing == null) {
            throw new BusinessException("Error at updating lesson subscription:"
                    + " no such subscription");
        }
        User user = userDao.findById(lessonSubscription.getUser().getId());
        Lesson lesson = lessonDao.findById(lessonSubscription.getLesson().getId());
        Teacher teacher = teacherDao.findById(lessonSubscription.getTeacher().getId());
        checkSubscriptionForForeignEntities(user, lesson, teacher,
                EntityOperation.UPDATING);
        if (!teacher.getActive()) {
            throw new BusinessException("Error at updating lesson subscription:"
                    + " teacher should be active");
        }
        handleTeacherReward(existing, lessonSubscription, teacher);
        Date lessonDate = lessonSubscription.getLessonDate();
        CourseSubscription courseSubscription = courseSubscriptionDao
                .findSubscriptionByUserAndCourse(user, lesson.getCourse());
        if (courseSubscription == null || !(lessonDate.before(courseSubscription
                .getEndDate()) && lessonDate.after(courseSubscription
                .getStartDate()))) {
            throw new BusinessException("Error at updating lesson subscription:"
                    + "no course subscription for user and course");
        }
        if (!user.equals(existing.getUser()) || !lesson.equals(existing.getLesson())) {
            if (lessonSubscriptionDao.findSubscriptionByUserAndLesson(user, lesson)
                    != null) {
                throw new BusinessException("Error at updating lesson subscription:"
                        + " subscription for new user and lesson exists");
            }
        }
        lessonSubscription.setId(id);
        lessonSubscription.setUser(user);
        lessonSubscription.setTeacher(teacher);
        lessonSubscription.setLesson(lesson);
        lessonSubscriptionDao.update(lessonSubscription);
    }

    @Override
    @Transactional
    public void deleteSubscriptionById(@Nonnull Integer id) {
        LessonSubscription subscription = lessonSubscriptionDao.findById(id);
        if (subscription == null) {
            throw new BusinessException("Error at deleting subscription:"
                    + " no such subscription");
        }
        if (subscription.getTookPlace()) {
            throw new BusinessException("Error at deleting subscription:"
                    + " subscription took place");
        }
        lessonSubscriptionDao.delete(subscription);
    }

    private void checkSubscriptionForNulls(@Nonnull LessonSubscription subscription,
                                           @Nonnull EntityOperation operation) {
        if (subscription.getLessonDate() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: date is null");
        }
        if (subscription.getTookPlace() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: took place is null");
        }
        if (subscription.getUser() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: user is null");
        }
        if (subscription.getLesson() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: lesson is null");
        }
        if (subscription.getTeacher() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: teacher is null");
        }
    }

    private void checkSubscriptionForForeignEntities(@Nullable User user,
                                                     @Nullable Lesson lesson,
                                                     @Nullable Teacher teacher,
                                                     @Nonnull EntityOperation operation) {
        if (user == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: no such user");
        }
        if (lesson == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: no such lesson");
        }
        if (teacher == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson subscription: no such teacher");
        }
    }

    private Integer extractActualId(Integer possibleUserId) {
        FullUserDetails currentUser = currentUserProvider
                .extractFullUserDetailsFromCurrentUser();
        RoleType userRole = currentUser.getRoleType();
        if (RoleType.ROLE_USER.equals(userRole)) {
            return currentUser.getId();
        } else if (RoleType.ROLE_ADMIN.equals(userRole)) {
            return possibleUserId;
        } else {
            throw new BusinessException("Error at finding lesson subscriptions:"
                    + " wrong user role");
        }
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<LessonSubscription, ?>> equalParameters,
            @Nullable Integer userId,
            @Nullable Integer teacherId,
            @Nullable Integer lessonId,
            @Nullable Boolean tookPlace) {
        if (userId != null) {
            equalParameters.add(new EqualQueryHandler<>(LessonSubscription_.user,
                    userDao.findById(userId)));
        }
        if (teacherId != null) {
            equalParameters.add(new EqualQueryHandler<>(LessonSubscription_.teacher,
                    teacherDao.findById(teacherId)));
        }
        if (lessonId != null) {
            equalParameters.add(new EqualQueryHandler<>(LessonSubscription_.lesson,
                    lessonDao.findById(lessonId)));
        }
        if (tookPlace != null) {
            equalParameters.add(new EqualQueryHandler<>(LessonSubscription_.tookPlace, tookPlace));
        }
    }

    private void formCompareHandlerList(
            @Nonnull List<CompareQueryHandler<LessonSubscription, Date>> parameters,
            @Nullable Date startDate,
            @Nullable Date endDate) {
        if (startDate != null) {
            parameters.add(
                    new CompareQueryHandler<>(LessonSubscription_.lessonDate,
                            startDate, QuerySortOperation.GREATER));
        }
        if (endDate != null) {
            parameters.add(
                    new CompareQueryHandler<>(LessonSubscription_.lessonDate,
                            endDate, QuerySortOperation.LESS));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<LessonSubscription, Y> extractSortField(
            @Nonnull LessonSubscriptionSortCriterion criterion) {
        if (LessonSubscriptionSortCriterion.DATE.equals(criterion)) {
            return (SingularAttribute<LessonSubscription, Y>)
                    LessonSubscription_.lessonDate;
        } else if (!LessonSubscriptionSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding lesson"
                    + " subscriptions by criterion: invalid criterion");
        }
        return null;
    }

    private void handleTeacherReward(@Nonnull LessonSubscription existing,
                                     @Nonnull LessonSubscription lessonSubscription,
                                     @Nonnull Teacher teacher) {
        Boolean existingTookPlace = existing.getTookPlace();
        Boolean newTookPlace = lessonSubscription.getTookPlace();
        if (newTookPlace == null) {
            throw new BusinessException("Error at updating lesson subscription:"
                    + " wrong took place status");
        }
        if (!newTookPlace && existingTookPlace) {
            throw new BusinessException("Error at updating lesson subscription:"
                    + " took place can not be updated anymore");
        }
        if (RewardType.FLEXIBLE.equals(teacher.getRewardType())
                && !existingTookPlace.equals(newTookPlace)) {
            teacher.setReward(teacher.getReward().add(teacherReward));
            teacherDao.update(teacher);
        }
    }
}
