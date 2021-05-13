package com.kaurpalang.mirth.graylogadapter.server;

import com.kaurpalang.mirth.annotationsplugin.annotation.ApiProvider;
import com.kaurpalang.mirth.annotationsplugin.type.ApiProviderType;
import com.kaurpalang.mirth.graylogadapter.server.handler.Log4jPropertiesHandler;
import com.mirth.connect.server.api.MirthServlet;
import com.kaurpalang.mirth.graylogadapter.shared.Constants;
import com.kaurpalang.mirth.graylogadapter.shared.model.LogSettingsInterface;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

@ApiProvider(type = ApiProviderType.SERVER_CLASS)
public class LogSettingsServlet extends MirthServlet implements LogSettingsInterface {
    private Log4jPropertiesHandler propertiesHandler;

    public LogSettingsServlet(@Context HttpServletRequest request, @Context SecurityContext sc) {
        super(request, sc, Constants.PLUGIN_POINTNAME);
        this.propertiesHandler = new Log4jPropertiesHandler();
    }

    /**
     * API endpoint to handle graylog appender state saving
     * @param data Incoming json from Admin interface.
     */
    @Override
    public void doSave(String data) {
        propertiesHandler.handleSave(data);
    }

    /**
     * API endpoint to display the current state of graylog appender
     */
    @Override
    public String getState() {
        return propertiesHandler.getJsonFromState();
    }
}
