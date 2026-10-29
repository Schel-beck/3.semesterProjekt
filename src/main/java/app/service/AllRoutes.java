package app.service;

import app.config.HibernateConfig;
import app.controllers.NotificationController;
import app.controllers.QuestionController;
import app.controllers.SubjectController;
import app.controllers.User_questionController;
import app.persistence.QuestionDAO;
import app.persistence.SubjectDAO;
import app.persistence.UserDAO;
import app.persistence.User_questionDAO;
import app.routes.*;
import app.security.controllers.SecurityController;
import app.security.persistence.SecurityDAO;
import app.security.routes.SecurityRoutes;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AllRoutes {
    private final EntityManagerFactory emf;
    public Routes allRoutes(){

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




        SubjectRoute subjectRoute = new SubjectRoute(subjectController);
        QuestionRoute questionRoute = new QuestionRoute(questionController);
        SecurityRoutes securityRoutes = new SecurityRoutes(securityController);
        User_questionRoutes userQuestionRoutes = new User_questionRoutes(userQuestionController);

        NotificationRoute notificationRoute = new NotificationRoute(notificationController);
        InteractiveChatRoute interactiveChatRoute = new InteractiveChatRoute();

        Routes routes = new Routes(
                subjectRoute,
                questionRoute,
                securityRoutes,
                interactiveChatRoute,
                userQuestionRoutes,
                notificationRoute
        );
        return routes;
    }
}
