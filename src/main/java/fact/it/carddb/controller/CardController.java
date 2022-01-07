package fact.it.carddb.controller;

import fact.it.carddb.model.Card;
import fact.it.carddb.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class CardController {
    @Autowired
    private CardRepository cardRepository;

    @GetMapping("/cards")
    public List<Card> getCards(){
        return cardRepository.findAll();
    }

    @GetMapping("/cards/{Name}")
    public Card findCardByNameContaining(@PathVariable String Name){
        return cardRepository.findCardByNameContaining(Name);
    }
    @GetMapping("/cards/cost/{Cost}")
    public List<Card> findCardsByCost(@PathVariable int Cost){
        return cardRepository.findCardsByCost(Cost);
    }

    @GetMapping("/cards/type/{Type}")
    public List<Card> findCardsByTypeContaining(@PathVariable String Type){
        return cardRepository.findCardsByTypeContaining(Type);
    }

    @DeleteMapping("/cards/{Name}")
    public ResponseEntity deleteCard(@PathVariable String Name){
        Card card = cardRepository.findCardByNameContaining(Name);
        if (card != null){
            cardRepository.delete(card);
            return  ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/cards/{Name}")
    public Card updateCard(@RequestBody Card updatedCard, @PathVariable String Name){
        Card retrievedCard = cardRepository.findCardByNameContaining(Name);

        retrievedCard.setName(updatedCard.getName());
        retrievedCard.setType(updatedCard.getType());
        retrievedCard.setCost(updatedCard.getCost());

        cardRepository.save(retrievedCard);
        return retrievedCard;

    }

    @PostMapping("/cards")
    public Card PostCard(@RequestBody Card postCard){
        cardRepository.save(postCard);
        return postCard;
    }


    @PostConstruct
    public void filldb(){
        if (cardRepository.count()==0){
            cardRepository.save(new Card("Ornitopther","Artifact Creature — Thopter",0));
            cardRepository.save(new Card("Burrenton Forge-Tender","Creature — Kithkin Wizard",1));
            cardRepository.save(new Card("Esper Sentinel","Artifact Creature — Human Soldier",1));
            cardRepository.save(new Card("Puresteel Paladin","Creature — Human Knight",2));
            cardRepository.save(new Card("Sanctifier en-Vec","Creature — Human Cleric",2));
            cardRepository.save(new Card("Stoneforge Mystic","Creature — Kor Artificer",2));
            cardRepository.save(new Card("Steelshaper's Gift","Sorcery",1));
            cardRepository.save(new Card("Colossus Hammer","Artifact — Equipment",1));
            cardRepository.save(new Card("Shadowspear","Legendary Artifact — Equipment",1));
            cardRepository.save(new Card("Kaldra Compleat","Legendary Artifact — Equipment",7));
            cardRepository.save(new Card("Springleaf Drum","Artifact",1));
            cardRepository.save(new Card("Cranial Plating","Artifact — Equipment",2));
            cardRepository.save(new Card("Nettlecyst","Artifact — Equipment",2));
            cardRepository.save(new Card("Sword of Fire and Ice","Artifact — Equipment",3));
            cardRepository.save(new Card("Seachrome Coast","Land",0));
            cardRepository.save(new Card("Sigarda's Aid","Enchantment",1));
            cardRepository.save(new Card("Flooded Strand","Land",0));
            cardRepository.save(new Card("Hallowed Fountain","Land — Plains Island",0));
            cardRepository.save(new Card("Inkmoth Nexus","Land",0));
        }
    }
}
