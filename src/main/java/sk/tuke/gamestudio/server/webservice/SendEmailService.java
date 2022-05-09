package sk.tuke.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.server.Sender;

@RestController
@RequestMapping("/api/send")
public class SendEmailService {

    private Sender sender = new Sender("bricksbreakingkp@gmail.com", "Brickstuke123");

    @GetMapping("/{email}")
    public String sendEmail(@PathVariable String email){
        int code = (int)(1000 + Math.random() * 10000);
        try {
            sender.send("Verification code", String.valueOf(code), "bricksbreakingkp@gmail.com", email);
            return String.valueOf(code);
        } catch (Exception e){
            return "error";
        }
    }

}
