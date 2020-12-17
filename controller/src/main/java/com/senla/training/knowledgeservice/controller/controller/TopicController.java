package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.TopicSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.topic.TopicDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.topic.TopicMapper;
import com.senla.training.knowledgeservice.service.service.topic.TopicService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.TOPIC_CONTROLLER_TAG)
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds topic and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addTopic(@ApiParam(value = "Defines data to insert")
                               @RequestBody TopicDto topicDto) {
        topicService.addTopic(topicMapper.toEntity(topicDto));
        return messageDtoMapper.toDto("Successfully added topic");
    }

    @ApiOperation(value = "Displays list of topics, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<TopicDto> findTopics(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    TopicSortCriterion criterion,
            @ApiParam(value = "Defines title to choose")
            @RequestParam(name = "title", required = false)
                    String title) {
        return topicMapper.listToDto(topicService.findTopics(criterion, title));
    }

    @ApiOperation(value = "Displays topic with given id, all authenticated",
            response = TopicDto.class)
    @GetMapping("/{id}")
    public TopicDto findTopicById(@ApiParam(value = "Defines id by which to search")
                                  @PathVariable("id") Integer id) {
        return topicMapper.toDto(topicService.findTopicById(id));
    }

    @ApiOperation(value = "Updates topic with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateTopic(@ApiParam(value = "Defines data for update")
                                  @RequestBody TopicDto topicDto,
                                  @ApiParam(value = "Defines id with which to update")
                                  @PathVariable("id") Integer id) {
        topicService.updateTopic(topicMapper.toEntity(topicDto), id);
        return messageDtoMapper.toDto("Successfully updated topic");
    }

    @ApiOperation(value = "Deletes topic with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteTopicById(@ApiParam(value = "Defines id with which to delete")
                                      @PathVariable("id") Integer id) {
        topicService.deleteTopicById(id);
        return messageDtoMapper.toDto("Successfully deleted topic");
    }
}
