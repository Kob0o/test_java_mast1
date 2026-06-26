package com.example.exercice13.bdd;

import com.example.exercice13.repository.AccountRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountStepDefinitions {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    private ResultActions lastResult;

    @Given("no account exists in the API")
    public void noAccountExists() {
        accountRepository.deleteAll();
    }

    @Given("an account {string} exists for holder {string}")
    public void accountExists(String number, String holder) throws Exception {
        accountRepository.deleteAll();
        createAccount(number, holder);
    }

    @Given("an account {string} exists for holder {string} with balance {int}")
    public void accountExistsWithBalance(String number, String holder, int balance) throws Exception {
        accountRepository.deleteAll();
        createAccount(number, holder);
        if (balance > 0) {
            deposit(number, balance);
        }
    }

    @Given("another account {string} exists for holder {string} with balance {int}")
    public void anotherAccountExistsWithBalance(String number, String holder, int balance) throws Exception {
        createAccount(number, holder);
        if (balance > 0) {
            deposit(number, balance);
        }
    }

    @When("I create an account with number {string} and holder {string}")
    public void createAccountStep(String number, String holder) throws Exception {
        lastResult = createAccount(number, holder);
    }

    @When("I deposit {int} on account {string}")
    public void depositStep(int amount, String number) throws Exception {
        lastResult = deposit(number, amount);
    }

    @When("I withdraw {int} from account {string}")
    public void withdrawStep(int amount, String number) throws Exception {
        String body = "{\"amount\":" + amount + "}";
        lastResult = mockMvc.perform(post("/api/accounts/" + number + "/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @When("I transfer {int} from {string} to {string}")
    public void transferStep(int amount, String from, String to) throws Exception {
        String body = """
                {
                  "fromNumber": "%s",
                  "toNumber": "%s",
                  "amount": %d
                }
                """.formatted(from, to, amount);

        lastResult = mockMvc.perform(post("/api/accounts/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    @Then("the HTTP response status should be {int}")
    public void responseStatusShouldBe(int status) throws Exception {
        lastResult.andExpect(status().is(status));
    }

    @Then("the response balance should be {int}")
    public void responseBalanceShouldBe(int balance) throws Exception {
        lastResult.andExpect(jsonPath("$.balance").value(balance));
    }

    @Then("account {string} has balance {int}")
    public void accountHasBalance(String number, int balance) throws Exception {
        mockMvc.perform(get("/api/accounts/" + number))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(balance));
    }

    private ResultActions createAccount(String number, String holder) throws Exception {
        String body = """
                {
                  "number": "%s",
                  "holder": "%s"
                }
                """.formatted(number, holder);

        return mockMvc.perform(post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }

    private ResultActions deposit(String number, int amount) throws Exception {
        String body = "{\"amount\":" + amount + "}";
        return mockMvc.perform(post("/api/accounts/" + number + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));
    }
}
