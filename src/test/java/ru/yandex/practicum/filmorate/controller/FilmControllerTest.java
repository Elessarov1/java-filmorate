package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    private FilmController filmController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
        assertThat(filmController).isNotNull();
    }

    @SneakyThrows
    @Test
    void validationWithValidData() {
        String validFilm = objectMapper.writeValueAsString(Film.builder()
                .name("nisi eiusmod")
                .description("adipisicing")
                .releaseDate(LocalDate.of(1967, 3,25))
                .duration(100)
                .build());
        mockMvc.perform(post("/films")
                .contentType("application/json")
                .content(validFilm)).andDo(
                h-> {
                    assertEquals(200, h.getResponse().getStatus());
                }
        );
        mockMvc.perform(get("/films")
                .contentType("application/json")).andDo(h->
                assertEquals(200, h.getResponse().getStatus())
        );

    }

    @SneakyThrows
    @Test
    void validationWithInvalidData() {
        String inValidFilm =
                objectMapper.writeValueAsString(Film.builder()
                        .name("nisi eiusmod")
                        .description("adipisicing")
                        .releaseDate(LocalDate.of(1967, 3,25))
                        .duration(-100)
                        .build());
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(inValidFilm))
                        .andDo(
                        h-> {
                            assertEquals(400, h.getResponse().getStatus());
                        }
        );
    }

    @SneakyThrows
    @Test
    void validationWithEmptyBody() {
        mockMvc.perform(post("/films")
                        .contentType("application/json")
                        .content(""))
                .andDo(
                        h-> {
                            assertEquals(400, h.getResponse().getStatus());
                        }
                );
    }

    @SneakyThrows
    @Test
    void getRequestWithWrongURI() {
        mockMvc.perform(get("/film")
                .contentType("application/json"))
                .andDo(
                        h->
                assertEquals(404, h.getResponse().getStatus())
        );
    }
}
