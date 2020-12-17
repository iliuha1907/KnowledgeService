package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.LessonSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.lesson.LessonDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.lesson.LessonMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.model.lesson.LessonType;
import com.senla.training.knowledgeservice.service.service.lesson.LessonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.LESSON_CONTROLLER_TAG)
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds lesson and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addLesson(@ApiParam(value = "Defines data to insert")
                                @RequestBody LessonDto lessonDto) {
        lessonService.addLesson(lessonMapper.toEntity(lessonDto));
        return messageDtoMapper.toDto("Successfully added lesson");
    }

    @ApiOperation(value = "Displays list of lessons, all authenticated",
            response = Iterable.class)
    @GetMapping()
    public List<LessonDto> findLessons(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    LessonSortCriterion criterion,
            @ApiParam(value = "Defines lesson type to find by")
            @RequestParam(name = "type", required = false)
                    LessonType type,
            @ApiParam(value = "Defines title to find by")
            @RequestParam(name = "title", required = false)
                    String title,
            @ApiParam(value = "Defines course id to find by")
            @RequestParam(name = "courseId", required = false)
                    Integer courseId) {
        return lessonMapper.listToDto(lessonService.findLessons(criterion, type,
                title, courseId));
    }

    @ApiOperation(value = "Displays lessons with given id, all authenticated",
            response = LessonDto.class)
    @GetMapping("/{id}")
    public LessonDto findLessonById(@ApiParam(value = "Defines id by which to search")
                                    @PathVariable("id") Integer id) {
        return lessonMapper.toDto(lessonService.findLessonById(id));
    }

    @ApiOperation(value = "Updates lesson with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateLesson(@ApiParam(value = "Defines data to update")
                                   @RequestBody LessonDto lessonDto,
                                   @ApiParam(value = "Defines id by which to update")
                                   @PathVariable("id") Integer id) {
        lessonService.updateLesson(lessonMapper.toEntity(lessonDto), id);
        return messageDtoMapper.toDto("Successfully updated lesson");
    }

    @ApiOperation(value = "Deletes lesson with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteLessonById(@ApiParam(value = "Defines id with which to delete")
                                       @PathVariable("id") Integer id) {
        lessonService.deleteLessonById(id);
        return messageDtoMapper.toDto("Successfully deleted lesson");
    }
}
