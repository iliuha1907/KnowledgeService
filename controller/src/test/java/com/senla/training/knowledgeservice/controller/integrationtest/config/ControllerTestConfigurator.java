package com.senla.training.knowledgeservice.controller.integrationtest.config;

import com.senla.training.knowledgeservice.controller.security.TokenProvider;
import com.senla.training.knowledgeservice.dao.course.CourseDao;
import com.senla.training.knowledgeservice.dao.lesson.LessonDao;
import com.senla.training.knowledgeservice.dao.review.course.CourseReviewDao;
import com.senla.training.knowledgeservice.dao.review.lesson.LessonReviewDao;
import com.senla.training.knowledgeservice.dao.section.SectionDao;
import com.senla.training.knowledgeservice.dao.subscription.course.CourseSubscriptionDao;
import com.senla.training.knowledgeservice.dao.subscription.lesson.LessonSubscriptionDao;
import com.senla.training.knowledgeservice.dao.teacher.TeacherDao;
import com.senla.training.knowledgeservice.dao.topic.TopicDao;
import com.senla.training.knowledgeservice.dao.user.UserDao;
import com.senla.training.knowledgeservice.dto.mapper.course.CourseMapper;
import com.senla.training.knowledgeservice.dto.mapper.lesson.LessonMapper;
import com.senla.training.knowledgeservice.dto.mapper.message.MessageDtoMapper;
import com.senla.training.knowledgeservice.dto.mapper.review.CourseReviewMapper;
import com.senla.training.knowledgeservice.dto.mapper.review.LessonReviewMapper;
import com.senla.training.knowledgeservice.dto.mapper.section.SectionMapper;
import com.senla.training.knowledgeservice.dto.mapper.subscription.CourseSubscriptionMapper;
import com.senla.training.knowledgeservice.dto.mapper.subscription.LessonSubscriptionMapper;
import com.senla.training.knowledgeservice.dto.mapper.teacher.TeacherMapper;
import com.senla.training.knowledgeservice.dto.mapper.topic.TopicMapper;
import com.senla.training.knowledgeservice.dto.mapper.user.UserMapper;
import com.senla.training.knowledgeservice.dto.security.TokenDtoMapper;
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {"com.senla.training.knowledgeservice.service.service",
        "com.senla.training.knowledgeservice.controller.controller"})
public class ControllerTestConfigurator {

    @Bean
    public CourseDao courseDao() {
        return mock(CourseDao.class);
    }

    @Bean
    public LessonDao lessonDao() {
        return mock(LessonDao.class);
    }

    @Bean
    public LessonReviewDao lessonReviewDao() {
        return mock(LessonReviewDao.class);
    }

    @Bean
    public CourseReviewDao courseReviewDao() {
        return mock(CourseReviewDao.class);
    }

    @Bean
    public LessonSubscriptionDao lessonSubscriptionDao() {
        return mock(LessonSubscriptionDao.class);
    }

    @Bean
    public CourseSubscriptionDao courseSubscriptionDao() {
        return mock(CourseSubscriptionDao.class);
    }

    @Bean
    public TeacherDao teacherDao() {
        return mock(TeacherDao.class);
    }

    @Bean
    public TopicDao topicDao() {
        return mock(TopicDao.class);
    }

    @Bean
    public UserDao userDao() {
        return mock(UserDao.class);
    }

    @Bean
    public SectionDao sectionDao() {
        return mock(SectionDao.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return mock(AuthenticationManager.class);
    }

    @Bean
    public ModelMapper modelMapper() {
        return mock(ModelMapper.class);
    }

    @Bean
    public CourseMapper courseMapper() {
        return mock(CourseMapper.class);
    }

    @Bean
    public CourseReviewMapper courseReviewMapper() {
        return mock(CourseReviewMapper.class);
    }

    @Bean
    public CourseSubscriptionMapper courseSubscriptionMapper() {
        return mock(CourseSubscriptionMapper.class);
    }

    @Bean
    public LessonMapper lessonMapper() {
        return mock(LessonMapper.class);
    }

    @Bean
    public LessonReviewMapper lessonReviewMapper() {
        return mock(LessonReviewMapper.class);
    }

    @Bean
    public LessonSubscriptionMapper lessonSubscriptionMapper() {
        return mock(LessonSubscriptionMapper.class);
    }

    @Bean
    public MessageDtoMapper messageDtoMapper() {
        return mock(MessageDtoMapper.class);
    }

    @Bean
    public SectionMapper sectionMapper() {
        return mock(SectionMapper.class);
    }

    @Bean
    public TeacherMapper teacherMapper() {
        return mock(TeacherMapper.class);
    }

    @Bean
    public TopicMapper topicMapper() {
        return mock(TopicMapper.class);
    }

    @Bean
    public UserMapper userMapper() {
        return mock(UserMapper.class);
    }

    @Bean
    public TokenDtoMapper tokenDtoMapper() {
        return mock(TokenDtoMapper.class);
    }

    @Bean
    public TokenProvider tokenProvider() {
        return mock(TokenProvider.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return mock(PasswordEncoder.class);
    }

    @Bean
    public CurrentUserProvider currentUserProvider() {
        return mock(CurrentUserProvider.class);
    }
}
