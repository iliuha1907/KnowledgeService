package com.senla.training.knowledgeservice.service.service.subscription.course;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.common.util.DateAddingValueType;
import com.senla.training.knowledgeservice.common.util.DateUtil;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.dao.subscription.course.CourseSubscriptionDao;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription_;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import com.senla.training.knowledgeservice.service.security.FullUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseSubscriptionServiceImpl implements CourseSubscriptionService {

    @Autowired
    private CurrentUserProvider currentUserProvider;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseSubscriptionDao courseSubscriptionDao;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;

    @Override
    @Transactional
    public void addSubscription(@Nonnull CourseSubscription courseSubscription) {
        checkSubscriptionForNulls(courseSubscription);
        Course course = courseDao.findById(courseSubscription.getCourse().getId());
        User user = userDao.findById(courseSubscription.getUser().getId());
        checkSubscriptionForForeignEntities(course, user);
        if (courseSubscriptionDao.findSubscriptionByUserAndCourse(user, course)
                != null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " subscription already exists");
        }
        courseSubscription.setEndDate(DateUtil.addPeriodToDate(
                courseSubscription.getStartDate(), DateAddingValueType.WEEKS,
                course.getDuration()));
        courseSubscription.setCourse(course);
        courseSubscription.setUser(user);
        courseSubscription.setId(null);
        courseSubscriptionDao.add(courseSubscription);
    }

    @Override
    @Transactional
    @Nonnull
    public CourseSubscription findSubscriptionById(@Nonnull Integer id) {
        FullUserDetails currentUser = currentUserProvider
                .extractFullUserDetailsFromCurrentUser();
        CourseSubscription courseSubscription = courseSubscriptionDao.findById(id);
        if (courseSubscription == null) {
            throw new BusinessException("Error at finding course subscription"
                    + " by id: no such subscription");
        }
        if (!RoleType.ROLE_ADMIN.equals(currentUser.getRoleType())
                && !courseSubscription.getUser().getId().equals(currentUser.getId())) {
            throw new BusinessException("Error at finding course subscription" +
                    " by id: id of user in subscription is different from given");
        }
        return courseSubscription;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<CourseSubscription> findSubscriptions(
            @Nonnull CourseSubscriptionSortCriterion criterion,
            @Nullable Date beginDateForStart,
            @Nullable Date endDateForStart,
            @Nullable Date beginDateForEnd,
            @Nullable Date endDateForEnd,
            @Nullable Integer userId,
            @Nullable Integer courseId) {
        Integer actualId = extractActualId(userId);
        List<EqualQueryHandler<CourseSubscription, ?>> equalParameters =
                new ArrayList<>();
        List<CompareQueryHandler<CourseSubscription, Y>> compareParameters =
                new ArrayList<>();
        formEqualHandlersList(equalParameters, actualId, courseId);
        formCompareHandlerList(compareParameters, beginDateForStart,
                endDateForStart, beginDateForEnd, beginDateForEnd);
        return courseSubscriptionDao.findSortedEntities(extractSortField(criterion), equalParameters,
                compareParameters);
    }

    @Override
    @Transactional
    public void deleteSubscriptionById(@Nonnull Integer id) {
        CourseSubscription courseSubscription = courseSubscriptionDao.findById(id);
        if (courseSubscription == null) {
            throw new BusinessException("Error at deleting course subscription"
                    + " by id: no such subscription");
        }
        lessonSubscriptionDao.deleteByCourseAndUser(courseSubscription.getCourse(),
                courseSubscription.getUser());
        courseSubscriptionDao.delete(courseSubscription);
    }

    private void checkSubscriptionForNulls(@Nonnull CourseSubscription subscription) {
        if (subscription.getStartDate() == null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " start date is null");
        }
        if (subscription.getEndDate() == null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " end date is null");
        }
        if (subscription.getUser() == null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " user is null");
        }
        if (subscription.getCourse() == null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " course is null");
        }
    }

    private void checkSubscriptionForForeignEntities(@Nullable Course course,
                                                     @Nullable User user) {
        if (user == null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " no such user");
        }
        if (course == null) {
            throw new BusinessException("Error at adding course subscription:"
                    + " no such course");
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
            throw new BusinessException("Error at course subscriptions:"
                    + " wrong user role");
        }
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<CourseSubscription, ?>> equalParameters,
            @Nullable Integer userId,
            @Nullable Integer courseId) {
        if (userId != null) {
            equalParameters.add(new EqualQueryHandler<>(CourseSubscription_.user,
                    userDao.findById(userId)));
        }
        if (courseId != null) {
            equalParameters.add(new EqualQueryHandler<>(CourseSubscription_.course,
                    courseDao.findById(courseId)));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> void formCompareHandlerList(
            @Nonnull List<CompareQueryHandler<CourseSubscription, Y>> parameters,
            @Nullable Date beginDateForStart,
            @Nullable Date endDateForStart,
            @Nullable Date beginDateForEnd,
            @Nullable Date endDateForEnd) {
        if (beginDateForStart != null) {
            parameters.add((CompareQueryHandler<CourseSubscription, Y>)
                    new CompareQueryHandler<>(CourseSubscription_.startDate,
                            beginDateForStart, QuerySortOperation.GREATER));
        }
        if (endDateForStart != null) {
            parameters.add((CompareQueryHandler<CourseSubscription, Y>)
                    new CompareQueryHandler<>(CourseSubscription_.endDate,
                            endDateForStart, QuerySortOperation.LESS));
        }
        if (beginDateForEnd != null) {
            parameters.add((CompareQueryHandler<CourseSubscription, Y>)
                    new CompareQueryHandler<>(CourseSubscription_.endDate,
                            beginDateForEnd, QuerySortOperation.GREATER));
        }
        if (endDateForEnd != null) {
            parameters.add((CompareQueryHandler<CourseSubscription, Y>)
                    new CompareQueryHandler<>(CourseSubscription_.endDate,
                            endDateForEnd, QuerySortOperation.LESS));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<CourseSubscription, Y> extractSortField(
            @Nullable CourseSubscriptionSortCriterion criterion) {
        if (CourseSubscriptionSortCriterion.START_DATE.equals(criterion)) {
            return (SingularAttribute<CourseSubscription, Y>)
                    CourseSubscription_.startDate;
        } else if (CourseSubscriptionSortCriterion.END_DATE.equals(criterion)) {
            return (SingularAttribute<CourseSubscription, Y>)
                    CourseSubscription_.endDate;
        } else if (!CourseSubscriptionSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding course subscriptions"
                    + " by criterion: invalid criterion");
        }
        return null;
    }
}
