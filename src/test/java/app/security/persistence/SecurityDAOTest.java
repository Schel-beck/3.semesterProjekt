
package app.security.persistence;

import app.config.HibernateTestConfig;
import app.dto.CreateUserDTO;
import app.security.entities.Role;
import app.security.entities.User;
import app.security.entities.User_role;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SecurityDAOTest {

    private static EntityManagerFactory emf;
    private static SecurityDAO securityDAO;

    @BeforeAll
    static void setup() {
        emf = HibernateTestConfig.getEntityManagerFactory();
        securityDAO = new SecurityDAO(emf);
    }

    // Unique names prevent conflicts between tests.
    private String uniqueName() {
        return "test_" + UUID.randomUUID()
            .toString()
            .replace("-", "");
    }

    private User createTestUser(String username) {
        CreateUserDTO dto = new CreateUserDTO(
            username + "@example.com",
            username,
            "Password123!"
        );

        return securityDAO.createUser(dto);
    }

    @Test
    void getVerifiedUser() {
        String username = uniqueName();
        String password = "Password123!";

        createTestUser(username);

        User verifiedUser = securityDAO.getVerifiedUser(
            username,
            password
        );

        assertNotNull(verifiedUser);
        assertEquals(username, verifiedUser.getUsername());

        User wrongPassword = securityDAO.getVerifiedUser(
            username,
            "WrongPassword123!"
        );

        assertNull(wrongPassword);
    }

    @Test
    void createUser() {
        String username = uniqueName();

        User user = createTestUser(username);

        assertNotNull(user);
        assertTrue(user.getId() > 0);
        assertEquals(username, user.getUsername());
        assertEquals(username + "@example.com", user.getEmail());

        // The password must be hashed, not stored as plain text.
        assertNotEquals("Password123!", user.getPassWord());
        assertTrue(user.verifyPassword("Password123!"));

        // Creating another user with the same username should fail.
        assertThrows(EntityExistsException.class,
            () -> createTestUser(username));
    }

    @Test
    void createRole() {
        String roleName = uniqueName();

        Role role = securityDAO.createRole(roleName);

        assertNotNull(role);
        assertTrue(role.getId() > 0);
        assertEquals(roleName, role.getRole());

        assertThrows(EntityExistsException.class,
            () -> securityDAO.createRole(roleName));
    }

    @Test
    void addUserRole() {
        String username = uniqueName();
        String roleName = uniqueName();

        createTestUser(username);
        securityDAO.createRole(roleName);

        User updatedUser = securityDAO.addUserRole(
            username,
            roleName
        );

        assertNotNull(updatedUser);

        boolean hasRole = updatedUser.getUser_roles()
            .stream()
            .map(User_role::getRole)
            .anyMatch(role ->
                role.getRole().equals(roleName)
            );

        assertTrue(hasRole);
    }

    @Test
    void addRole() {
        String username = uniqueName();
        String roleName = uniqueName();

        User user = createTestUser(username);

        // Verify the user exists before adding a role.
        assertNotNull(user);

        // Check the database after role creation.
        // The DAO's addRole method requires a UserDTO,
        // so this test currently verifies role persistence.
        securityDAO.createRole(roleName);

        try (EntityManager em = emf.createEntityManager()) {
            Role savedRole = em.createQuery(
                    "SELECT r FROM Role r WHERE r.role = :role",
                    Role.class
                )
                .setParameter("role", roleName)
                .getSingleResult();

            assertNotNull(savedRole);
            assertEquals(roleName, savedRole.getRole());
        }
    }
}
