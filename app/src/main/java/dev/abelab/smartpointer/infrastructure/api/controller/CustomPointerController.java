package dev.abelab.smartpointer.infrastructure.api.controller;

import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import dev.abelab.smartpointer.infrastructure.api.type.CustomPointer;
import dev.abelab.smartpointer.infrastructure.api.type.CustomPointers;
import dev.abelab.smartpointer.usecase.custom_pointer.GetCustomPointersUseCase;
import lombok.RequiredArgsConstructor;

/**
 * カスタムポインターコントローラ
 */
@Controller
@RequiredArgsConstructor
public class CustomPointerController {

    private final GetCustomPointersUseCase getCustomPointersUseCase;

    /**
     * カスタムポインターリスト取得API
     *
     * @param roomId ルームID
     * @return カスタムポインターリスト
     */
    @QueryMapping
    public CustomPointers getCustomPointers( //
        @Argument final String roomId //
    ) {
        final var customPointers = this.getCustomPointersUseCase.handle(roomId).stream() //
            .map(CustomPointer::new) //
            .collect(Collectors.toList());
        return new CustomPointers(customPointers);
    }

}
