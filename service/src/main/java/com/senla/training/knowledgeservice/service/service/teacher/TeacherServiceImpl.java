package com.senla.training.knowledgeservice.service.service.teacher;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.TeacherSortCriterion;
import com.senla.training.knowledgeservice.dao.query.CompareQueryHandler;
import com.senla.training.knowledgeservice.dao.query.EqualQueryHandler;
import com.senla.training.knowledgeservice.dao.query.QuerySortOperation;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.teacher.TeacherDao;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import com.senla.training.knowledgeservice.model.teacher.Teacher_;
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
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;

    @Override
    @Transactional
    public void addTeacher(@Nonnull Teacher teacher) {
        checkTeacherForNulls(teacher, EntityOperation.ADDING);
        if (teacher.getReward().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Error at adding teacher: invalid reward");
        }
        teacher.setId(null);
        teacherDao.add(teacher);
    }

    @Override
    @Transactional
    @Nonnull
    public Teacher findTeacherById(@Nonnull Integer id) {
        Teacher teacher = teacherDao.findById(id);
        if (teacher == null) {
            throw new BusinessException("Error at finding teacher by id:"
                    + " no such teacher");
        }
        return teacher;
    }

    @Override
    @Transactional
    @Nonnull
    public <Y extends Comparable<? super Y>> List<Teacher> findTeachers(
            @Nonnull TeacherSortCriterion criterion,
            @Nullable BigDecimal startReward,
            @Nullable BigDecimal endReward,
            @Nullable RewardType type,
            @Nullable Boolean active) {
        List<EqualQueryHandler<Teacher, ?>> equalParameters = new ArrayList<>();
        List<CompareQueryHandler<Teacher, Y>> compareParameters = new ArrayList<>();
        formEqualHandlersList(equalParameters, type, active);
        formCompareHandlerList(compareParameters, startReward, endReward);
        return teacherDao.findSortedEntities(extractSortField(criterion),
                equalParameters, compareParameters);
    }

    @Override
    @Transactional
    public void updateTeacher(@Nonnull Teacher teacher, @Nonnull Integer id) {
        checkTeacherForNulls(teacher, EntityOperation.UPDATING);
        if (teacher.getReward().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Error at updating teacher: invalid reward");
        }
        Teacher existing = teacherDao.findById(id);
        if (existing == null) {
            throw new BusinessException("Error at updating teacher: no such teacher");
        }
        Boolean isActive = teacher.getActive();
        if (isActive == null) {
            throw new BusinessException("Error at updating teacher:"
                    + " wrong activity status");
        }
        if (existing.getActive() && !teacher.getActive()) {
            lessonSubscriptionDao.deleteAllNotTookPlaceByTeacher(existing);
        }
        teacher.setId(id);
        teacherDao.update(teacher);
    }

    private void checkTeacherForNulls(@Nonnull Teacher teacher,
                                      @Nonnull EntityOperation operation) {
        if (teacher.getFirstName() == null) {
            throw new BusinessException("Error at " + operation
                    + " teacher: first name is null");
        }
        if (teacher.getLastName() == null) {
            throw new BusinessException("Error at " + operation
                    + " teacher: last name is null");
        }
        if (teacher.getReward() == null) {
            throw new BusinessException("Error at " + operation
                    + " teacher: reward is null");
        }
        if (teacher.getRewardType() == null) {
            throw new BusinessException("Error at " + operation
                    + " teacher: reward type is null");
        }
        if (teacher.getActive() == null) {
            throw new BusinessException("Error at " + operation
                    + " teacher: activity status is null");
        }
    }

    private void formEqualHandlersList(
            @Nonnull List<EqualQueryHandler<Teacher, ?>> equalParameters,
            @Nullable RewardType type,
            @Nullable Boolean active) {
        if (type != null) {
            equalParameters.add(new EqualQueryHandler<>(Teacher_.rewardType, type));
        }
        if (active != null) {
            equalParameters.add(new EqualQueryHandler<>(Teacher_.isActive, active));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> void
    formCompareHandlerList(@Nonnull List<CompareQueryHandler<Teacher, Y>> parameters,
                           @Nullable BigDecimal startReward,
                           @Nullable BigDecimal endReward) {
        if (startReward != null) {
            parameters.add((CompareQueryHandler<Teacher, Y>)
                    new CompareQueryHandler<>(Teacher_.reward, startReward,
                            QuerySortOperation.GREATER));
        }
        if (endReward != null) {
            parameters.add((CompareQueryHandler<Teacher, Y>)
                    new CompareQueryHandler<>(Teacher_.reward, endReward,
                            QuerySortOperation.LESS));
        }
    }

    @SuppressWarnings("unchecked")
    private <Y extends Comparable<? super Y>> SingularAttribute<Teacher, Y> extractSortField(
            @Nullable TeacherSortCriterion criterion) {
        if (TeacherSortCriterion.FIRST_NAME.equals(criterion)) {
            return (SingularAttribute<Teacher, Y>) Teacher_.firstName;
        } else if (TeacherSortCriterion.LAST_NAME.equals(criterion)) {
            return (SingularAttribute<Teacher, Y>) Teacher_.lastName;
        } else if (!TeacherSortCriterion.NATURAL.equals(criterion)) {
            throw new BusinessException("Error at finding teachers by criterion:"
                    + " invalid criterion");
        }
        return null;
    }
}
