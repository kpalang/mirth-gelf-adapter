package com.kaurpalang.mirth.graylogadapter.server.util;

import biz.paluch.logging.gelf.log4j.GelfLogAppender;
import com.kaurpalang.mirth.graylogadapter.shared.Constants;
import com.kaurpalang.mirth.graylogadapter.shared.model.GelfProtocol;
import com.kaurpalang.mirth.graylogadapter.shared.model.State;
import org.apache.commons.configuration2.Configuration;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class LoggerAppenderUtil {
    private static final Logger logger = Logger.getLogger(LoggerAppenderUtil.class);

    private LoggerAppenderUtil() {}

    public static State getCurrentStateFromAppender(GelfLogAppender graylogAppenderinRootLogger) {
        State stateFromAppender = null;

        // If root logger does not have gelfappender, return null, otherwise generate a new state from it
        if (graylogAppenderinRootLogger != null) {
            stateFromAppender = new State();
            stateFromAppender.setEnabled(true);
            stateFromAppender.setOriginHost(graylogAppenderinRootLogger.getOriginHost());
            stateFromAppender.setPort(graylogAppenderinRootLogger.getGraylogPort());

            String[] protocolHostString = graylogAppenderinRootLogger.getHost().split(":");
            stateFromAppender.setProtocol(GelfProtocol.valueOf(protocolHostString[0].toUpperCase()));
            stateFromAppender.setHost(protocolHostString[1]);

            stateFromAppender.setVersion(graylogAppenderinRootLogger.getVersion());
            stateFromAppender.setFacility(graylogAppenderinRootLogger.getFacility());
            stateFromAppender.setIncludeLocation(graylogAppenderinRootLogger.isIncludeLocation());
            stateFromAppender.setThreshold(graylogAppenderinRootLogger.getThreshold());
            stateFromAppender.setIncludeFullMdc(graylogAppenderinRootLogger.isIncludeFullMdc());
        }

        return stateFromAppender;
    }

    public static State getCurrentStateFromProperties(Configuration props) {
        State stateFromProperties = new State();

        try {
            stateFromProperties.setEnabled(props.getString(Constants.PROPERTIES_KEY_ROOTLOGGER).contains(Constants.LOG4J_GRAYLOG_APPENDER_ID));
            stateFromProperties.setOriginHost(props.getString(Constants.PROPERTIES_KEY_ORIGINHOST));

            String[] protocolHostString = props.getString(Constants.PROPERTIES_KEY_HOST).split(":");
            stateFromProperties.setProtocol(GelfProtocol.valueOf(protocolHostString[0].toUpperCase()));
            stateFromProperties.setHost(protocolHostString[1]);

            stateFromProperties.setPort(props.getInt(Constants.PROPERTIES_KEY_PORT));
            stateFromProperties.setVersion(props.getString(Constants.PROPERTIES_KEY_VERSION));
            stateFromProperties.setIncludeLocation(props.getBoolean(Constants.PROPERTIES_KEY_INCLUDELOCATION));
            stateFromProperties.setFacility(props.getString(Constants.PROPERTIES_KEY_FACILITY));

            // Ssshhhh, I can't be bothered to fix this...
            String levelString = props.getString(Constants.PROPERTIES_KEY_THRESHOLD);
            Level levelFromString = Level.toLevel(levelString);

            stateFromProperties.setThreshold(levelFromString);
            stateFromProperties.setIncludeFullMdc(props.getBoolean(Constants.PROPERTIES_KEY_INCLUDEFULLMDC));

        } catch (Exception e) {
            logger.error(e);
        }

        return stateFromProperties;
    }

    public static GelfLogAppender getGelfAppenderFromState(State state) {
        GelfLogAppender appender = new GelfLogAppender();
        appender.setName(Constants.LOG4J_GRAYLOG_APPENDER_ID);
        appender.setOriginHost(state.getOriginHost());
        appender.setHost(state.getCombinedHost());
        appender.setGraylogPort(state.getPort());
        appender.setVersion(state.getVersion());
        appender.setIncludeFullMdc(state.getIncludeFullMdc());
        appender.setFacility(state.getFacility());
        appender.setThreshold(state.getThreshold());
        appender.setIncludeLocation(state.getIncludeLocation());

        return appender;
    }

    public static Map<String, Object> getMapOfParamsFromState(State state) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();

        result.put(Constants.PROPERTIES_KEY_ORIGINHOST, state.getOriginHost());
        result.put(Constants.PROPERTIES_KEY_HOST, state.getCombinedHost());
        result.put(Constants.PROPERTIES_KEY_PORT, state.getPort());
        result.put(Constants.PROPERTIES_KEY_VERSION, state.getVersion());
        result.put(Constants.PROPERTIES_KEY_INCLUDEFULLMDC, state.getIncludeFullMdc());
        result.put(Constants.PROPERTIES_KEY_FACILITY, state.getFacility());
        result.put(Constants.PROPERTIES_KEY_THRESHOLD, state.getThreshold());
        result.put(Constants.PROPERTIES_KEY_INCLUDELOCATION, state.getIncludeLocation());

        return result;
    }
}
