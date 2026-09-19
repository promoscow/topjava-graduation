package ru.xpendence.topjavagraduation.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.JsonRpcBasicServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import ru.xpendence.topjavagraduation.controller.mcp.McpController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Configuration
public class JsonRpcConfig {

    @Bean
    public JsonRpcBasicServer mcpJsonRpcServer(ObjectMapper objectMapper, McpController controller) {
        var server = new JsonRpcBasicServer(objectMapper, controller);
        server.setAllowLessParams(true);
        server.setAllowExtraParams(true);
        return server;
    }

    @Bean
    public SimpleUrlHandlerMapping mcpHandlerMapping(JsonRpcBasicServer mcpJsonRpcServer, ObjectMapper objectMapper) {
        return new SimpleUrlHandlerMapping(
                Map.of(McpController.PATH, mcpHandler(mcpJsonRpcServer, objectMapper)),
                Ordered.HIGHEST_PRECEDENCE
        );
    }

    /**
     * Экспортёры jsonrpc4j ({@code AutoJsonRpcServiceImplExporter}, {@code JsonServiceExporter})
     * и {@code JsonRpcServer} собраны под {@code javax.servlet}, поэтому в Spring 6 не используются:
     * запрос отдаём серверу потоками, транспортный слой реализуем сами по MCP Streamable HTTP —
     * ошибки JSON-RPC отдаём со статусом 200 (иначе клиент считает их сбоем транспорта),
     * на уведомления отвечаем 202 без тела, на не-POST — 405, чтобы клиент не ждал SSE-поток.
     */
    private HttpRequestHandler mcpHandler(JsonRpcBasicServer server, ObjectMapper objectMapper) {
        return (request, response) -> {
            if (!HttpMethod.POST.matches(request.getMethod())) {
                response.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
                return;
            }
            var requestBody = request.getInputStream().readAllBytes();
            var responseBody = new ByteArrayOutputStream();
            server.handleRequest(new ByteArrayInputStream(requestBody), responseBody);
            if (isNotification(objectMapper, requestBody)) {
                response.setStatus(HttpStatus.ACCEPTED.value());
                return;
            }
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            responseBody.writeTo(response.getOutputStream());
        };
    }

    private boolean isNotification(ObjectMapper objectMapper, byte[] requestBody) {
        try {
            var request = objectMapper.readTree(requestBody);
            return request.isObject() && !request.has("id");
        } catch (IOException e) {
            return false;
        }
    }
}
