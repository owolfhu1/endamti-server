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

    @PutMapping("/put-test")
    public String putTest(@RequestBody TestObject object) {
        System.out.println(object.getType());
        return "success";
    }

}

class TestObject {
    private String type;
    private int value;
    int getValue() { return value; }
    String getType() { return type; }
}
