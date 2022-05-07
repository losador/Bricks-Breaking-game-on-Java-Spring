package sk.tuke.gamestudio.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery( name = "Account.getUser",
        query = "SELECT a FROM Account a WHERE a.login=:login")
@NamedQuery( name = "Account.getEmail",
        query = "SELECT a FROM Account a WHERE a.email=:email")
@Data
public class Account {
    @Id
    @GeneratedValue
    private int ident;
    private String login;
    private String password;
    private String email;

    public Account(){}

    public Account(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
