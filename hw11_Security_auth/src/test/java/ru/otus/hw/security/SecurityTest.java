package ru.otus.hw.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.otus.hw.config.ConfigForSecurityTest;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@Import({SecurityConfig.class, ConfigForSecurityTest.class})
public class SecurityTest {

    private static final String URL_FOR_REDIRECT_SECURITY = "http://localhost/login";

    @Autowired
    private MockMvc mvc;

    @Test
    void accessWithoutAuthorization() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
        mvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(
            authorities = "ROLE_ADMIN"
    )
    void accessAdminPage() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @MethodSource("requestMappings")
    void accessDeniedWithoutAuthorization(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        mvc.perform(requestBuilder)
                .andExpect(status().isFound())
                .andExpect(redirectedUrl(URL_FOR_REDIRECT_SECURITY));
    }

    private static Stream<MockHttpServletRequestBuilder> requestMappings() {
        return Stream.of(
                get("/"),
                get("/book/1"),
                post("/book/1"),
                get("/book/new"),
                post("/book/new"),
                post("/deleteBook"),

                get("/admin"),

                get("/book/1/comments"),
                get("/book/1/comments/1/edit"),
                post("/book/1/comments"),
                post("/book/1/comments/1/edit"),
                post("/book/1/comments/1/delete")
        );
    }
}
