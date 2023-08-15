package project.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MapController {

    @GetMapping("/api/map")
    public String proRequest() {
        System.out.println("지도 연결");

        return "map";
    }

}
