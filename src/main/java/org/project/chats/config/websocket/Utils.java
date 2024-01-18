package org.project.chats.config.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.project.chats.domain.SocketMessage;

public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // TODO : Exception JSON
    public static SocketMessage getObject(String socketMessage) throws JsonProcessingException {
        return objectMapper.readValue(socketMessage, SocketMessage.class);
    }

    public static String getString(SocketMessage socketMessage) throws JsonProcessingException {
        return objectMapper.writeValueAsString(socketMessage);
    }
}
