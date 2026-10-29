package app;

import app.config.HibernateConfig;
import app.controllers.*;
import app.routes.*;
import app.security.controllers.SecurityController;
import app.security.routes.SecurityRoutes;
import app.service.AllRoutes;
import app.service.Importer;
import app.service.InteractiveChat;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        AllRoutes allRoutes = new AllRoutes(emf);
        Importer importer = new Importer();
        importer.importIntoDatabase();


        Javalin app = Javalin.create(config -> {
            config.routes.apiBuilder(allRoutes.allRoutes().getRoutes());
        }).start(7070);
    }
}