package com.senla.training.knowledgeservice.dto.mapper.user;

import com.senla.training.knowledgeservice.dto.dto.user.UserDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.user.User;
import com.senla.training.knowledgeservice.model.userProfile.UserProfile;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;

@Component
public class UserMapper extends AbstractMapper<User, UserDto> {

    @Autowired
    private ModelMapper mapper;

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(User.class, UserDto.class)
                .addMappings(m -> {
                    m.skip(UserDto::setFirstName);
                    m.skip(UserDto::setLastName);
                    m.skip(UserDto::setBirthDate);
                    m.skip(UserDto::setPassword);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(UserDto.class, User.class)
                .addMappings(m -> m.skip(User::setProfile)).setPostConverter(toEntityConverter());
    }

    @Override
    @Nonnull
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    @Nonnull
    protected Class<UserDto> getDtoClass() {
        return UserDto.class;
    }

    @Nonnull
    private Converter<UserDto, User> toEntityConverter() {
        return context -> {
            UserDto source = context.getSource();
            User destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    @Nonnull
    private Converter<User, UserDto> toDtoConverter() {
        return context -> {
            User source = context.getSource();
            UserDto destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(@Nonnull UserDto source, @Nonnull User destination) {
        UserProfile userProfile = new UserProfile(source.getFirstName(),
                source.getLastName(), source.getBirthDate());
        destination.setProfile(userProfile);
    }

    private void mapSpecificFields(@Nonnull User source, @Nonnull UserDto destination) {
        UserProfile userProfile = source.getProfile();
        if (userProfile != null) {
            destination.setFirstName(userProfile.getFirstName());
            destination.setLastName(userProfile.getLastName());
            destination.setBirthDate(userProfile.getBirthDate());
            destination.setPassword("");
        }
    }
}
