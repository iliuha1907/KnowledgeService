package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.TeacherSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.teacher.TeacherDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.teacher.TeacherMapper;
import com.senla.training.knowledgeservice.model.teacher.RewardType;
import com.senla.training.knowledgeservice.service.service.teacher.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.TEACHER_CONTROLLER_TAG)
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds teacher and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addTeacher(@ApiParam(value = "Defines data to insert")
                                 @RequestBody TeacherDto courseDto) {
        teacherService.addTeacher(teacherMapper.toEntity(courseDto));
        return messageDtoMapper.toDto("Successfully added teacher");
    }

    @ApiOperation(value = "Displays list of topics, admin only", response = Iterable.class)
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TeacherDto> findTeachers(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    TeacherSortCriterion criterion,
            @ApiParam(value = "Defines start reward to find above or equal")
            @RequestParam(name = "startReward", required = false)
                    BigDecimal startReward,
            @ApiParam(value = "Defines end reward to find below or equal")
            @RequestParam(name = "endReward", required = false)
                    BigDecimal endReward,
            @ApiParam(value = "Defines type of reward to find by")
            @RequestParam(name = "type", required = false)
                    RewardType type,
            @ApiParam(value = "Defines activity status to find by")
            @RequestParam(name = "active", required = false)
                    Boolean active) {
        return teacherMapper.listToDto(teacherService.findTeachers(criterion, startReward,
                endReward, type, active));
    }

    @ApiOperation(value = "Displays teacher with given id, admin only",
            response = TeacherDto.class)
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TeacherDto findTeacherById(@ApiParam(value = "Defines id by which to search")
                                      @PathVariable("id") Integer id) {
        return teacherMapper.toDto(teacherService.findTeacherById(id));
    }

    @ApiOperation(value = "Updates teacher with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateTeacher(@ApiParam(value = "Defines data to update")
                                    @RequestBody TeacherDto teacherDto,
                                    @ApiParam(value = "Defines id with which to update")
                                    @PathVariable("id") Integer id) {
        teacherService.updateTeacher(teacherMapper.toEntity(teacherDto), id);
        return messageDtoMapper.toDto("Successfully updated teacher");
    }
}
