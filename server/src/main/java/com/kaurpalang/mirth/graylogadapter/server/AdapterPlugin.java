package com.kaurpalang.mirth.graylogadapter.server;

import com.kaurpalang.mirth.annotationsplugin.annotation.ServerClass;
import com.mirth.connect.client.core.ControllerException;
import com.mirth.connect.model.ExtensionPermission;
import com.mirth.connect.plugins.ServicePlugin;
import com.mirth.connect.server.controllers.ConfigurationController;
import com.mirth.connect.util.ConfigurationProperty;
import com.kaurpalang.mirth.graylogadapter.shared.Constants;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@ServerClass
public class AdapterPlugin implements ServicePlugin {
    private static final Logger logger = Logger.getLogger(AdapterPlugin.class);

    private static final String PREFIX_KEY = "GlConfigPrefix";
    private String grayLogConfigPrefix = "GL.";

    @Override
    public void init(Properties properties) {
        updateMdcFields();
    }

    @Override
    public void update(Properties properties) {
        // We don't do anything here
    }

    /**
     * This method updates and tidies up the MDC fields.
     * Clears any other fields, checks if config key prefix field
     * exists in configMap, and then adds any fields with that
     * prefix to MDC with default values. Also sets environment value.
     */
    public void updateMdcFields() {
        Map<String, ConfigurationProperty> configMap = new HashMap<>();
        try {
            configMap = ConfigurationController.getInstance().getConfigurationProperties();
        } catch (Exception e) {
            logger.error(e);
        }

        if (configMap.containsKey(PREFIX_KEY)) {
            grayLogConfigPrefix = configMap.get(PREFIX_KEY).getValue();
        } else {
            configMap.put(PREFIX_KEY, new ConfigurationProperty(grayLogConfigPrefix, "Prefix to identify necessary fields to be sent to Graylog"));
            try {
                ConfigurationController.getInstance().setConfigurationProperties(configMap, true);
            } catch (ControllerException e) {
                logger.error(e);
            }
        }

        MDC.clear();

        ConfigurationProperty environment = configMap.getOrDefault(String.format("%s.environment", grayLogConfigPrefix), new ConfigurationProperty("TEST", ""));
        ConfigurationProperty program = configMap.getOrDefault(String.format("%s.program", grayLogConfigPrefix), new ConfigurationProperty("NextGen Connect", ""));
        MDC.put("environment", environment.getValue());
        MDC.put("program", program.getValue());

        for (Map.Entry<String, ConfigurationProperty> entry : configMap.entrySet()) {
            if (entry.getKey().startsWith(grayLogConfigPrefix)) {
                MDC.put(entry.getKey().replace(grayLogConfigPrefix, ""), entry.getValue().getValue());
            }
        }
    }

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }

    @Override
    public ExtensionPermission[] getExtensionPermissions() {
        return new ExtensionPermission[]{};
    }

    @Override
    public Map<String, Object> getObjectsForSwaggerExamples() {
        return new HashMap<>();
    }

    @Override
    public String getPluginPointName() {
        return Constants.PLUGIN_POINTNAME;
    }

    @Override
    public void start() {
        // We don't do anything here
    }

    @Override
    public void stop() {
        // We don't do anything here
    }
}
