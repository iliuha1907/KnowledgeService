package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.annotationapi.ConfigProperty;
import com.senla.training.hoteladmin.annotationapi.NeedInjectionClass;
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

@NeedInjectionClass
public class AppenderBuilder {

    private static final String APPENDER_NAME = "SqlAppender";
    private static final String LOGGER_NAME = "org.hibernate";
    private static final String OUTPUT_FILE_NAME = "logs/sqlLog.log";
    private static final Integer BUFFER_SIZE = 4000;
    @ConfigProperty(propertyName = "util.appenderBuilder.needSqlAppender", type = Boolean.class)
    private static Boolean needSqlAppender;
    @ConfigProperty(propertyName = "util.appenderBuilder.logLevel", type = String.class)
    private static String logLevel;

    public static void build() {
        if (!needSqlAppender) {
            return;
        }
        Level level;
        try {
            level = Level.valueOf(logLevel.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IncorrectWorkException(ex.getMessage());
        }

        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        Appender appender = FileAppender.newBuilder()
                .withFileName(OUTPUT_FILE_NAME)
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