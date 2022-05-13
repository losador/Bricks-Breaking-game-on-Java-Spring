package sk.tuke.gamestudio.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery( name = "Rating.getAverageRating",
        query = "SELECT avg(r.rate) FROM Rating r WHERE r.game=:game")
@NamedQuery( name = "Rating.getRating",
        query = "SELECT r.rate FROM Rating r WHERE r.game=:game AND r.player=:player")
@NamedQuery( name = "Rating.updateRating",
        query = "UPDATE Rating SET rate=:rate WHERE player=:player")
@NamedQuery( name = "Rating.resetRating",
        query = "DELETE FROM Rating")
@Data
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String player;
    private int rate;
    private Date ratedOn;

    public Rating() {}

    public Rating(String game, String player, int rate, Date ratedOn) {
        this.game = game;
        this.player = player;
        this.rate = rate;
        this.ratedOn = ratedOn;
    }

    @Override
    public String toString() {
        return  "Game: " + game +
                ", Player: " + player +
                ", Rating: " + rate +
                ", Rated on: " + ratedOn +
                '}';
    }
}
