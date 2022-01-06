package fact.it.carddb.repository;

import fact.it.carddb.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findCardsByNameContaining(String Name);
    List<Card> findCardsByCost(int cost);
    List<Card> findCardsByTypeContaining(String Type);
    Card findCardByNameContaining(String Name);

}
