package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.security.SecurityConfig;
import ru.otus.hw.services.RegistrationService;
import ru.otus.hw.utils.UserValidator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private UserValidator userValidator;

    @MockBean
    private UserDetailsService userDetailsService;

    @Test
    void loginPage() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void registrationPage() throws Exception {
        mvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    void adminPageAccess() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_USER"}
    )
    void adminPageAccessDinaedWithRoleUser() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPageAccessDinaedWithOutAuth() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }
}
