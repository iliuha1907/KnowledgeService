package com.senla.training.knowledgeservice.service.service.review.lesson;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonReviewSortCriterion;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.review.lesson.LessonReviewDao;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.review.LessonReview;
import com.senla.training.knowledgeservice.model.review.LessonReview_;
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
public class LessonReviewServiceImpl implements LessonReviewService {

    @Autowired
    private LessonReviewDao lessonReviewDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public void addReview(@Nonnull LessonReview lessonReview) {
        checkReviewForNulls(lessonReview, EntityOperation.ADDING);
        Lesson lesson = lessonDao.findById(lessonReview.getLesson().getId());
        User user = userDao.findById(lessonReview.getUser().getId());
        checkReviewForForeignEntities(lesson, user, EntityOperation.ADDING);
        lessonReview.setLesson(lesson);
        lessonReview.setUser(user);
        lessonReview.setId(null);
        lessonReviewDao.add(lessonReview);
    }

    @Override
    @Transactional
    @Nonnull
    public LessonReview findReviewById(@Nonnull Integer id) {
        LessonReview lessonReview = lessonReviewDao.findById(id);
        if (lessonReview == null) {
            throw new BusinessException("Error at finding lesson review by id:"
                    + " no such review");
        }
        return lessonReview;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<LessonReview> findReviews(
            @Nonnull LessonReviewSortCriterion criterion,
            @Nullable Integer userId,
            @Nullable Integer lessonId,
            @Nullable Date startDate,
            @Nullable Date endDate) {
        List<EqualQueryHandler<LessonReview, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<LessonReview, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, userId, lessonId);
        formCompareHandlerList(compareParameters, startDate, endDate);
        return lessonReviewDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateReview(@Nonnull LessonReview lessonReview, @Nonnull Integer id) {
        checkReviewForNulls(lessonReview, EntityOperation.UPDATING);
        if (lessonReviewDao.findById(id) == null) {
            throw new BusinessException("Error at updating lesson review by id:"
                    + " no such review");
        }
        Lesson lesson = lessonDao.findById(lessonReview.getLesson().getId());
        User user = userDao.findById(lessonReview.getUser().getId());
        checkReviewForForeignEntities(lesson, user, EntityOperation.UPDATING);
        lessonReview.setLesson(lesson);
        lessonReview.setUser(user);
        lessonReview.setId(id);
        lessonReviewDao.update(lessonReview);
    }

    @Override
    @Transactional
    public void deleteReviewById(@Nonnull Integer id) {
        LessonReview lessonReview = lessonReviewDao.findById(id);
        if (lessonReview == null) {
            throw new BusinessException("Error at deleting lesson review by id:"
                    + " no such review");
        }
        lessonReviewDao.delete(lessonReview);
    }

    private void checkReviewForNulls(@Nonnull LessonReview review,
                                     @Nonnull EntityOperation operation) {
        if (review.getMessage() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson review: message is null");
        }
        if (review.getReviewDate() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson review: date is null");
        }
        if (review.getUser() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson review: user is null");
        }
        if (review.getLesson() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson review: lesson is null");
        }
    }

    private void checkReviewForForeignEntities(@Nullable Lesson lesson,
                                               @Nullable User user,
                                               @Nonnull EntityOperation operation) {
        if (user == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson review: no such user");
        }
        if (lesson == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson review: no such lesson");
        }
    }

    private void formEqualHandlersList(@Nonnull List<EqualQueryHandler<LessonReview, ?>> equalParameters,
                                       @Nullable Integer userId,
                                       @Nullable Integer lessonId) {
        if (userId != null) {
            equalParameters.add(new EqualQueryHandler<>(LessonReview_.user,
                    userDao.findById(userId)));
        }
        if (lessonId != null) {
            equalParameters.add(new EqualQueryHandler<>(LessonReview_.lesson,
                    lessonDao.findById(lessonId)));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<LessonReview, Y> extractSortField(
            @Nullable LessonReviewSortCriterion criterion) {
        if (LessonReviewSortCriterion.DATE.equals(criterion)) {
            return (SingularAttribute<LessonReview, Y>) LessonReview_.reviewDate;
        } else if (!LessonReviewSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding lesson reviews"
                    + " by criterion: invalid criterion");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> void formCompareHandlerList(
            @Nonnull List<CompareQueryHandler<LessonReview, Y>> compareParameters,
            @Nullable Date startDate,
            @Nullable Date endDate) {
        if (startDate != null) {
            compareParameters.add((CompareQueryHandler<LessonReview, Y>)
                    new CompareQueryHandler<>(LessonReview_.reviewDate, startDate,
                            QuerySortOperation.GREATER));
        }
        if (endDate != null) {
            compareParameters.add((CompareQueryHandler<LessonReview, Y>)
                    new CompareQueryHandler<>(LessonReview_.reviewDate, endDate,
                            QuerySortOperation.LESS));
        }
    }
}
