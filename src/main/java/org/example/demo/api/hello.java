package org.example.demo.api;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class hello {
    @GetMapping("/helloworld")
    public String hello() {
        return "helloworld";
    }
    @GetMapping("/hellohp")
    public String hellohp() {
        return "hellohp";
    }
}
