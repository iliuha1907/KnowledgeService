package com.senla.training.knowledgeservice.service.service.lesson;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.LessonSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.lesson.Lesson;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.model.lesson.Lesson_;
import com.senla.training.knowledgeservice.service.service.EntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private CourseDao courseDao;

    @Override
    @Transactional
    public void addLesson(@Nonnull Lesson lesson) {
        checkLessonForNulls(lesson, EntityOperation.ADDING);
        Course course = courseDao.findById(lesson.getCourse().getId());
        if (course == null) {
            throw new BusinessException("Error at adding lesson:"
                    + " no such course for lesson");
        }
        lesson.setCourse(course);
        lesson.setId(null);
        lessonDao.add(lesson);
    }

    @Override
    @Transactional
    @Nonnull
    public Lesson findLessonById(@Nonnull Integer id) {
        Lesson lesson = lessonDao.findById(id);
        if (lesson == null) {
            throw new BusinessException("Error at finding lesson by id:"
                    + " no such lesson");
        }
        return lesson;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<Lesson> findLessons(
            @Nonnull LessonSortCriterion criterion,
            @Nullable LessonType type,
            @Nullable String title,
            @Nullable Integer courseId) {
        List<EqualQueryHandler<Lesson, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<Lesson, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, type, title, courseId);
        return lessonDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateLesson(@Nonnull Lesson lesson, @Nonnull Integer id) {
        checkLessonForNulls(lesson, EntityOperation.UPDATING);
        if (lessonDao.findById(id) == null) {
            throw new BusinessException("Error at updating lesson: no such lesson");
        }
        Course course = courseDao.findById(lesson.getCourse().getId());
        if (course == null) {
            throw new BusinessException("Error at updating lesson:"
                    + " no such course for lesson");
        }
        lesson.setCourse(course);
        lesson.setId(id);
        lessonDao.update(lesson);
    }

    @Override
    @Transactional
    public void deleteLessonById(@Nonnull Integer id) {
        Lesson lesson = lessonDao.findById(id);
        if (lesson == null) {
            throw new BusinessException("Error at deleting lesson by id:"
                    + " no such lesson");
        }
        lessonDao.delete(lesson);
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<Lesson, Y> extractSortField(
            @Nullable LessonSortCriterion criterion) {
        if (LessonSortCriterion.TITLE.equals(criterion)) {
            return (SingularAttribute<Lesson, Y>) Lesson_.title;
        } else if (!LessonSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding lessons by criterion:"
                    + " invalid criterion");
        }
        return null;
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<Lesson, ?>> equalParameters,
            @Nullable LessonType type,
            @Nullable String title,
            @Nullable Integer courseId) {
        if (type != null) {
            equalParameters.add(new EqualQueryHandler<>(Lesson_.type, type));
        }
        if (title != null) {
            equalParameters.add(new EqualQueryHandler<>(Lesson_.title, title));
        }
        if (courseId != null) {
            equalParameters.add(new EqualQueryHandler<>(Lesson_.course,
                    courseDao.findById(courseId)));
        }
    }

    private void checkLessonForNulls(@Nonnull Lesson lesson,
                                     @Nonnull EntityOperation operation) {
        if (lesson.getTitle() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson: title is null");
        }
        if (lesson.getDescription() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson: description is null");
        }
        if (lesson.getType() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson: type is null");
        }
        if (lesson.getCourse() == null) {
            throw new BusinessException("Error at " + operation
                    + " lesson: course is null");
        }
    }
}
