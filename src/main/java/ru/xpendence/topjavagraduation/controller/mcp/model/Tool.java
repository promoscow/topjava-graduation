package ru.xpendence.topjavagraduation.controller.mcp.model;

import java.util.Map;

public record Tool(
        String name,
        String description,
        Map<String, Object> inputSchema
) {
}
