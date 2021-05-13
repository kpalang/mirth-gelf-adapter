package com.kaurpalang.mirth.graylogadapter.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.log4j.Priority;

@Data
public class State {
    private boolean enabled;
    private String originHost;
    private GelfProtocol protocol;
    private String host;
    private Integer port;
    private String version;
    private Boolean includeLocation;
    private String facility;
    private Priority threshold;
    private Boolean includeFullMdc;

    @JsonIgnore
    public String getCombinedHost() {
        return String.format("%s:%s", protocol.toString().toLowerCase(), host);
    }
}
