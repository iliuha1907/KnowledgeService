package com.senla.training.knowledgeservice.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@EnableSwagger2
public class SpringFoxConfig {

    public static final String TOPIC_CONTROLLER_TAG = "TopicController";
    public static final String USER_CONTROLLER_TAG = "UserController";
    public static final String TEACHER_CONTROLLER_TAG = "TeacherController";
    public static final String SECTION_CONTROLLER_TAG = "SectionController";
    public static final String LESSON_SUBSCRIPTION_CONTROLLER_TAG =
            "LessonSubscriptionController";
    public static final String LESSON_REVIEW_CONTROLLER_TAG =
            "LessonReviewController";
    public static final String LESSON_CONTROLLER_TAG = "LessonController";
    public static final String COURSE_SUBSCRIPTION_CONTROLLER_TAG =
            "CourseSubscriptionController";
    public static final String COURSE_REVIEW_CONTROLLER_TAG =
            "CourseReviewController";
    public static final String COURSE_CONTROLLER_TAG = "CourseController";
    public static final String AUTHENTICATION_CONTROLLER_TAG =
            "AuthenticationController";

    @Bean
    public Docket apiDocket() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        parameterBuilder
                .name("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("Bearer abcdefg12345")
                .required(true)
                .build();
        List<Parameter> parameters = new ArrayList<>();
        parameters.add(parameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .tags(new Tag(TOPIC_CONTROLLER_TAG, "Manipulates with"
                        + " topics"))
                .tags(new Tag(USER_CONTROLLER_TAG, "Manipulates with users"))
                .tags(new Tag(TEACHER_CONTROLLER_TAG, "Manipulates with"
                        + " teachers"))
                .tags(new Tag(SECTION_CONTROLLER_TAG, "Manipulates with"
                        + " sections"))
                .tags(new Tag(LESSON_SUBSCRIPTION_CONTROLLER_TAG,
                        "Manipulates with lesson subscriptions"))
                .tags(new Tag(LESSON_REVIEW_CONTROLLER_TAG,
                        "Manipulates with lesson reviews"))
                .tags(new Tag(LESSON_CONTROLLER_TAG, "Manipulates "
                        + "with lesson"))
                .tags(new Tag(COURSE_SUBSCRIPTION_CONTROLLER_TAG,
                        "Manipulates  with course subscriptions"))
                .tags(new Tag(COURSE_REVIEW_CONTROLLER_TAG,
                        "Manipulates with course reviews"))
                .tags(new Tag(COURSE_CONTROLLER_TAG,
                        "Manipulates with courses"))
                .tags(new Tag(AUTHENTICATION_CONTROLLER_TAG, "Provides "
                        + "authentication operations"))
                .globalOperationParameters(parameters);
    }
}
