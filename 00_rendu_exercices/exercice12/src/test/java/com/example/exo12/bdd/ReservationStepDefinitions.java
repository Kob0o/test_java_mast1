package com.example.exo12.bdd;

import com.example.exo12.repository.ReservationRepository;
import com.example.exo12.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long roomId;

    @Given("no room exists in the API")
    public void noRoomExists() {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();
        roomId = null;
    }

    @Given("a room {string} exists with capacity {int}")
    public void aRoomExists(String name, int capacity) throws Exception {
        reservationRepository.deleteAll();
        roomRepository.deleteAll();

        String body = "{\"name\":\"" + name + "\",\"capacity\":" + capacity + "}";
        var response = mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn().getResponse();

        roomId = objectMapper.readTree(response.getContentAsString()).get("id").asLong();
    }

    @Given("the room is already booked from {string} to {string}")
    public void roomAlreadyBooked(String start, String end) throws Exception {
        String body = """
                {
                  "roomId": %d,
                  "reservedBy": "Already taken",
                  "startTime": "%s",
                  "endTime": "%s"
                }
                """.formatted(roomId, start, end);

        mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("I book the room for {string} from {string} to {string}")
    public void bookRoom(String person, String start, String end) throws Exception {
        String body = """
                {
                  "roomId": %d,
                  "reservedBy": "%s",
                  "startTime": "%s",
                  "endTime": "%s"
                }
                """.formatted(roomId, person, start, end);

        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("I book room {long} for {string} from {string} to {string}")
    public void bookUnknownRoom(Long id, String person, String start, String end) throws Exception {
        String body = """
                {
                  "roomId": %d,
                  "reservedBy": "%s",
                  "startTime": "%s",
                  "endTime": "%s"
                }
                """.formatted(id, person, start, end);

        lastResult = mockMvc.perform(post("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Then("the HTTP response status should be {int}")
    public void responseStatusShouldBe(int status) throws Exception {
        lastResult.andExpect(status().is(status));
    }

    @Then("the response contains the name {string}")
    public void responseContainsName(String name) throws Exception {
        lastResult.andExpect(jsonPath("$.reservedBy").value(name));
    }
}
