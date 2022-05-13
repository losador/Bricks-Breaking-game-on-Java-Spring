package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public void setRating(Rating rating) throws RatingException {
        if(getRating(rating.getGame(), rating.getPlayer()) != 0){
            entityManager.createNamedQuery("Rating.deleteByName")
                    .setParameter("player", rating.getPlayer()).setParameter("game", rating.getGame()).executeUpdate();
        }
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Double d = (Double) entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game", game).getSingleResult();
        return d.intValue();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            return (int) entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game).setParameter("player", player)
                    .getSingleResult();
        } catch (Exception e){
            return 0;
        }
    }

    @Override
    public void reset() {
        entityManager.createNamedQuery("Rating.resetRating").executeUpdate();
    }

}