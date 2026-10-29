package app.routes;

import app.config.HibernateConfig;
import app.config.HibernateTestConfig;
import app.controllers.SubjectController;
import app.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

import lombok.NoArgsConstructor;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;
public class SubjectRoute {
    private final SubjectController subjectController;

    public EndpointGroup getRoutes() {

        return () -> {
            post("/subjects", subjectController::create, Role.ADMIN);
            get("/subjects", subjectController::readAll, Role.ANYONE);
            get("/subjects/{id}", subjectController::read, Role.ANYONE);
            put("/subjects/{id}", subjectController::update, Role.ADMIN);
            delete("/subjects/{id}", subjectController::delete, Role.ADMIN);
        };
    }

    public SubjectRoute(SubjectController subjectController) {
        this.subjectController = subjectController;
    }
}
