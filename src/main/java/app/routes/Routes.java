package app.routes;

import app.security.routes.SecurityRoutes;
import app.service.InteractiveChat;
import io.javalin.apibuilder.EndpointGroup;
import lombok.AllArgsConstructor;

import static io.javalin.apibuilder.ApiBuilder.path;
public class Routes {
    private SubjectRoute subjectRoute;
    private QuestionRoute questionRoute;
    private SecurityRoutes securityRoutes;
    private InteractiveChatRoute interactiveChatRoute;
    private User_questionRoutes userQuestionRoutes;


    public EndpointGroup getRoutes() {
        return () -> {
            path("/", subjectRoute.getRoutes());
            path("/", questionRoute.getRoutes());
            path("/", securityRoutes.getSecurityRoutes());
            path("/", interactiveChatRoute.getRoutes());
            path("/", userQuestionRoutes.getRoutes());
        };
    }

    public Routes(SubjectRoute subjectRoute, QuestionRoute questionRoute, SecurityRoutes securityRoutes,
                  InteractiveChatRoute interactiveChatRoute, User_questionRoutes userQuestionRoutes) {
        this.subjectRoute = subjectRoute;
        this.questionRoute = questionRoute;
        this.securityRoutes = securityRoutes;
        this.interactiveChatRoute = interactiveChatRoute;
        this.userQuestionRoutes = userQuestionRoutes;
    }
}
