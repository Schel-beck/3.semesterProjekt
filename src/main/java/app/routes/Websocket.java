package app.routes;

import app.controllers.QuestionController;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.websocket.WsContext;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static io.javalin.apibuilder.ApiBuilder.ws;

public class Websocket {
    private static Set<WsContext> clients = ConcurrentHashMap.newKeySet();
    private static final Logger log = LoggerFactory.getLogger(Websocket.class);

    public EndpointGroup getWs() {

        return () -> {
            ws("/", ws -> {

                ws.onConnect(ctx -> {
                    clients.add(ctx);
                    log.info("Client connected: " + ctx.sessionId());

                    broadcastPlayerCount();
                });

                ws.onClose(ctx -> {
                    clients.remove(ctx);
                    log.info("Client disconnected: " + ctx.sessionId());
                    broadcastPlayerCount();
                });

            });
        };
    }

    private static void broadcast(String message) {
        clients.removeIf(ctx -> !ctx.session.isOpen());
        clients.forEach(client -> client.send(message));
    }

    private static void broadcastPlayerCount() {
        String json = """
                {
                  "type": "players",
                  "count": %d
                }
                """.formatted(clients.size());

        broadcast(json);
    }
}
