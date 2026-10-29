
package app.routes;

import app.config.HibernateConfig;
import app.controllers.User_questionController;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class User_questionRoutes {

    private final User_questionController controller;

    public User_questionRoutes() {
        this(HibernateConfig.getEntityManagerFactory());
    }

    public User_questionRoutes(EntityManagerFactory emf) {
        this.controller = new User_questionController(emf);
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/user-questions", controller::readAll);
            get("/user-questions/{id}", controller::read);
            post("/user-questions", controller::create);
            put("/user-questions/{id}", controller::update);
            delete("/user-questions/{id}", controller::delete);
        };
    }
}

