package ru.xpendence.topjavagraduation.controller.mcp.model;

import java.util.Map;

public record InitializeResponse(
        String protocolVersion,
        Map<String, Object> capabilities,
        ServerInfo serverInfo
) {
}
