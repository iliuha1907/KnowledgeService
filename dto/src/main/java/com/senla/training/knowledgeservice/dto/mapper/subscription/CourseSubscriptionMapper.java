package com.senla.training.knowledgeservice.dto.mapper.subscription;

import com.senla.training.knowledgeservice.dto.dto.subscription.CourseSubscriptionDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.subscription.course.CourseSubscription;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class CourseSubscriptionMapper extends AbstractMapper<CourseSubscription, CourseSubscriptionDto> {

    @Override
    @Nonnull
    protected Class<CourseSubscription> getEntityClass() {
        return CourseSubscription.class;
    }

    @Override
    @Nonnull
    protected Class<CourseSubscriptionDto> getDtoClass() {
        return CourseSubscriptionDto.class;
    }
}
