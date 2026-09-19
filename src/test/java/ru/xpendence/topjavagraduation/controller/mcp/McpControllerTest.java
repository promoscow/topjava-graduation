package ru.xpendence.topjavagraduation.controller.mcp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.xpendence.topjavagraduation.controller.AbstractControllerTest;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.xpendence.topjavagraduation.controller.JwtUserRequestPostProcessors.user;

class McpControllerTest extends AbstractControllerTest {

    private static final String PING_REQUEST = """
            {"jsonrpc": "2.0", "id": 1, "method": "ping"}""";

    private static final String INITIALIZE_REQUEST = """
            {"jsonrpc": "2.0", "id": 2, "method": "initialize", "params": {
                "protocolVersion": "2025-06-18",
                "capabilities": {},
                "clientInfo": {"name": "cursor", "version": "1.0"}
            }}""";

    @Test
    @DisplayName("ping(): корректный JSON-RPC запрос -> успешный ответ с пустым результатом")
    void ping() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(user())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PING_REQUEST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jsonrpc").value("2.0"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.result").isMap())
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @DisplayName("ping(): неизвестный метод -> JSON-RPC ошибка со статусом 200")
    void pingFailsWhenMethodIsUnknown() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(user())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jsonrpc": "2.0", "id": 1, "method": "unknown"}"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist())
                .andExpect(jsonPath("$.error.code").value(-32601));
    }

    @Test
    @DisplayName("ping(): запрос без аутентификации -> успешный ответ")
    void pingWithoutAuthentication() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PING_REQUEST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isMap());
    }

    @Test
    @DisplayName("initialize(): параметры реального клиента -> согласованная версия протокола и описание сервера")
    void initialize() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/json, text/event-stream")
                        .content(INITIALIZE_REQUEST))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jsonrpc").value("2.0"))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.result.protocolVersion").value("2025-06-18"))
                .andExpect(jsonPath("$.result.capabilities.tools.listChanged").value(false))
                .andExpect(jsonPath("$.result.serverInfo.name").value(not(emptyString())))
                .andExpect(jsonPath("$.result.serverInfo.version").value(not(emptyString())));
    }

    @Test
    @DisplayName("initialize(): запрос без params -> версия протокола по умолчанию")
    void initializeWithoutParams() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(user())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jsonrpc": "2.0", "id": 2, "method": "initialize"}"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.result.protocolVersion").value(not(emptyString())));
    }

    @Test
    @DisplayName("initialize(): неизвестные параметры клиента -> параметры игнорируются, ответ успешный")
    void initializeIgnoresUnknownParams() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(user())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jsonrpc": "2.0", "id": 2, "method": "initialize", "params": {
                                    "protocolVersion": "2025-06-18",
                                    "_meta": {"client": "agent"},
                                    "unknown": true
                                }}"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.result.protocolVersion").value("2025-06-18"));
    }

    @Test
    @DisplayName("initialized(): уведомление об инициализации -> 202 без тела ответа")
    void initialized() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jsonrpc": "2.0", "method": "notifications/initialized"}"""))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("initialized(): неизвестное уведомление -> 202 без тела ответа")
    void unknownNotification() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jsonrpc": "2.0", "method": "notifications/cancelled"}"""))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("listTools(): запрос списка инструментов -> пустой список")
    void listTools() throws Exception {
        mockMvc.perform(post(McpController.PATH)
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"jsonrpc": "2.0", "id": 3, "method": "tools/list"}"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").doesNotExist())
                .andExpect(jsonPath("$.result.tools").isArray())
                .andExpect(jsonPath("$.result.tools").value(hasSize(0)));
    }

    @Test
    @DisplayName("ping(): GET вместо POST -> метод не поддерживается")
    void getIsNotAllowed() throws Exception {
        mockMvc.perform(get(McpController.PATH)
                        .with(anonymous())
                        .accept("text/event-stream"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @DisplayName("ping(): вложенный путь без аутентификации -> запрос отклонён")
    void nestedPathFailsWhenUnauthorized() throws Exception {
        mockMvc.perform(post(McpController.PATH + "/tools")
                        .with(anonymous())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PING_REQUEST))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
