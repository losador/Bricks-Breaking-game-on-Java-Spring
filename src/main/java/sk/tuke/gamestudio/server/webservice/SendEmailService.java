package sk.tuke.gamestudio.server.webservice;

import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.server.Sender;

@RestController
@RequestMapping("/api/send")
public class SendEmailService {

    private Sender sender = new Sender("bricksbreakingkp@gmail.com", "Brickstuke123");

    @GetMapping("/{email}")
    public String sendEmail(@PathVariable String email){
        int code = (int)(10000000 + Math.random() * 99999999);
        try {
            String message = "Your verification code to continue registration at BricksBreaking game is: " + code;
            sender.send("Verification code", message, "bricksbreakingkp@gmail.com", email);
            return String.valueOf(code);
        } catch (Exception e){
            return "error";
        }
    }

}
