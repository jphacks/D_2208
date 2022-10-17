package dev.abelab.smartpointer.usecase

import org.springframework.beans.factory.annotation.Autowired

/**
 * CreateRoomUseCaseの単体テスト
 */
class CreateRoomUseCase_UT extends AbstractUseCase_UT {

    @Autowired
    CreateRoomUseCase sut

    def "handle: ルームを作成する"() {
        when:
        this.sut.handle()

        then:
        1 * this.roomRepository.insert(_)
    }

}
