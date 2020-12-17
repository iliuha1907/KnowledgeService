package com.senla.training.knowledgeservice.testunit;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.TeacherSortCriterion;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.teacher.TeacherDao;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
import com.senla.training.knowledgeservice.service.service.teacher.TeacherService;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfigurator.class)
public class TeacherServiceImplTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Teacher> teachers;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;

    @BeforeAll
    public static void setUp() {
        Teacher teacherOne = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);
        Teacher teacherTwo = new Teacher(2, "Petr", "Petrov",
                BigDecimal.TEN, RewardType.FIXED, true);
        teachers = Arrays.asList(teacherOne, teacherTwo);
    }

    @Test
    void TeacherServiceImpl_addTeacher() {
        Teacher teacher = teachers.get(0);

        Assertions.assertDoesNotThrow(() -> teacherService.addTeacher(teacher));
    }

    @Test
    void TeacherServiceImpl_addTeacher_firstNameIsNull() {
        String message = "Error at adding teacher: first name is null";
        Teacher teacher = new Teacher(1, null, "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_addTeacher_lastNameIsNull() {
        String message = "Error at adding teacher: last name is null";
        Teacher teacher = new Teacher(1, "Name", null,
                BigDecimal.TEN, RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_addTeacher_rewardTypeIsNull() {
        String message = "Error at adding teacher: reward type is null";
        Teacher teacher = new Teacher(1, "Name", "Last",
                BigDecimal.TEN, null, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_addTeacher_isActiveIsNull() {
        String message = "Error at adding teacher: activity status is null";
        Teacher teacher = new Teacher(1, "Name", "Last",
                BigDecimal.TEN, RewardType.FIXED, null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_addTeacher_rewardIsNull() {
        String message = "Error at adding teacher: reward is null";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                null, RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_addTeacher_wrongReward() {
        String message = "Error at adding teacher: invalid reward";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.valueOf(-50), RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_addTeacher_daoExceptionAtAdd() {
        Teacher teacher = teachers.get(1);

        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).add(teacher);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> teacherService.addTeacher(teacher));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherServiceImpl_findTeacherById() {
        Teacher teacher = teachers.get(0);
        Integer id = teacher.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Assertions.assertEquals(teacher, teacherService.findTeacherById(id));
    }

    @Test
    void TeacherServiceImpl_findTeacherById_noTeacher() {
        String message = "Error at finding teacher by id: no such teacher";
        Integer id = 0;

        Mockito.doReturn(null).when(teacherDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.findTeacherById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_findTeacherById_daoExceptionAtFindTeacher() {
        Integer id = -1;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(teacherDao).findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> teacherService.findTeacherById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TeacherServiceImpl_findTeachers() {
        Mockito.doReturn(teachers).when(teacherDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> teacherService
                .findTeachers(TeacherSortCriterion.NATURAL, BigDecimal.ONE, BigDecimal.TEN,
                        RewardType.FIXED, null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TeacherServiceImpl_findTeachers_daoException() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> teacherService.findTeachers(TeacherSortCriterion.NATURAL, BigDecimal.ONE,
                        BigDecimal.TEN, RewardType.FIXED, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherServiceImpl_updateTeacher() {
        Teacher teacher = teachers.get(0);
        Integer id = teacher.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Assertions.assertDoesNotThrow(() -> teacherService.updateTeacher(teacher, id));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_firstNameIsNull() {
        String message = "Error at updating teacher: first name is null";
        Teacher teacher = new Teacher(1, null, "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherService.updateTeacher(teacher, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_lastNameIsNull() {
        String message = "Error at updating teacher: last name is null";
        Teacher teacher = new Teacher(1, "Name", null,
                BigDecimal.TEN, RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherService.updateTeacher(teacher, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_rewardTypeIsNull() {
        String message = "Error at updating teacher: reward type is null";
        Teacher teacher = new Teacher(1, "Name", "Last",
                BigDecimal.TEN, null, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherService.updateTeacher(teacher, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_isActiveIsNull() {
        String message = "Error at updating teacher: activity status is null";
        Teacher teacher = new Teacher(1, "Name", "Last",
                BigDecimal.TEN, RewardType.FIXED, null);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherService.updateTeacher(teacher, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_rewardIsNull() {
        String message = "Error at updating teacher: reward is null";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                null, null, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherService.updateTeacher(teacher, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_wrongReward() {
        String message = "Error at updating teacher: invalid reward";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.valueOf(-50), RewardType.FIXED, true);

        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherService.updateTeacher(teacher, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_noTeacher() {
        String message = "Error at updating teacher: no such teacher";
        Teacher teacher = teachers.get(0);

        Mockito.doReturn(null).when(teacherDao).findById(teacher.getId());
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherService.updateTeacher(teacher,
                        teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_daoExceptionAtFindTeacher() {
        Teacher teacher = teachers.get(0);

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).findById(teacher.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> teacherService.updateTeacher(teacher,
                        teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_daoExceptionAtDelete() {
        Teacher teacher = teachers.get(0);
        Teacher teacherNew = new Teacher(2, "Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, false);
        Integer id = teacherNew.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(teacherNew.getId());
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).deleteAllNotTookPlaceByTeacher(teacher);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> teacherService.updateTeacher(teacherNew, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherServiceImpl_updateTeacher_daoExceptionAtUpdate() {
        Teacher teacher = teachers.get(1);
        Integer id = teacher.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).update(teacher);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> teacherService.updateTeacher(teacher, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
