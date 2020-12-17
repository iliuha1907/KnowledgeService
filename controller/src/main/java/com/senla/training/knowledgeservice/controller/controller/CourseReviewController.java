package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.CourseReviewSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.review.CourseReviewDto;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.mapper.review.CourseReviewMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.service.service.review.course.CourseReviewService;
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
@Api(tags = SpringFoxConfig.COURSE_REVIEW_CONTROLLER_TAG)
@RequestMapping("/api/courseReviews")
public class CourseReviewController {

    @Autowired
    private CourseReviewService courseReviewService;
    @Autowired
    private CourseReviewMapper courseReviewMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds course review and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addReview(@ApiParam(value = "Defines data to insert")
                                @RequestBody CourseReviewDto courseReviewDto) {
        courseReviewService.addReview(courseReviewMapper.toEntity(courseReviewDto));
        return messageDtoMapper.toDto("Successfully added course review");
    }

    @ApiOperation(value = "Displays list of course reviews, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<CourseReviewDto> findReviews(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    CourseReviewSortCriterion criterion,
            @ApiParam(value = "Defines user id to find by")
            @RequestParam(name = "userId", required = false)
                    Integer userId,
            @ApiParam(value = "Defines course id to find by")
            @RequestParam(name = "courseId", required = false)
                    Integer courseId,
            @ApiParam(value = "Defines start date to find above or equal")
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date startDate,
            @ApiParam(value = "Defines end date to find below or equal")
            @RequestParam(name = "endDate", required = false)
            @DateTimeFormat(pattern = "yyyy-M-dd")
                    Date endDate) {
        return courseReviewMapper.listToDto(courseReviewService.findReviews(criterion,
                userId, courseId, startDate, endDate));
    }

    @ApiOperation(value = "Displays course review with given id, all authenticated",
            response = CourseReviewDto.class)
    @GetMapping("/{id}")
    public CourseReviewDto findCourseReviewById(
            @ApiParam(value = "Defines id by which to search")
            @PathVariable("id") Integer id) {
        return courseReviewMapper.toDto(courseReviewService.findReviewById(id));
    }

    @ApiOperation(value = "Updates course review with given id and responses with "
            + "message, admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateCourseReview(@ApiParam(value = "Defines data to update")
                                         @RequestBody CourseReviewDto courseReviewDto,
                                         @ApiParam(value = "Defines id by which to update")
                                         @PathVariable("id") Integer id) {
        courseReviewService.updateReview(courseReviewMapper.toEntity(courseReviewDto), id);
        return messageDtoMapper.toDto("Successfully updated course review");
    }

    @ApiOperation(value = "Deletes course review with given id and responses with"
            + " message, admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteCourseReviewById(
            @ApiParam(value = "Defines id with which to delete")
            @PathVariable("id") Integer id) {
        courseReviewService.deleteReviewById(id);
        return messageDtoMapper.toDto("Successfully deleted course review");
    }
}
