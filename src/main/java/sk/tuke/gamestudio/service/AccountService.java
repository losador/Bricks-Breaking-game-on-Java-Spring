package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Account;

public interface AccountService {
    void addUser(Account user);
    Account getUser(String login);
    boolean getEmail(String email);
//    boolean getName(String name);
}
