package fact.it.carddb;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.carddb.model.Card;
import fact.it.carddb.repository.CardRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepository cardRepository;

    private Card card1 = new Card("card1","Type1",0);
    private Card card2 = new Card("card2","Type1",1);
    private Card card3 = new Card("card3","Type2",5);
    private Card card4 = new Card("card4", "Type2",1);

    @BeforeEach
    public void beforeAllTests(){
        cardRepository.deleteAll();
        cardRepository.save(card1);
        cardRepository.save(card2);
        cardRepository.save(card3);
        cardRepository.save(card4);
    }

    @AfterEach
    public void afterAllTests(){
        cardRepository.deleteAll();
    }

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenCard_whenGetCardByNameContaining_thenReturnJsonCard() throws Exception{
        mockMvc.perform(get("/cards/{Name}","card1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("card1")))
                .andExpect(jsonPath("$.cost",is(0)))
                .andExpect(jsonPath("$.type",is("Type1")));
    }

    @Test
    public void givenCard_whenGetCardsByCost_thenReturnJsonCard() throws Exception{
        mockMvc.perform(get("/cards/cost/{Cost}",1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("card2")))
                .andExpect(jsonPath("$[0].type", is("Type1")))
                .andExpect(jsonPath("$[0].cost",is(1)))
                .andExpect(jsonPath("$[1].name", is("card4")))
                .andExpect(jsonPath("$[1].type", is("Type2")))
                .andExpect(jsonPath("$[1].cost",is(1)));
    }

    @Test
    public void  GivenCard_whenGetCardsByType_thenReturnJsonCard() throws Exception{
        mockMvc.perform(get("/cards/type/{Type}", "Type1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("card1")))
                .andExpect(jsonPath("$[0].type", is("Type1")))
                .andExpect(jsonPath("$[0].cost",is(0)))
                .andExpect(jsonPath("$[1].name", is("card2")))
                .andExpect(jsonPath("$[1].type", is("Type1")))
                .andExpect(jsonPath("$[1].cost",is(1)));
    }

    @Test
    public void whenPostCard_thenReturnJsonCard() throws Exception{
        mockMvc.perform(post("/cards")
                .content(mapper.writeValueAsString(card3))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("card3")))
                .andExpect(jsonPath("$.type", is("Type2")))
                .andExpect(jsonPath("$.cost",is(5)));
    }

    @Test
    public void givenCard_WhenDeleteCard_thenStatusOk() throws Exception{
        mockMvc.perform(delete("/cards/{Name}", "card4")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenCard_WhenDeleteCard_thenStatusNotFound() throws Exception {
        mockMvc.perform(delete("/cards/{Name}", "card5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenCard_WhenPutCard_thenJsonCard() throws Exception{
        Card putcard = new Card("putcard","puttype",5);
        mockMvc.perform(put("/cards/{Name}","card1")
                .content(mapper.writeValueAsString(putcard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("putcard")))
                .andExpect(jsonPath("$.type", is("puttype")))
                .andExpect(jsonPath("$.cost",is(5)));

    }
}
