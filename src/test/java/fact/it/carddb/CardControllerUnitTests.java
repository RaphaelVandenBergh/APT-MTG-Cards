package fact.it.carddb;

import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.carddb.model.Card;
import fact.it.carddb.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenCard_whenGetCardByNameContaining_thenReturnJsonCard() throws Exception{
        Card card3 = new Card("card3","Type2",5);

        given(cardRepository.findCardByNameContaining("card3")).willReturn(card3);
        mockMvc.perform(get("/cards/{Name}","card3"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("card3")))
                .andExpect(jsonPath("$.cost",is(5)))
                .andExpect(jsonPath("$.type",is("Type2")));
    }

    @Test
    public void givenCard_whenGetCardsByCost_thenReturnJsonCard() throws Exception{
        Card card2 = new Card("card2","Type1",1);
        Card card4 = new Card("card4", "Type2",1);

        List<Card> cardList = new ArrayList<>();
        cardList.add(card2);
        cardList.add(card4);

        given(cardRepository.findCardsByCost(1)).willReturn(cardList);

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
    public void givenCard_whenGetCardsbyType_thenReturnJsonCard() throws Exception{
        Card card1 = new Card("card1","Type1",0);
        Card card2 = new Card("card2","Type1",1);

        List<Card> cardList = new ArrayList<>();
        cardList.add(card1);
        cardList.add(card2);

        given(cardRepository.findCardsByTypeContaining("Type1")).willReturn(cardList);

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
        Card card3 = new Card("card3","Type2",5);
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
    public void givenCard_whenPutCard_thenReturnJsonCard()throws Exception{
        Card card1 = new Card("card1","Type1",0);

        given(cardRepository.findCardByNameContaining("card1")).willReturn(card1);

        Card putcard = new Card("putcard", "puttype",5);

        mockMvc.perform(put("/cards/{Name}","card1")
                .content(mapper.writeValueAsString(putcard))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("putcard")))
                .andExpect(jsonPath("$.type", is("puttype")))
                .andExpect(jsonPath("$.cost",is(5)));


    }

    @Test
    public void givenCard_whenDeleteCard_thenStatusOk() throws Exception{
        Card deletecard = new Card("delete","dtype",99999);
        given(cardRepository.findCardByNameContaining("delete")).willReturn(deletecard);
        mockMvc.perform(delete("/cards/{Name}", "delete")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenCard_whenDeleteCard_thenStatusNotFound() throws Exception{
        given(cardRepository.findCardByNameContaining("cardo")).willReturn(null);
        mockMvc.perform(delete("/cards/{Name}", "card5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
