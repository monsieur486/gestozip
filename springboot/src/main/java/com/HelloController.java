package com.mr486.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {

    @GetMapping
    public ApiResponse<String> hello() {
        return new ApiResponse<>(true, "Hello from hello !", "Hello from hello !");
    }
}
