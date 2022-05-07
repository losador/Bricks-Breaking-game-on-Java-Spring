package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Account;
import sk.tuke.gamestudio.service.AccountService;

@RestController
@RequestMapping("/api/user")
public class AccountServiceRest {

    @Autowired
    private AccountService userService;

    @PostMapping
    public void addUser(@RequestBody Account user){
        userService.addUser(user);
    }

    @GetMapping("/{login}")
    public Account getUser(@PathVariable String login){
        return userService.getUser(login);
    }

    @GetMapping("/email/{email}")
    public boolean getEmail(@PathVariable String email) {return userService.getEmail(email);}
}
