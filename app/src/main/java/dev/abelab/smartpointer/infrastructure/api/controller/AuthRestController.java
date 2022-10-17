package dev.abelab.smartpointer.infrastructure.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import dev.abelab.smartpointer.infrastructure.api.request.LoginRequest;
import dev.abelab.smartpointer.infrastructure.api.validation.RequestValidated;
import dev.abelab.smartpointer.usecase.LoginUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 認証コントローラ
 */
@Tag(name = "Auth", description = "認証")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class AuthRestController {

    private final LoginUseCase loginUseCase;

    /**
     * ログインAPI
     *
     * @param requestBody ログインリクエスト
     */
    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public void login( //
        @RequestValidated @RequestBody final LoginRequest requestBody, //
        final HttpServletResponse response //
    ) {
        final var credentials = this.loginUseCase.handle(requestBody);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + credentials);
    }

}
