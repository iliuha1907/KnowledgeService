package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.CourseSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.course.CourseDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.course.CourseMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.service.service.course.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.COURSE_CONTROLLER_TAG)
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds course and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addCourse(@ApiParam(value = "Defines data to insert")
                                @RequestBody CourseDto courseDto) {
        courseService.addCourse(courseMapper.toEntity(courseDto));
        return messageDtoMapper.toDto("Successfully added course");
    }

    @ApiOperation(value = "Displays list of courses, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<CourseDto> findCourses(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    CourseSortCriterion criterion,
            @ApiParam(value = "Defines title to find by")
            @RequestParam(name = "title", required = false)
                    String title,
            @ApiParam(value = "Defines start price to find above or equal to")
            @RequestParam(name = "startPrice", required = false)
                    BigDecimal startPrice,
            @ApiParam(value = "Defines end price to find below or equal to")
            @RequestParam(name = "endPrice", required = false)
                    BigDecimal endPrice,
            @ApiParam(value = "Defines start duration to find above or equal to")
            @RequestParam(name = "startDuration", required = false)
                    Integer startDuration,
            @ApiParam(value = "Defines end duration to find below or equal to")
            @RequestParam(name = "endDuration", required = false)
                    Integer endDuration,
            @ApiParam(value = "Defines section id to find with")
            @RequestParam(name = "sectionId", required = false)
                    Integer sectionId) {
        return courseMapper.listToDto(courseService.findCourses(criterion, title,
                startPrice, endPrice, startDuration, endDuration, sectionId));
    }

    @ApiOperation(value = "Displays course with given id, all authenticated",
            response = CourseDto.class)
    @GetMapping("/{id}")
    public CourseDto findCourseById(@ApiParam(value = "Defines id by which to search")
                                    @PathVariable("id") Integer id) {
        return courseMapper.toDto(courseService.findCourseById(id));
    }

    @ApiOperation(value = "Updates course with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateCourse(@ApiParam(value = "Defines data to update")
                                   @RequestBody CourseDto courseDto,
                                   @ApiParam(value = "Defines id by which to update")
                                   @PathVariable("id") Integer id) {
        courseService.updateCourse(courseMapper.toEntity(courseDto), id);
        return messageDtoMapper.toDto("Successfully updated course");
    }

    @ApiOperation(value = "Deletes course with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteCourseById(@ApiParam(value = "Defines id with which to delete")
                                       @PathVariable("id") Integer id) {
        courseService.deleteCourseById(id);
        return messageDtoMapper.toDto("Successfully deleted course");
    }
}
