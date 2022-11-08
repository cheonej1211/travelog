package com.travelog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/travelog")
public class TravelogController {
	
	@GetMapping("test")
    public String textBasic(Model model) {
        model.addAttribute("data", "이제부터 타임리프로 사용하세요!");
        return "travelog/travelog";
    }
	
}
