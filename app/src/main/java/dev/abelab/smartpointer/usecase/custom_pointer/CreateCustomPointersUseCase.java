package dev.abelab.smartpointer.usecase.custom_pointer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import dev.abelab.smartpointer.domain.model.CustomPointerModel;
import dev.abelab.smartpointer.domain.model.FileModel;
import dev.abelab.smartpointer.domain.repository.CustomPointerRepository;
import dev.abelab.smartpointer.domain.repository.RoomRepository;
import dev.abelab.smartpointer.exception.ConflictException;
import dev.abelab.smartpointer.exception.ErrorCode;
import dev.abelab.smartpointer.exception.NotFoundException;
import dev.abelab.smartpointer.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;

/**
 * カスタムポインター作成ユースケース
 */
@RequiredArgsConstructor
@Component
public class CreateCustomPointersUseCase {

    private final RoomRepository roomRepository;

    private final CustomPointerRepository customPointerRepository;

    private final FileStorageUtil fileStorageUtil;

    /**
     * Handle UseCase
     *
     * @param roomId ルームID
     * @param id カスタムポインターID
     * @param label ラベル
     * @param content 画像コンテンツ(Base64)
     */
    @Transactional
    public void handle(final String roomId, final String id, final String label, final String content) {
        // ルームの存在チェック
        if (!this.roomRepository.existsById(roomId)) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_ROOM);
        }

        // カスタムポインターの存在チェック
        if (this.customPointerRepository.existsByIdAndRoomId(id, roomId)) {
            throw new ConflictException(ErrorCode.CUSTOM_POINTER_IS_ALREADY_EXISTS);
        }

        // 画像をアップロード
        final var file = new FileModel(id, roomId, content);
        final var url = this.fileStorageUtil.upload(file);

        // カスタムポインターを作成
        final var customPointer = CustomPointerModel.builder() //
            .id(id) //
            .roomId(roomId) //
            .label(label) //
            .url(url) //
            .build();
        this.customPointerRepository.insert(customPointer);
    }

}
