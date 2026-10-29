
package app.routes;

import app.config.HibernateConfig;
import app.controllers.User_questionController;
import app.security.entities.User;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class User_questionRoutes {

    private final User_questionController userQuestionController;


    public User_questionRoutes(User_questionController userQuestionController) {
        this.userQuestionController = userQuestionController;
    }

    public EndpointGroup getRoutes() {
        return () -> {
            get("/user-questions", userQuestionController::readAll);
            get("/user-questions/{id}", userQuestionController::read);
            post("/user-questions", userQuestionController::create);
            put("/user-questions/{id}", userQuestionController::update);
            delete("/user-questions/{id}", userQuestionController::delete);
        };
    }
}

