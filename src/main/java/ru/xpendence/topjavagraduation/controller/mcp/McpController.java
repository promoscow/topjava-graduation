package ru.xpendence.topjavagraduation.controller.mcp;

import com.googlecode.jsonrpc4j.JsonRpcMethod;
import com.googlecode.jsonrpc4j.JsonRpcParam;
import com.googlecode.jsonrpc4j.JsonRpcService;
import org.springframework.stereotype.Component;
import ru.xpendence.topjavagraduation.controller.mcp.model.InitializeResponse;
import ru.xpendence.topjavagraduation.controller.mcp.model.ServerInfo;
import ru.xpendence.topjavagraduation.controller.mcp.model.ToolsListResponse;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
@JsonRpcService(McpController.PATH)
public class McpController {

    public static final String PATH = "/mcp";

    private static final String DEFAULT_PROTOCOL_VERSION = "2025-11-25";
    private static final String SERVER_NAME = "topjava-graduation";
    private static final String SERVER_VERSION = "0.0.1-SNAPSHOT";

    private final McpToolRouter mcpToolRouter;

    public McpController(McpToolRouter mcpToolRouter) {
        this.mcpToolRouter = mcpToolRouter;
    }

    @JsonRpcMethod("initialize")
    public InitializeResponse initialize(
            @JsonRpcParam("protocolVersion") String protocolVersion
    ) {
        return new InitializeResponse(
                Objects.requireNonNullElse(protocolVersion, DEFAULT_PROTOCOL_VERSION),
                Map.of("tools", Map.of("listChanged", false)),
                new ServerInfo(SERVER_NAME, SERVER_VERSION)
        );
    }

    @JsonRpcMethod("notifications/initialized")
    public void initialized() {
    }

    @JsonRpcMethod("tools/list")
    public ToolsListResponse listTools() {
        return new ToolsListResponse(List.of());
    }

    @JsonRpcMethod("ping")
    public Map<String, Object> ping() {
        return Map.of();
    }
}
