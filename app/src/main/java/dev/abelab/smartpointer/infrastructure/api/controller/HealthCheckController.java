package dev.abelab.smartpointer.infrastructure.api.controller;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

/**
 * ルームコントローラ
 */
@Controller
@RequiredArgsConstructor
public class HealthCheckController {

    /**
     * ヘルスチェックAPI
     * 
     * @return チェック結果
     */
    @QueryMapping
    public Boolean health() {
        return true;
    }

}
