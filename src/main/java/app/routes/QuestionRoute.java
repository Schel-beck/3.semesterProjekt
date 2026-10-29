package app.routes;

import app.controllers.QuestionController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;
@NoArgsConstructor
public class QuestionRoute {
    private QuestionController questionController;

    protected EndpointGroup getRoutes() {

        return () -> {
            post("/subjects/{id}/questions", questionController::create,Role.ADMIN);
            get("/questions", questionController::readAll, Role.ANYONE);
            get("/questions/{id}", questionController::read,Role.ANYONE);
            put("/questions/{id}", questionController::update,Role.ADMIN);
            delete("/questions/{id}", questionController::delete,Role.ADMIN);
        };
    }

    public QuestionRoute(QuestionController questionController) {
        this.questionController = questionController;
    }
}
