package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class AccountServiceJPA implements AccountService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(Account user) {
        entityManager.persist(user);
    }

    @Override
    public Account getUser(String login) {
        try {
            return (Account) entityManager.createNamedQuery("Account.getUser")
                    .setParameter("login", login).getSingleResult();
        } catch (Exception e){
            return new Account(null, null, null);
        }
    }

    @Override
    public boolean getEmail(String email) {
        try {
            boolean st = entityManager.createNamedQuery("Account.getEmail")
                    .setParameter("email", email).getSingleResult() != null;
            return st;
        } catch (Exception e){
            return false;
        }
    }
}
