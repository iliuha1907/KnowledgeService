package com.senla.training.knowledgeservice.service.service.course;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.CourseSortCriterion;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.model.course.Course;
import com.senla.training.knowledgeservice.model.course.Course_;
import com.senla.training.knowledgeservice.model.section.Section;
import com.senla.training.knowledgeservice.service.service.EntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private SectionDao sectionDao;

    @Override
    @Transactional
    public void addCourse(@Nonnull Course course) {
        checkCourseForNulls(course, EntityOperation.ADDING);
        Section section = sectionDao.findById(course.getSection().getId());
        if (section == null) {
            throw new BusinessException("Error at adding course:"
                    + " no such section for course");
        }
        if (BigDecimal.ZERO.compareTo(course.getPrice()) >= 0) {
            throw new BusinessException("Error at adding course: invalid price");
        }
        if (course.getDuration().compareTo(0) <= 0) {
            throw new BusinessException("Error at adding course: invalid duration");
        }
        course.setSection(section);
        course.setId(null);
        courseDao.add(course);
    }

    @Override
    @Transactional
    @Nonnull
    public Course findCourseById(@Nonnull Integer id) {
        Course course = courseDao.findById(id);
        if (course == null) {
            throw new BusinessException("Error at finding course by id:"
                    + " no such course");
        }
        return course;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<Course> findCourses(
            @Nonnull CourseSortCriterion criterion,
            @Nullable String title,
            @Nullable BigDecimal startPrice,
            @Nullable BigDecimal endPrice,
            @Nullable Integer startDuration,
            @Nullable Integer endDuration,
            @Nullable Integer sectionId) {
        List<EqualQueryHandler<Course, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<Course, Y>> compareParameters = new ArrayList<>();
        formEqualHandlerList(equalParameters, title, sectionId);
        formCompareHandlerList(compareParameters, startPrice, endPrice,
                startDuration, endDuration);
        return courseDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateCourse(@Nonnull Course course, @Nonnull Integer id) {
        checkCourseForNulls(course, EntityOperation.UPDATING);
        if (courseDao.findById(id) == null) {
            throw new BusinessException("Error at updating course: no such course");
        }
        Section section = sectionDao.findById(course.getSection().getId());
        if (section == null) {
            throw new BusinessException("Error at updating course:"
                    + " no such section for course");
        }
        if (BigDecimal.ZERO.compareTo(course.getPrice()) >= 0) {
            throw new BusinessException("Error at updating course: invalid price");
        }
        if (course.getDuration().compareTo(0) <= 0) {
            throw new BusinessException("Error at updating course: invalid duration");
        }
        course.setSection(section);
        course.setId(id);
        courseDao.update(course);
    }

    @Override
    @Transactional
    public void deleteCourseById(@Nonnull Integer id) {
        Course course = courseDao.findById(id);
        if (course == null) {
            throw new BusinessException("Error at deleting course by id:"
                    + " no such course");
        }
        courseDao.delete(course);
    }

    private void formEqualHandlerList(
            @Nonnull List<EqualQueryHandler<Course, ?>> equalParameters,
            @Nullable String title, @Nullable Integer sectionId) {
        if (title != null) {
            equalParameters.add(new EqualQueryHandler<>(Course_.title, title));
        }
        if (sectionId != null) {
            equalParameters.add(new EqualQueryHandler<>(Course_.section,
                    sectionDao.findById(sectionId)));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<Course, Y> extractSortField(
            @Nullable CourseSortCriterion criterion) {
        if (CourseSortCriterion.TITLE.equals(criterion)) {
            return (SingularAttribute<Course, Y>) Course_.title;
        } else if (CourseSortCriterion.DURATION.equals(criterion)) {
            return (SingularAttribute<Course, Y>) Course_.duration;
        } else if (CourseSortCriterion.PRICE.equals(criterion)) {
            return (SingularAttribute<Course, Y>) Course_.price;
        } else if (!CourseSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding courses by criterion:"
                    + " invalid criterion");
        }
        return null;
    }

    private <Y extends Comparable<? super Y>> void formCompareHandlerList(
            @Nonnull List<CompareQueryHandler<Course, Y>> compareParameters,
            @Nullable BigDecimal startPrice, @Nullable BigDecimal endPrice,
            @Nullable Integer startDuration, @Nullable Integer endDuration) {
        addPricesToCompareParameters(compareParameters, startPrice, endPrice);
        addDurationsToCompareParameters(compareParameters, startDuration,
                endDuration);
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> void addPricesToCompareParameters(
            @Nonnull List<CompareQueryHandler<Course, Y>> parameters,
            @Nullable BigDecimal startPrice, @Nullable BigDecimal endPrice) {
        if (startPrice != null) {
            parameters.add((CompareQueryHandler<Course, Y>) new CompareQueryHandler<>(
                    Course_.price, startPrice, QuerySortOperation.GREATER));
        }
        if (endPrice != null) {
            parameters.add((CompareQueryHandler<Course, Y>) new CompareQueryHandler<>(
                    Course_.price, endPrice, QuerySortOperation.LESS));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> void addDurationsToCompareParameters(
            @Nonnull List<CompareQueryHandler<Course, Y>> parameters,
            @Nullable Integer startDuration, @Nullable Integer endDuration) {
        if (startDuration != null) {
            parameters.add((CompareQueryHandler<Course, Y>) new CompareQueryHandler<>(
                    Course_.duration, startDuration, QuerySortOperation.GREATER));
        }
        if (endDuration != null) {
            parameters.add((CompareQueryHandler<Course, Y>) new CompareQueryHandler<>(
                    Course_.duration, endDuration, QuerySortOperation.LESS));
        }
    }

    private void checkCourseForNulls(@Nonnull Course course, @Nonnull EntityOperation operation) {
        if (course.getTitle() == null) {
            throw new BusinessException("Error at " + operation + " course:"
                    + " title is null");
        }
        if (course.getDescription() == null) {
            throw new BusinessException("Error at " + operation + " course:"
                    + " description is null");
        }
        if (course.getDuration() == null) {
            throw new BusinessException("Error at " + operation + " course:"
                    + " duration is null");
        }
        if (course.getPrice() == null) {
            throw new BusinessException("Error at " + operation + " course:"
                    + " price is null");
        }
        if (course.getSection() == null) {
            throw new BusinessException("Error at " + operation + " course:"
                    + " section is null");
        }
    }
}
