package com.example.demo.domain.requestBinding.controller;

import com.example.demo.domain.exception.Response;
import com.example.demo.domain.requestBinding.model.ClassTest;
import com.example.demo.domain.requestBinding.model.reqeust.AllowedName;
import com.example.demo.domain.requestBinding.model.reqeust.DefaultValue;
import com.example.demo.domain.requestBinding.model.reqeust.PostBinding;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@Tag(name ="APi Request Binding 테스트")
@RestController
@RequestMapping("/api/v1/binding")
@RequiredArgsConstructor
public class RequestBinding {

    @PostMapping("/test")
    public void PostRequestBinding(@RequestBody @Valid PostBinding req) {
        System.out.println(req);
    }

    @PostMapping("/empty-to-desc")
    public void MakeDefaultValue(@RequestBody @Valid DefaultValue req) {
        System.out.println(req);
    }


    @GetMapping("/test")
    public Response<ClassTest> QueryRequestBinding(
            @RequestParam String key,
            @RequestParam(required = false) String name
    ) {
        return Response.success(new ClassTest(name, key));
    }

    @GetMapping("/path/{name}/{age}")
    public void UriRequestBinding(
            @PathVariable AllowedName name,
            @PathVariable int age
    ) {
        System.out.println(name.name() + age);
    }

    
}