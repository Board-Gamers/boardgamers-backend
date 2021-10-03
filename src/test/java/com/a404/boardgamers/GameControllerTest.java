package com.a404.boardgamers;

import com.a404.boardgamers.Game.Domain.Repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class GameControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    GameRepository gameRepository;

    @Test
    public void 게임_전체조회() throws Exception {
        mvc.perform(get("/game/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void nameKor로_조회() throws Exception {
        mvc.perform(get("/game/list")
                .param("nameKor", "모험")
                .param("minAge", "12"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void 최소인원_최대시간으로_조회() throws Exception {
        mvc.perform(get("/game/list").param("minplayers", "4").param("maxplaytime", "30"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}
