package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * ヘルスチェックコントローラ
 */
@Tag(name = "Health Check", description = "ヘルスチェック")
@RestController
@RequestMapping(path = "/api/health", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class HealthCheckRestController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void health() {}

}
