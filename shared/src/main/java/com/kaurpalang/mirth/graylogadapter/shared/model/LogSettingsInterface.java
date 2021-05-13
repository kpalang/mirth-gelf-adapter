package com.kaurpalang.mirth.graylogadapter.shared.model;

import com.kaurpalang.mirth.annotationsplugin.annotation.ApiProvider;
import com.kaurpalang.mirth.annotationsplugin.type.ApiProviderType;
import com.mirth.connect.client.core.api.BaseServletInterface;
import com.mirth.connect.client.core.api.MirthOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;

@ApiProvider(type = ApiProviderType.SERVLET_INTERFACE)
@Path("/extensions/graylog")
@Tag(name = "Extension Services")
@Consumes({"application/json"})
@Produces({"application/json"})
public interface LogSettingsInterface extends BaseServletInterface {

    @POST
    @Path("/saveState")
    @Operation(
            summary = "Save new state of Graylog appender"
    )
    @MirthOperation(
            name = "doSave",
            display = "Save changes",
            permission = "manageExtensions"
    )
    void doSave(String json);

    @GET
    @Path("/getState")
    @Operation(
            summary = "Get current logger state from server"
    )
    @ApiResponse(
            content = {@Content(
                    mediaType = "application/json"
            )}
    )
    @MirthOperation(
            name = "getState",
            display = "Get current state of Graylog appender",
            permission = "manageExtensions"
    )
    String getState();
}
