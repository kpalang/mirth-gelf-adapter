package com.kaurpalang.mirth.graylogadapter.server.handler;

import biz.paluch.logging.gelf.log4j.GelfLogAppender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaurpalang.mirth.graylogadapter.server.util.LoggerAppenderUtil;
import com.kaurpalang.mirth.graylogadapter.shared.Constants;
import com.kaurpalang.mirth.graylogadapter.shared.model.State;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.log4j.Logger;

import java.util.Map;

public class Log4jPropertiesHandler {
    private static final Logger logger = Logger.getLogger(Log4jPropertiesHandler.class);

    private ObjectMapper objectMapper;
    private FileBasedConfigurationBuilder<FileBasedConfiguration> propertiesFileBuilder;

    public Log4jPropertiesHandler() {
        this.objectMapper = new ObjectMapper();

        this.propertiesFileBuilder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(new Parameters().properties().setFileName("log4j.properties"));
    }

    public void handleSave(String jsonData) {
        try {
            State incomingState = objectMapper.readValue(jsonData, State.class);

            GelfLogAppender graylogAppenderInRoot = (GelfLogAppender) Logger.getRootLogger().getAppender(Constants.LOG4J_GRAYLOG_APPENDER_ID);
            State currentStateFromLogger = LoggerAppenderUtil.getCurrentStateFromAppender(graylogAppenderInRoot);
            //State currentStateFromProperties = LoggerAppenderUtil.getCurrentStateFromProperties(propertiesFileBuilder.getConfiguration());

            // TODO Saving of enable/disable in both logger and file

            // Save everything other than enabled state to file
            Map<String, Object> map = LoggerAppenderUtil.getMapOfParamsFromState(incomingState);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                propertiesFileBuilder.getConfiguration().setProperty(entry.getKey(), entry.getValue());
            }
            propertiesFileBuilder.save();

            // Graylog appender is currently disabled but should be enabled
            if (currentStateFromLogger == null && incomingState.isEnabled()) {
                // Add to logger
                GelfLogAppender appender = LoggerAppenderUtil.getGelfAppenderFromState(incomingState);
                Logger.getRootLogger().addAppender(appender);

                // Just append ',graylog' to key log4j.rootLogger
                String rootLoggerValue = propertiesFileBuilder.getConfiguration().getString(Constants.PROPERTIES_KEY_ROOTLOGGER);
                propertiesFileBuilder.getConfiguration().setProperty(Constants.PROPERTIES_KEY_ROOTLOGGER,
                        String.format("%s,%s", rootLoggerValue, Constants.LOG4J_GRAYLOG_APPENDER_ID));
                propertiesFileBuilder.save();

            // Graylog appender is currently enabled but should be disabled
            } else if (currentStateFromLogger != null && !incomingState.isEnabled()) {
                // Remove from logger
                Logger.getRootLogger().removeAppender(Constants.LOG4J_GRAYLOG_APPENDER_ID);

                // Remove from log4j.properties
                // Just replace ',graylog' with and empty string in log4j.rootLogger
                String rootLoggerValue = propertiesFileBuilder.getConfiguration().getString(Constants.PROPERTIES_KEY_ROOTLOGGER);
                propertiesFileBuilder.getConfiguration().setProperty(Constants.PROPERTIES_KEY_ROOTLOGGER,
                        rootLoggerValue.replace(String.format(",%s", Constants.LOG4J_GRAYLOG_APPENDER_ID), ""));
                propertiesFileBuilder.save();
            }

        } catch (Exception e) {
            logger.error(e);
        }
    }

    public String getJsonFromState() {
        String result = "";
        try {
            GelfLogAppender graylogAppenderInRoot = (GelfLogAppender) Logger.getRootLogger().getAppender(Constants.LOG4J_GRAYLOG_APPENDER_ID);

            State stateToSend = LoggerAppenderUtil.getCurrentStateFromAppender(graylogAppenderInRoot);

            if (stateToSend == null) {
                stateToSend = LoggerAppenderUtil.getCurrentStateFromProperties(propertiesFileBuilder.getConfiguration());
            }

            result = objectMapper.writeValueAsString(stateToSend);
        } catch (Exception e) {
            logger.error(e);
        }

        return result;
    }
}
