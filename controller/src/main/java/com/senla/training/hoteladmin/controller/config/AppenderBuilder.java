package com.senla.training.hoteladmin.controller.config;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AppenderBuilder {

    private static final String APPENDER_NAME = "SqlAppender";
    private static final String LOGGER_NAME = "org.hibernate";
    private static final Integer BUFFER_SIZE = 4000;
    @Value("${util.appenderBuilder.folderName:logs}")
    private String folderName;
    private String fileName = "sqlLog.log";
    @Value("${util.appenderBuilder.needSqlAppender:true}")
    private boolean needSqlAppender;
    @Value("${util.appenderBuilder.logLevel:debug}")
    private String logLevel;

    @PostConstruct
    public void build() {
        if (!needSqlAppender) {
            return;
        }
        String outputFilePath = folderName + "/" + fileName;
        Level level;
        try {
            level = Level.valueOf(logLevel.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        Appender appender = FileAppender.newBuilder()
                .withFileName(outputFilePath)
                .withAppend(false)
                .withBufferedIo(true)
                .withBufferSize(BUFFER_SIZE)
                .setConfiguration(config)
                .setName(APPENDER_NAME)
                .setLayout(PatternLayout.createDefaultLayout())
                .build();
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(APPENDER_NAME, null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, level, LOGGER_NAME,
                "true", refs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(LOGGER_NAME, loggerConfig);
        ctx.updateLoggers();
    }
}
