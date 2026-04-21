package com.mr486.serviceb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-b/hello")
public class HelloController {

    @GetMapping
    public ApiResponse<String> hello() {
        return new ApiResponse<>(true, "Hello from service-b !", "Hello from service-b !");
    }
}
