package com.senla.training.knowledgeservice.dto.mapper.subscription;

import com.senla.training.knowledgeservice.dto.dto.subscription.LessonSubscriptionDto;
import com.senla.training.knowledgeservice.dto.mapper.AbstractMapper;
import com.senla.training.knowledgeservice.model.subscription.lesson.LessonSubscription;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class LessonSubscriptionMapper extends AbstractMapper<LessonSubscription, LessonSubscriptionDto> {

    @Override
    @Nonnull
    protected Class<LessonSubscription> getEntityClass() {
        return LessonSubscription.class;
    }

    @Override
    @Nonnull
    protected Class<LessonSubscriptionDto> getDtoClass() {
        return LessonSubscriptionDto.class;
    }
}
