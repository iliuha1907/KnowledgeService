package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.LessonSubscriptionSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.subscription.LessonSubscriptionDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.subscription.LessonSubscriptionMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.service.service.subscription.lesson.LessonSubscriptionService;
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
@Api(tags = SpringFoxConfig.LESSON_SUBSCRIPTION_CONTROLLER_TAG)
@RequestMapping("/api/lessonSubscriptions")
public class LessonSubscriptionController {

    @Autowired
    private LessonSubscriptionService lessonSubscriptionService;
    @Autowired
    private LessonSubscriptionMapper lessonSubscriptionMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds lesson subscription and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addSubscription(
            @ApiParam(value = "Defines data to insert")
            @RequestBody LessonSubscriptionDto lessonSubscriptionDto) {
        lessonSubscriptionService.addSubscription(lessonSubscriptionMapper.toEntity(
                lessonSubscriptionDto));
        return messageDtoMapper.toDto("Successfully added lesson subscription");
    }

    @ApiOperation(value = "Displays list of lesson subscriptions, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<LessonSubscriptionDto> findSubscriptions(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    LessonSubscriptionSortCriterion criterion,
            @ApiParam(value = "Defines start date to find above or equal")
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date startDate,
            @ApiParam(value = "Defines end date to find below or equal")
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date endDate,
            @ApiParam(value = "Defines user id to find by. If not admin, then"
                    + " automatically will be taken id of authenticated")
            @RequestParam(name = "userId", required = false)
                    Integer userId,
            @ApiParam(value = "Defines lesson id to find by")
            @RequestParam(name = "lessonId", required = false)
                    Integer lessonId,
            @ApiParam(value = "Defines teacher id to find by")
            @RequestParam(name = "teacherId", required = false)
                    Integer teacherId,
            @ApiParam(value = "Defines took place status to find by")
            @RequestParam(name = "tookPlace", required = false)
                    Boolean tookPlace) {
        return lessonSubscriptionMapper.listToDto(
                lessonSubscriptionService.findSubscriptions(criterion, startDate,
                        endDate, userId, teacherId, lessonId, tookPlace));
    }

    @ApiOperation(value = "Displays lesson subscription with given id, all authenticated",
            response = LessonSubscriptionDto.class)
    @GetMapping("/{id}")
    public LessonSubscriptionDto findLessonSubscriptionById(
            @ApiParam(value = "Defines id by which to search")
            @PathVariable("id") Integer id) {
        return lessonSubscriptionMapper.toDto(lessonSubscriptionService
                .findSubscriptionById(id));
    }

    @ApiOperation(value = "Updates lesson subscription with given id and responses"
            + " with message, admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateLessonSubscription(
            @ApiParam(value = "Defines data to update")
            @RequestBody LessonSubscriptionDto lessonSubscriptionDto,
            @ApiParam(value = "Defines id by which to update")
            @PathVariable("id") Integer id) {
        lessonSubscriptionService.updateSubscription(lessonSubscriptionMapper.toEntity(
                lessonSubscriptionDto), id);
        return messageDtoMapper.toDto("Successfully updated lesson subscription");
    }

    @ApiOperation(value = "Deletes lesson subscription with given id and responses"
            + " with message, admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteLessonSubscriptionById(
            @ApiParam(value = "Defines id with which to delete")
            @PathVariable("id") Integer id) {
        lessonSubscriptionService.deleteSubscriptionById(id);
        return messageDtoMapper.toDto("Successfully deleted lesson subscription");
    }
}
