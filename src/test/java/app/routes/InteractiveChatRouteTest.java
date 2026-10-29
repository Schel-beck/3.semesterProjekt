
package app.routes;

import io.javalin.Javalin;
import org.junit.jupiter.api.Test;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

class InteractiveChatRouteTest {

    @Test
    void getRoutes() {

        InteractiveChatRoute chatRoute = new InteractiveChatRoute();

        Javalin app = Javalin.create(config -> {
            config.routes.apiBuilder(chatRoute.getRoutes());
        }).start(0);

        try {
            int port = app.port();

            given()
                .port(port)
                .contentType("application/json")
                .body("""
                    {
                        "contents": []
                    }
                    """)
                .when()
                .post("/chat")
                .then()
                .statusCode(400);

        } finally {
            app.stop();
        }
    }
}
