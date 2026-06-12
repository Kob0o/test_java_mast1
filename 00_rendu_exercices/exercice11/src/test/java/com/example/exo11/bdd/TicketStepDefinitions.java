package com.example.exo11.bdd;

import com.example.exo11.repository.TicketRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TicketStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions lastResult;
    private Long createdTicketId;

    @Given("aucun ticket n existe dans l API")
    public void noTicketExists() {
        ticketRepository.deleteAll();
        createdTicketId = null;
    }

    @When("je cree un ticket avec le titre {string} et la priorite {string}")
    public void createTicket(String title, String priority) throws Exception {
        String body = "{\"title\":\"" + title + "\",\"priority\":\"" + priority + "\"}";
        lastResult = mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        var response = lastResult.andReturn().getResponse();
        if (!response.getContentAsString().isBlank() && response.getStatus() == 201) {
            createdTicketId = objectMapper.readTree(response.getContentAsString()).get("id").asLong();
        }
    }

    @When("je demande le ticket cree")
    public void getCreatedTicket() throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + createdTicketId));
    }

    @When("je demande le ticket avec l identifiant {long}")
    public void getTicketById(Long id) throws Exception {
        lastResult = mockMvc.perform(get("/api/tickets/" + id));
    }

    @When("je mets a jour le statut du ticket cree vers {string}")
    public void updateCreatedTicketStatus(String status) throws Exception {
        String body = "{\"status\":\"" + status + "\"}";
        lastResult = mockMvc.perform(patch("/api/tickets/" + createdTicketId + "/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Then("la reponse HTTP doit etre {int}")
    public void responseStatusShouldBe(int expectedStatus) throws Exception {
        lastResult.andExpect(status().is(expectedStatus));
    }

    @Then("la reponse contient le titre {string}")
    public void responseShouldContainTitle(String expectedTitle) throws Exception {
        lastResult.andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Then("la reponse contient le statut {string}")
    public void responseShouldContainStatus(String expectedStatus) throws Exception {
        lastResult.andExpect(jsonPath("$.status").value(expectedStatus));
    }

    @Then("la reponse contient un message d erreur")
    public void responseShouldContainErrorMessage() throws Exception {
        lastResult.andExpect(jsonPath("$.message").exists());
    }
}
