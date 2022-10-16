package dev.abelab.smartpointer.infrastructure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Index Controller
 */
@Controller
public class IndexController {

    @RequestMapping("/**/{path:[^\\.]*}")
    public String redirect(@PathVariable String path) {
        return "forward:/";
    }

}
