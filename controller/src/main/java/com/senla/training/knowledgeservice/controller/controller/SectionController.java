package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.SectionSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.section.SectionDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.section.SectionMapper;
import com.senla.training.knowledgeservice.service.service.section.SectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.SECTION_CONTROLLER_TAG)
@RequestMapping("/api/sections")
public class SectionController {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private SectionMapper sectionMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds section and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addSection(@ApiParam(value = "Defines data to insert")
                                 @RequestBody SectionDto sectionDto) {
        sectionService.addSection(sectionMapper.toEntity(sectionDto));
        return messageDtoMapper.toDto("Successfully added section");
    }

    @ApiOperation(value = "Displays list of sections, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<SectionDto> findSections(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    SectionSortCriterion criterion,
            @ApiParam(value = "Defines title to search by")
            @RequestParam(name = "title", required = false)
                    String title,
            @ApiParam(value = "Defines id of topic to search by")
            @RequestParam(name = "topicId", required = false)
                    Integer topicId) {
        return sectionMapper.listToDto(sectionService.findSections(criterion, title,
                topicId));
    }

    @ApiOperation(value = "Displays section with given id, all authenticated",
            response = SectionDto.class)
    @GetMapping("/{id}")
    public SectionDto findSectionById(@ApiParam(value = "Defines id by which to search")
                                      @PathVariable("id") Integer id) {
        return sectionMapper.toDto(sectionService.findSectionById(id));
    }

    @ApiOperation(value = "Updates section with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto updateSection(@ApiParam(value = "Defines data to update")
                                    @RequestBody SectionDto sectionDto,
                                    @ApiParam(value = "Defines id by which to update")
                                    @PathVariable("id") Integer id) {
        sectionService.updateSection(sectionMapper.toEntity(sectionDto), id);
        return messageDtoMapper.toDto("Successfully updated section");
    }

    @ApiOperation(value = "Deletes section with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteSectionById(@ApiParam(value = "Defines id with which to delete")
                                        @PathVariable("id") Integer id) {
        sectionService.deleteSectionById(id);
        return messageDtoMapper.toDto("Successfully deleted section");
    }
}
