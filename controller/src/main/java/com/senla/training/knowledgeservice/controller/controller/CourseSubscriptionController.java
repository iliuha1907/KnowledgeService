package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.CourseSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.subscription.CourseSubscriptionDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.subscription.CourseSubscriptionMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.service.service.subscription.course.CourseSubscriptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.COURSE_SUBSCRIPTION_CONTROLLER_TAG)
@RequestMapping("/api/courseSubscriptions")
public class CourseSubscriptionController {

    @Autowired
    private CourseSubscriptionService courseSubscriptionService;
    @Autowired
    private CourseSubscriptionMapper courseSubscriptionMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds course subscription and responses with message,"
            + " admin only", response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addSubscription(
            @ApiParam(value = "Defines data to insert")
            @RequestBody CourseSubscriptionDto courseSubscriptionDto) {
        courseSubscriptionService.addSubscription(courseSubscriptionMapper.toEntity(
                courseSubscriptionDto));
        return messageDtoMapper.toDto("Successfully added course subscription");
    }

    @ApiOperation(value = "Displays list of course subscriptions, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<CourseSubscriptionDto> findSubscriptions(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    CourseSubscriptionSortCriterion criterion,
            @ApiParam(value = "Defines begin date for start date of subscription"
                    + " to find above or equal")
            @RequestParam(name = "beginDateForStart", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date beginDateForStart,
            @ApiParam(value = "Defines end date for start date of subscription to"
                    + " find below or equal")
            @RequestParam(name = "endDateForStart", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date endDateForStart,
            @ApiParam(value = "Defines begin date for end date of subscription to"
                    + " find above or equal")
            @RequestParam(name = "beginDateForEnd", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date beginDateForEnd,
            @ApiParam(value = "Defines end date for end date of subscription to"
                    + " find below or equal")
            @RequestParam(name = "endDateForEnd", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date endDateForEnd,
            @ApiParam(value = "Defines user id to find by. If not admin then id"
                    + " of authenticated is taken")
            @RequestParam(name = "userId", required = false)
                    Integer userId,
            @ApiParam(value = "Defines course id to find by")
            @RequestParam(name = "courseId", required = false)
                    Integer courseId) {
        return courseSubscriptionMapper.listToDto(
                courseSubscriptionService.findSubscriptions(criterion, beginDateForStart,
                        endDateForStart, beginDateForEnd, endDateForEnd, userId, courseId));
    }

    @ApiOperation(value = "Displays course subscription with given id, all authenticated",
            response = CourseSubscriptionDto.class)
    @GetMapping("/{id}")
    public CourseSubscriptionDto findCourseSubscriptionById(
            @ApiParam(value = "Defines id by which to search")
            @PathVariable("id") Integer id) {
        return courseSubscriptionMapper.toDto(
                courseSubscriptionService.findSubscriptionById(id));
    }

    @ApiOperation(value = "Deletes course subscription with given id and"
            + " responses with message, admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteCourseSubscriptionById(
            @ApiParam(value = "Defines id with which to delete")
            @PathVariable("id") Integer id) {
        courseSubscriptionService.deleteSubscriptionById(id);
        return messageDtoMapper.toDto("Successfully deleted course subscription");
    }
}
