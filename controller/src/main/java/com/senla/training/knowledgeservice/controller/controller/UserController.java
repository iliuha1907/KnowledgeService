package com.senla.training.knowledgeservice.controller.controller;

import com.senla.training.knowledgeservice.common.sort.UserSortCriterion;
import com.senla.training.knowledgeservice.controller.config.SpringFoxConfig;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import com.senla.training.knowledgeservice.dto.dto.user.UserDto;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.user.UserMapper;
import com.senla.training.knowledgeservice.model.user.RoleType;
import com.senla.training.knowledgeservice.service.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = SpringFoxConfig.USER_CONTROLLER_TAG)
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MessageDtoMapper messageDtoMapper;

    @ApiOperation(value = "Adds user and responses with message, admin only",
            response = MessageDto.class)
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto addUser(@ApiParam(value = "Defines data to insert")
                              @RequestBody UserDto userDto) {
        userService.addUser(userMapper.toEntity(userDto));
        return messageDtoMapper.toDto("Successfully added user");
    }

    @ApiOperation(value = "Displays list of users, all authenticated",
            response = Iterable.class)
    @GetMapping
    public List<UserDto> findUsers(
            @ApiParam(value = "Defines sort criterion")
            @RequestParam(name = "criterion", defaultValue = "NATURAL")
                    UserSortCriterion criterion,
            @ApiParam(value = "Defines login to search")
            @RequestParam(name = "login", required = false)
                    String login,
            @ApiParam(value = "Defines type to search")
            @RequestParam(name = "type", required = false)
                    RoleType type) {
        return userMapper.listToDto(userService.findUsers(criterion, login, type));
    }

    @ApiOperation(value = "Displays user with given id, all authenticated",
            response = UserDto.class)
    @GetMapping("/{id}")
    public UserDto findUserById(@ApiParam(value = "Defines id by which to search")
                                @PathVariable("id") Integer id) {
        return userMapper.toDto(userService.findUserById(id));
    }

    @ApiOperation(value = "Updates user with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @PatchMapping("/{id}")
    public MessageDto updateUser(@ApiParam(value = "Defines data for update")
                                 @RequestBody UserDto userDto,
                                 @PathVariable("id") Integer id) {
        userService.updateUser(userMapper.toEntity(userDto), id);
        return messageDtoMapper.toDto("Successfully updated user");
    }

    @ApiOperation(value = "Deletes user with given id and responses with message,"
            + " admin only", response = MessageDto.class)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MessageDto deleteUserById(@ApiParam(value = "Defines id with which to delete")
                                     @PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return messageDtoMapper.toDto("Successfully deleted user");
    }
}
