package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.LessonReviewSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.review.LessonReviewDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.review.LessonReviewMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.service.service.review.lesson.LessonReviewService;
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
@Api(tags = SpringFoxConfig.LESSON_REVIEW_CONTROLLER_TAG)
@RequestMapping("/api/lessonReviews")
public class LessonReviewController {

    @Autowired
    private LessonReviewService lessonReviewService;
    @Autowired
    private LessonReviewMapper lessonReviewMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds lesson review and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addReview(@ApiParam(value = "Defines data to insert")
                                @RequestBody LessonReviewDto lessonReviewDto) {
        lessonReviewService.addReview(lessonReviewMapper.toEntity(lessonReviewDto));
        return messageDtoMapper.toDto("Successfully added lesson review");
    }

    @ApiOperation(value = "Displays list of lesson reviews, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<LessonReviewDto> findReviews(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    LessonReviewSortCriterion criterion,
            @ApiParam(value = "Defines user id to find with")
            @RequestParam(name = "userId", required = false)
                    Integer userId,
            @ApiParam(value = "Defines lesson id to find with")
            @RequestParam(name = "lessonId", required = false)
                    Integer lessonId,
            @ApiParam(value = "Defines start date to find above")
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date startDate,
            @ApiParam(value = "Defines end date to find below")
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date endDate) {
        return lessonReviewMapper.listToDto(lessonReviewService.findReviews(criterion,
                userId, lessonId, startDate, endDate));
    }


    @ApiOperation(value = "Displays lesson review with given id, all authenticated",
            response = LessonReviewDto.class)
    @GetMapping("/{id}")
    public LessonReviewDto findLessonReviewById(
            @ApiParam(value = "Defines id by which to search")
            @PathVariable("id") Integer id) {
        return lessonReviewMapper.toDto(lessonReviewService.findReviewById(id));
    }

    @ApiOperation(value = "Updates lesson review with given id and responses with message, admin only",
            response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateLessonReview(@ApiParam(value = "Defines data to update")
                                         @RequestBody LessonReviewDto lessonReviewDto,
                                         @ApiParam(value = "Defines id by which to update")
                                         @PathVariable("id") Integer id) {
        lessonReviewService.updateReview(lessonReviewMapper.toEntity(lessonReviewDto), id);
        return messageDtoMapper.toDto("Successfully updated lesson review");
    }

    @ApiOperation(value = "Deletes lesson review with given id and responses with message, admin only",
            response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteLessonReviewById(
            @ApiParam(value = "Defines id with which to delete")
            @PathVariable("id") Integer id) {
        lessonReviewService.deleteReviewById(id);
        return messageDtoMapper.toDto("Successfully deleted lesson review");
    }
}
