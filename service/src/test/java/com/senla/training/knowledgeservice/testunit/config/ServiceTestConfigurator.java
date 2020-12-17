package com.senla.training.knowledgeservice.testunit.config;

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
import com.senla.training.knowledgeservice.service.security.CurrentUserProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan(basePackages = {"com.senla.training.knowledgeservice.service.service"})
public class ServiceTestConfigurator {

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
    public PasswordEncoder passwordEncoder() {
        return mock(PasswordEncoder.class);
    }

    @Bean
    public CurrentUserProvider currentUserProvider() {
        return mock(CurrentUserProvider.class);
    }
}
