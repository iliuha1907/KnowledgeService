package com.senla.training.knowledgeservice.service.service.review.course;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseReviewSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.review.course.CourseReviewDao;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.review.CourseReview;
import com.senla.training.knowledgeservice.model.review.CourseReview_;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.service.service.EntityOperation;
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
public class CourseReviewServiceImpl implements CourseReviewService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CourseReviewDao courseReviewDao;
    @Autowired
    private CourseDao courseDao;

    @Override
    @Transactional
    public void addReview(@Nonnull CourseReview courseReview) {
        checkReviewForNulls(courseReview, EntityOperation.ADDING);
        Course course = courseDao.findById(courseReview.getCourse().getId());
        User user = userDao.findById(courseReview.getUser().getId());
        checkReviewForForeignEntities(course, user, EntityOperation.ADDING);
        courseReview.setCourse(course);
        courseReview.setUser(user);
        courseReview.setId(null);
        courseReviewDao.add(courseReview);
    }

    @Override
    @Transactional
    @Nonnull
    public CourseReview findReviewById(@Nonnull Integer id) {
        CourseReview courseReview = courseReviewDao.findById(id);
        if (courseReview == null) {
            throw new BusinessException("Error at finding course review by id:"
                    + " no such review");
        }
        return courseReview;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<CourseReview> findReviews(
            @Nonnull CourseReviewSortCriterion criterion,
            @Nullable Integer userId,
            @Nullable Integer courseId,
            @Nullable Date startDate,
            @Nullable Date endDate) {
        List<EqualQueryHandler<CourseReview, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<CourseReview, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, userId, courseId);
        formCompareHandlerList(compareParameters, startDate, endDate);
        return courseReviewDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateReview(@Nonnull CourseReview courseReview, @Nonnull Integer id) {
        checkReviewForNulls(courseReview, EntityOperation.UPDATING);
        if (courseReviewDao.findById(id) == null) {
            throw new BusinessException("Error at updating course review by id:"
                    + " no such review");
        }
        Course course = courseDao.findById(courseReview.getCourse().getId());
        User user = userDao.findById(courseReview.getUser().getId());
        checkReviewForForeignEntities(course, user, EntityOperation.UPDATING);
        courseReview.setCourse(course);
        courseReview.setUser(user);
        courseReview.setId(id);
        courseReviewDao.update(courseReview);
    }

    @Override
    @Transactional
    public void deleteReviewById(@Nonnull Integer id) {
        CourseReview courseReview = courseReviewDao.findById(id);
        if (courseReview == null) {
            throw new BusinessException("Error at deleting course review by id:"
                    + " no such review");
        }
        courseReviewDao.delete(courseReview);
    }

    private void checkReviewForNulls(@Nonnull CourseReview review,
                                     @Nonnull EntityOperation operation) {
        if (review.getMessage() == null) {
            throw new BusinessException("Error at " + operation
                    + " course review: message is null");
        }
        if (review.getReviewDate() == null) {
            throw new BusinessException("Error at " + operation
                    + " course review: date is null");
        }
        if (review.getUser() == null) {
            throw new BusinessException("Error at " + operation
                    + " course review: user is null");
        }
        if (review.getCourse() == null) {
            throw new BusinessException("Error at " + operation
                    + " course review: course is null");
        }
    }

    private void checkReviewForForeignEntities(@Nullable Course course,
                                               @Nullable User user,
                                               @Nonnull EntityOperation operation) {
        if (user == null) {
            throw new BusinessException("Error at " + operation
                    + " course review: no such user");
        }
        if (course == null) {
            throw new BusinessException("Error at " + operation
                    + " course review: no such course");
        }
    }

    private void formEqualHandlersList(@Nonnull List<EqualQueryHandler<CourseReview, ?>> equalParameters,
                                       @Nullable Integer userId,
                                       @Nullable Integer courseId) {
        if (userId != null) {
            equalParameters.add(new EqualQueryHandler<>(CourseReview_.user,
                    userDao.findById(userId)));
        }
        if (courseId != null) {
            equalParameters.add(new EqualQueryHandler<>(CourseReview_.course,
                    courseDao.findById(courseId)));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<CourseReview, Y> extractSortField(
            @Nullable CourseReviewSortCriterion criterion) {
        if (CourseReviewSortCriterion.DATE.equals(criterion)) {
            return (SingularAttribute<CourseReview, Y>) CourseReview_.reviewDate;
        } else if (!CourseReviewSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding course reviews"
                    + " by criterion: invalid criterion");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> void formCompareHandlerList(
            @Nonnull List<CompareQueryHandler<CourseReview, Y>> compareParameters,
            @Nullable Date startDate,
            @Nullable Date endDate) {
        if (startDate != null) {
            compareParameters.add(
                    (CompareQueryHandler<CourseReview, Y>) new CompareQueryHandler<>(
                    CourseReview_.reviewDate, startDate, QuerySortOperation.GREATER));
        }
        if (endDate != null) {
            compareParameters.add(
                    (CompareQueryHandler<CourseReview, Y>) new CompareQueryHandler<>(
                    CourseReview_.reviewDate, endDate, QuerySortOperation.LESS));
        }
    }
}
