package app.security.routes;

import app.security.controllers.SecurityController;
import jakarta.persistence.EntityManagerFactory;
import app.security.enums.Role;
import app.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.security.RouteRole;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class SecurityRoutes {
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private final SecurityController securityController;

    public SecurityRoutes(EntityManagerFactory emf) {
        this.securityController = new SecurityController(emf);
    }
    public  EndpointGroup getSecurityRoutes() {
        return ()->{
            path("/auth", ()->{
                get("/healthcheck", securityController::healthCheck, Role.ANYONE);
                get("/test", ctx->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from Open Deployment")), Role.ANYONE);
                post("/login", securityController.login(), Role.ANYONE);
                post("/register", securityController.register(), Role.ANYONE);
                before("/user/addrole", securityController.authenticate());
                post("/user/addrole", securityController.addRole(), Role.ADMIN);
            });
        };
    }
}
