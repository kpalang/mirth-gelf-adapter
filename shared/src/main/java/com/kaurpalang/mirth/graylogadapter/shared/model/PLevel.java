package com.kaurpalang.mirth.graylogadapter.shared.model;

import org.apache.log4j.Level;

import java.util.Arrays;
import java.util.List;

public class PLevel extends Level {
    /**
     * Instantiate a Level object.
     *
     * @param level
     * @param levelStr
     * @param syslogEquivalent
     */
    protected PLevel(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    public static List<org.apache.log4j.Level> getAllPossibleLevels() {
        return Arrays.asList(Level.FATAL, Level.ERROR, Level.WARN, Level.INFO, Level.DEBUG);
    }
}
