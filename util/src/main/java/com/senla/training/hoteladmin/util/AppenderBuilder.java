package com.senla.training.hoteladmin.util;

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

@Component
public class AppenderBuilder {

    private final String appenderName = "SqlAppender";
    private final String loggerName = "org.hibernate";
    private final String outputFileName = "logs/sqlLog.log";
    private final Integer bufferSize = 4000;
    @Value("${util.appenderBuilder.needSqlAppender:true}")
    private boolean needSqlAppender;
    @Value("${util.appenderBuilder.logLevel:debug}")
    private String logLevel;

    public void build() {
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
                .withFileName(outputFileName)
                .withAppend(false)
                .withBufferedIo(true)
                .withBufferSize(bufferSize)
                .setConfiguration(config)
                .setName(appenderName)
                .setLayout(PatternLayout.createDefaultLayout())
                .build();
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(appenderName, null, null);
        AppenderRef[] refs = new AppenderRef[]{ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger(false, level, loggerName,
                "true", refs, null, config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(loggerName, loggerConfig);
        ctx.updateLoggers();
    }
}
