package app.config;
import app.controllers.NotificationController;
import app.controllers.QuestionController;
import app.controllers.SubjectController;
import app.persistence.QuestionDAO;
import app.routes.*;
import app.security.routes.SecurityRoutes;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.EntityManagerFactory;

public class ApplicationConfig {

    public static Javalin startServer(int port,EntityManagerFactory emf) {
        QuestionDAO questionDAO = new QuestionDAO(emf);


        QuestionController questionController = new QuestionController(emf);
        SubjectController subjectController = new SubjectController(emf);
        SecurityRoutes securityRoutes = new SecurityRoutes(emf);
        NotificationController notificationController = new NotificationController();


        Routes routes = new Routes(
                new SubjectRoute(subjectController),
                new QuestionRoute(questionController),
                securityRoutes,
                new InteractiveChatRoute(),
                new User_questionRoutes(emf),
                new NotificationRoute(notificationController));

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(new ObjectMapper().registerModule(new JavaTimeModule()), false));
            config.routes.apiBuilder(routes.getRoutes());
            config.bundledPlugins.enableRouteOverview("/routes");
        });


        return app.start(port);
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }
}