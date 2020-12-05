package com.server.endamti.controller;

import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class TestController {


    @GetMapping("/get-test")
    public String getTest() {
        return "test string";
    }

    @PostMapping("/post-test")
    public String postTest(@RequestBody TestObject object) {
        return object.getType() + ": " + object.getValue();
    }

}

class TestObject {
    private String type;
    private int value;

    public int getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
