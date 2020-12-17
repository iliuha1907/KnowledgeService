package com.senla.training.knowledgeservice.controller.integrationtest;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.sort.TeacherSortCriterion;
import com.senla.training.knowledgeservice.controller.controller.TeacherController;
import com.senla.training.knowledgeservice.controller.integrationtest.config.ControllerTestConfigurator;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.teacher.TeacherDao;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.teacher.TeacherDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.teacher.TeacherMapper;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.model.teacher.Teacher;
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
@ContextConfiguration(classes = ControllerTestConfigurator.class)
public class TeacherControllerTest {

    private static final String DAO_EXCEPTION_MESSAGE = "Error at manipulating with entity";
    private static List<Teacher> teachers;
    private static List<TeacherDto> teacherDtos;
    private final TeacherDto teacherDtoForAll = new TeacherDto();
    private final MessageDto messageDtoForAll = new MessageDto();
    @Autowired
    private TeacherController teacherController;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private MessageDtoMapper messageDtoMapper;
    @Autowired
    private LessonSubscriptionDao lessonSubscriptionDao;

    @BeforeAll
    public static void setUp() {
        Teacher teacherOne = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);
        Teacher teacherTwo = new Teacher(2, "Petr", "Petrov",
                BigDecimal.TEN, RewardType.FIXED, true);
        teachers = Arrays.asList(teacherOne, teacherTwo);
        teacherDtos = Arrays.asList(new TeacherDto(), new TeacherDto());
    }

    @Test
    void TeacherServiceImpl_addTeacher() {
        Mockito.doReturn(teachers.get(0)).when(teacherMapper).toEntity(teacherDtoForAll);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully added teacher");
        Assertions.assertEquals(messageDtoForAll, teacherController.addTeacher(teacherDtoForAll));
    }

    @Test
    void TeacherController_addTeacher_firstNameIsNull() {
        String message = "Error at adding teacher: first name is null";
        Teacher teacher = new Teacher(1, null, "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_addTeacher_lastNameIsNull() {
        String message = "Error at adding teacher: last name is null";
        Teacher teacher = new Teacher(1, "Name", null,
                BigDecimal.TEN, RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_addTeacher_rewardTypeIsNull() {
        String message = "Error at adding teacher: reward type is null";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.TEN, null, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_addTeacher_isActiveIsNull() {
        String message = "Error at adding teacher: activity status is null";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, null);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_addTeacher_rewardIsNull() {
        String message = "Error at adding teacher: reward is null";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                null, RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_addTeacher_wrongReward() {
        String message = "Error at adding teacher: invalid reward";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.valueOf(-50), RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_addTeacher_daoExceptionAtAdd() {
        Teacher teacher = teachers.get(0);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(teacherDao).add(
                teacher);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> teacherController.addTeacher(teacherDtoForAll));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherController_findTeacherById() {
        Teacher teacher = teachers.get(0);
        Integer id = teacher.getId();

        Mockito.doReturn(teacherDtoForAll).when(teacherMapper).toDto(teacher);
        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Assertions.assertEquals(teacherDtoForAll, teacherController.findTeacherById(id));
    }

    @Test
    void TeacherController_findTeacherById_noTeacher() {
        String message = "Error at finding teacher by id: no such teacher";
        Integer id = 0;

        Mockito.doReturn(null).when(teacherDao).findById(id);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class, () -> teacherController.findTeacherById(id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_findTeacherById_daoExceptionAtFind() {
        Integer id = 0;

        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE)).when(teacherDao)
                .findById(id);
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class, () -> teacherController.findTeacherById(id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TeacherController_findTeachers() {
        Mockito.doReturn(teacherDtos).when(teacherMapper).listToDto(teachers);
        Mockito.doReturn(teachers).when(teacherDao).findSortedEntities(Matchers.eq(null),
                Matchers.anyList(), Matchers.anyList());
        Assertions.assertDoesNotThrow(() -> teacherController
                .findTeachers(TeacherSortCriterion.NATURAL, BigDecimal.ONE,
                        BigDecimal.TEN, RewardType.FIXED, null));
    }

    @Test
    @SuppressWarnings("unchecked")
    void TeacherController_findTeachers_daoExceptionAtFind() {
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).findSortedEntities(Matchers.eq(null), Matchers.anyList(),
                Matchers.anyList());

        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> teacherController.findTeachers(TeacherSortCriterion.NATURAL, BigDecimal.ONE,
                        BigDecimal.TEN, RewardType.FIXED, null));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherController_updateTeacher() {
        Teacher teacher = teachers.get(0);
        Integer id = teacher.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        Mockito.doReturn(messageDtoForAll)
                .when(messageDtoMapper).toDto("Successfully updated teacher");
        Assertions.assertEquals(messageDtoForAll,
                teacherController.updateTeacher(teacherDtoForAll, id));
    }

    @Test
    void TeacherController_updateTeacher_firstNameIsNull() {
        String message = "Error at updating teacher: first name is null";
        Teacher teacher = new Teacher(1, null, "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () ->  teacherController.updateTeacher(teacherDtoForAll, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_lastNameIsNull() {
        String message = "Error at updating teacher: last name is null";
        Teacher teacher = new Teacher(1, "Name", null,
                BigDecimal.TEN, RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () ->  teacherController.updateTeacher(teacherDtoForAll, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_rewardTypeIsNull() {
        String message = "Error at updating teacher: reward type is null";
        Teacher teacher = new Teacher(1, "Name", "Last",
                BigDecimal.TEN, null, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () ->  teacherController.updateTeacher(teacherDtoForAll, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_isActiveIsNull() {
        String message = "Error at updating teacher: activity status is null";
        Teacher teacher = new Teacher(1, "Name", "Last",
                BigDecimal.TEN, RewardType.FIXED, null);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () ->  teacherController.updateTeacher(teacherDtoForAll, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_rewardIsNull() {
        String message = "Error at updating teacher: reward is null";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                null, RewardType.FIXED, true);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () ->  teacherController.updateTeacher(teacherDtoForAll, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_wrongReward() {
        String message = "Error at updating teacher: invalid reward";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.valueOf(-50), RewardType.FIXED, true);
        Integer id = teacher.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherController.updateTeacher(teacherDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_noTeacher() {
        String message = "Error at updating teacher: no such teacher";
        Teacher teacher = new Teacher(1, "Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, true);
        Integer id = teacher.getId();

        Mockito.doReturn(null).when(teacherDao).findById(id);
        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        BusinessException thrown = Assertions.assertThrows(
                BusinessException.class,
                () -> teacherController.updateTeacher(teacherDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(message));
    }

    @Test
    void TeacherController_updateTeacher_daoExceptionAtFindTeacher() {
        Teacher teacher = teachers.get(0);

        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        Mockito.doThrow(new IllegalStateException(DAO_EXCEPTION_MESSAGE))
                .when(teacherDao).findById(teacher.getId());
        IllegalStateException thrown = Assertions.assertThrows(
                IllegalStateException.class,
                () -> teacherController.updateTeacher(teacherDtoForAll, teacher.getId()));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherController_updateTeacher_daoExceptionAtDelete() {
        Teacher teacher = teachers.get(0);
        Teacher teacherNew = new Teacher(2,"Ivan", "Ivanov",
                BigDecimal.TEN, RewardType.FIXED, false);
        Integer id = teacherNew.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Mockito.doReturn(teacherNew).when(teacherMapper).toEntity(teacherDtoForAll);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE))
                .when(lessonSubscriptionDao).deleteAllNotTookPlaceByTeacher(teacher);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class, () -> teacherController
                        .updateTeacher(teacherDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }

    @Test
    void TeacherController_updateTeacher_daoExceptionAtUpdate() {
        Teacher teacher = teachers.get(1);
        Integer id = teacher.getId();

        Mockito.doReturn(teacher).when(teacherDao).findById(id);
        Mockito.doReturn(teacher).when(teacherMapper).toEntity(teacherDtoForAll);
        Mockito.doThrow(new PersistenceException(DAO_EXCEPTION_MESSAGE)).when(teacherDao)
                .update(teacher);
        PersistenceException thrown = Assertions.assertThrows(
                PersistenceException.class,
                () -> teacherController.updateTeacher(teacherDtoForAll, id));
        Assertions.assertTrue(thrown.getMessage().contains(DAO_EXCEPTION_MESSAGE));
    }
}
