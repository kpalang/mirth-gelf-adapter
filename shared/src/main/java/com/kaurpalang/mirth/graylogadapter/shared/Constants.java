package com.kaurpalang.mirth.graylogadapter.shared;

public final class Constants {

    private Constants() {}

    public static final String PLUGIN_POINTNAME = "Graylog Adapter";
    public static final String SETTINGSPANEL_LABEL = "Graylog";

    public static final String LOG4J_GRAYLOG_APPENDER_ID = "graylog";
    public static final String GRAYLOG_APPENDER_PREFIX = "log4j.appender.".concat(LOG4J_GRAYLOG_APPENDER_ID).concat(".");

    // Keys for appender values in log4j.properties file
    public static final String PROPERTIES_KEY_ROOTLOGGER = "log4j.rootLogger";
    public static final String PROPERTIES_KEY_ORIGINHOST = GRAYLOG_APPENDER_PREFIX.concat("OriginHost");
    public static final String PROPERTIES_KEY_HOST = GRAYLOG_APPENDER_PREFIX.concat("Host");
    public static final String PROPERTIES_KEY_PORT = GRAYLOG_APPENDER_PREFIX.concat("Port");
    public static final String PROPERTIES_KEY_VERSION = GRAYLOG_APPENDER_PREFIX.concat("Version");
    public static final String PROPERTIES_KEY_INCLUDELOCATION = GRAYLOG_APPENDER_PREFIX.concat("IncludeLocation");
    public static final String PROPERTIES_KEY_FACILITY = GRAYLOG_APPENDER_PREFIX.concat("Facility");
    public static final String PROPERTIES_KEY_THRESHOLD = GRAYLOG_APPENDER_PREFIX.concat("Threshold");
    public static final String PROPERTIES_KEY_INCLUDEFULLMDC = GRAYLOG_APPENDER_PREFIX.concat("IncludeFullMdc");

}
