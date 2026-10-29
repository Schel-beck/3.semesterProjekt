package app.config;
import app.controllers.NotificationController;
import app.controllers.QuestionController;
import app.controllers.SubjectController;
import app.controllers.User_questionController;
import app.dto.UserDTO;
import app.persistence.QuestionDAO;
import app.persistence.SubjectDAO;
import app.persistence.UserDAO;
import app.persistence.User_questionDAO;
import app.routes.*;
import app.security.controllers.SecurityController;
import app.security.enums.Role;
import app.security.persistence.SecurityDAO;
import app.security.routes.SecurityRoutes;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;

public class ApplicationConfig {

    public static Javalin startServer(int port,EntityManagerFactory emf) {
        QuestionDAO questionDAO = new QuestionDAO(emf);
        SubjectDAO subjectDAO = new SubjectDAO(emf);
        SecurityDAO securityDAO = new SecurityDAO(emf);
        User_questionDAO userQuestionDAO = new User_questionDAO(emf);
        UserDAO userDAO = new UserDAO(emf);


        QuestionController questionController = new QuestionController(subjectDAO,questionDAO);
        SubjectController subjectController = new SubjectController(subjectDAO);
        SecurityController securityController = new SecurityController(securityDAO);
        User_questionController userQuestionController = new User_questionController(
                userDAO,questionDAO,userQuestionDAO);
        NotificationController notificationController = new NotificationController();


        Routes routes = new Routes(
                new SubjectRoute(subjectController),
                new QuestionRoute(questionController),
                new SecurityRoutes(securityController),
                new InteractiveChatRoute(),
                new User_questionRoutes(userQuestionController),
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