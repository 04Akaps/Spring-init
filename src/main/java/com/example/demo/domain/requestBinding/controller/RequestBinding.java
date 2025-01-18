package com.example.demo.domain.requestBinding.controller;

import com.example.demo.custom.annotation.PageAnnotation;
import com.example.demo.domain.exception.Response;
import com.example.demo.domain.requestBinding.model.ClassTest;
import com.example.demo.domain.requestBinding.model.reqeust.AllowedName;
import com.example.demo.domain.requestBinding.model.reqeust.DefaultValue;
import com.example.demo.domain.requestBinding.model.reqeust.PostBinding;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/page-test")
    public void PagingAnnotation(
            @ParameterObject @PageableDefault(
                    sort = "time", direction = Sort.Direction.DESC,
                    size = 10,
                    page = 0
            ) Pageable pageable
    ) {

        System.out.println(pageable.getPageNumber());
        System.out.println(pageable.getPageSize());
        System.out.println("Sort: " + pageable.getSort());

        Sort sort = pageable.getSort();
        sort.forEach(order -> {
            String property = order.getProperty();
            Sort.Direction direction = order.getDirection();
            System.out.println("Field: " + property);
            System.out.println("Direction: " + direction);
        });

    }

    @GetMapping("/page-annotation")
    public void pagingAnnotation(
            @PageAnnotation() String order
    ) {
        System.out.println("orderorderorderorder" + order);
    }

    
}