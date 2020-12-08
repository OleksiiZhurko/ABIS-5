package com.gen.controller;

import com.gen.dto.GivenBag;
import com.gen.dto.RandomBag;
import com.gen.dto.BagDecision;
import com.gen.model.BagSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/")
public class DefaultController {

    private final BagSolver bagSolver;

    @Autowired
    public DefaultController(BagSolver bagSolver) {
        this.bagSolver = bagSolver;
    }

    @GetMapping(value = "")
    public String produceMainPage() {
        return "index";
    }

    @PostMapping(value = "/given_bag")
    public @ResponseBody BagDecision
    solveGivenBag(@RequestBody GivenBag givenBag) {
        return bagSolver.solveBag(givenBag);
    }

    @PostMapping(value = "/random_bag")
    public @ResponseBody BagDecision
    solveRandomBag(@RequestBody RandomBag randomBag) {
        return bagSolver.solveBag(randomBag);
    }

}