package app;

import app.config.HibernateConfig;
import app.controllers.*;
import app.dto.UserDTO;
import app.routes.*;
import app.security.controllers.SecurityController;
import app.security.enums.Role;
import app.security.persistence.SecurityDAO;
import app.security.routes.SecurityRoutes;
import app.service.AllRoutes;
import app.service.Importer;
import app.service.InteractiveChat;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;
import io.javalin.security.RouteRole;
import jakarta.persistence.EntityManagerFactory;

import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        AllRoutes allRoutes = new AllRoutes(emf);
        SecurityDAO securityDAO = new SecurityDAO(emf);
        SecurityController securityController = new SecurityController(securityDAO);
        Importer importer = new Importer();
        importer.importIntoDatabase();


        Javalin app = Javalin.create(config -> {
            config.routes.apiBuilder(allRoutes.allRoutes().getRoutes());

            config.routes.beforeMatched(ctx -> {
                Set<RouteRole> roles = ctx.routeRoles();

                if (roles.contains(Role.ANYONE)) {
                    return;
                }

                securityController.authenticate().handle(ctx);

                UserDTO user = ctx.attribute("user");

                if (!securityController.authorize(user, roles)) {
                    throw new ForbiddenResponse("You don't have permission");
                }
            });
        }).start(7070);
    }
}