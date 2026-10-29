
package app.routes;

import app.config.ApplicationConfig;
import app.config.HibernateTestConfig;
import app.entities.User_question;
import app.persistence.User_questionDAO;
import app.testUtils.Populator;
import io.javalin.Javalin;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class User_questionRoutesTest {

    private static Javalin app;

    private static final EntityManagerFactory emf =
        HibernateTestConfig.getEntityManagerFactory();

    private static User_questionDAO userQuestionDAO;

    private Map<String, User_question> userQuestionMap;

    @BeforeAll
    static void init() {
        app = ApplicationConfig.startServer(0, emf);

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = app.port();
    }

    @BeforeEach
    void setUp() {
        userQuestionDAO = new User_questionDAO(emf);
        userQuestionMap = Populator.userQuestionPopulate(emf);
    }

    @AfterAll
    static void shutDown() {
        if (app != null) {
            ApplicationConfig.stopServer(app);
        }
        RestAssured.reset();
    }

    @Test
    void getAllUserQuestions() {

        List<User_question> expected =
            userQuestionDAO.readAll();

        given()
            .when()
            .get("/user-questions")
            .then()
            .statusCode(200)
            .body("size()", is(3));

        assertEquals(3, expected.size());
    }

    @Test
    void getUserQuestionById() {

        User_question userQuestion =
            userQuestionMap.get("userQuestion1");

        given()
            .when()
            .get("/user-questions/" + userQuestion.getId())
            .then()
            .statusCode(200)
            .body("id", is(userQuestion.getId()))
            .body("answered", is(false));
    }

    @Test
    void getUserQuestionByIdNotFound() {

        given()
            .when()
            .get("/user-questions/999999")
            .then()
            .statusCode(404);
    }

    @Test
    void createUserQuestion() {

        User_question existing =
            userQuestionMap.get("userQuestion1");

        int userId = existing.getUser().getId();
        int questionId = existing.getQuestion().getId();

        given()
            .contentType("application/json")
            .body("""
                {
                    "userId": %d,
                    "questionId": %d,
                    "answered": true
                }
                """.formatted(userId, questionId))
            .when()
            .post("/user-questions")
            .then()
            .statusCode(201)
            .body("userId", is(userId))
            .body("questionId", is(questionId))
            .body("answered", is(true));
    }

    @Test
    void createUserQuestionInvalidBody() {

        given()
            .contentType("application/json")
            .body("""
                {
                    "userId": null,
                    "questionId": null,
                    "answered": null
                }
                """)
            .when()
            .post("/user-questions")
            .then()
            .statusCode(400);
    }

    @Test
    void updateUserQuestion() {

        User_question userQuestion =
            userQuestionMap.get("userQuestion1");

        int userId = userQuestion.getUser().getId();
        int questionId = userQuestion.getQuestion().getId();

        given()
            .contentType("application/json")
            .body("""
                {
                    "userId": %d,
                    "questionId": %d,
                    "answered": true
                }
                """.formatted(userId, questionId))
            .when()
            .put("/user-questions/" + userQuestion.getId())
            .then()
            .statusCode(200)
            .body("id", is(userQuestion.getId()))
            .body("answered", is(true));
    }

    @Test
    void updateUserQuestionNotFound() {

        given()
            .contentType("application/json")
            .body("""
                {
                    "userId": 1,
                    "questionId": 1,
                    "answered": true
                }
                """)
            .when()
            .put("/user-questions/999999")
            .then()
            .statusCode(404);
    }

    @Test
    void deleteUserQuestion() {

        User_question userQuestion =
            userQuestionMap.get("userQuestion1");

        given()
            .when()
            .delete("/user-questions/" + userQuestion.getId())
            .then()
            .statusCode(204);

        given()
            .when()
            .get("/user-questions/" + userQuestion.getId())
            .then()
            .statusCode(404);
    }

    @Test
    void deleteUserQuestionNotFound() {

        given()
            .when()
            .delete("/user-questions/999999")
            .then()
            .statusCode(404);
    }
}
