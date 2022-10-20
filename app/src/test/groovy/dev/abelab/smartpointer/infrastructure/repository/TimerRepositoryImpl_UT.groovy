package dev.abelab.smartpointer.infrastructure.repository

import dev.abelab.smartpointer.domain.model.TimerModel
import dev.abelab.smartpointer.enums.TimerStatus
import dev.abelab.smartpointer.helper.TableHelper
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

/**
 * TimerRepositoryImplの単体テスト
 */
class TimerRepositoryImpl_UT extends AbstractRepository_UT {

    @Autowired
    TimerRepositoryImpl sut

    def "selectByRoomId: ルームIDからユーザを取得"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | ""
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | value | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60    | "2000-01-01 10:30:30"
        }
        // @formatter:on

        when:
        final result = this.sut.selectByRoomId("00000000-0000-0000-0000-000000000000")

        then:
        result.isPresent()
        result.get().roomId == "00000000-0000-0000-0000-000000000000"
        result.get().status == TimerStatus.READY
        result.get().value == 60
        result.get().finishAt.toString() == "2000-01-01T10:30:30"
    }

    def "selectByRoomId: 存在しない場合はOptional.empty()を返す"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | ""
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | value | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60    | "2000-01-01 00:00:00"
        }
        // @formatter:on

        when:
        final result = this.sut.selectByRoomId("00000000-0000-0000-0000-000000000001")

        then:
        result.isEmpty()
    }

    def "insert: ルームを作成する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | ""
        }
        // @formatter:on

        final timer = TimerModel.builder()
            .roomId("00000000-0000-0000-0000-000000000000")
            .status(TimerStatus.READY)
            .value(60)
            .finishAt(LocalDateTime.now())
            .build()

        when:
        this.sut.insert(timer)

        then:
        final createdTimer = sql.firstRow("SELECT * FROM timer")
        createdTimer.room_id == timer.roomId
        createdTimer.status == timer.status.id
        createdTimer.value == timer.value
    }

    def "upsert: ルームが存在する場合は更新する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | ""
        }
        TableHelper.insert sql, "timer", {
            room_id                                | status               | value | finish_at
            "00000000-0000-0000-0000-000000000000" | TimerStatus.READY.id | 60    | "2000-01-01 00:00:00"
        }
        // @formatter:on

        final timer = TimerModel.builder()
            .roomId("00000000-0000-0000-0000-000000000000")
            .status(TimerStatus.RUNNING)
            .value(120)
            .finishAt(LocalDateTime.now())
            .build()

        when:
        this.sut.upsert(timer)

        then:
        final upsertedTimer = sql.firstRow("SELECT * FROM timer")
        upsertedTimer.room_id == timer.roomId
        upsertedTimer.status == timer.status.id
        upsertedTimer.value == timer.value
    }

    def "upsert: ルームが存在しない場合は作成する"() {
        given:
        // @formatter:off
        TableHelper.insert sql, "room", {
            id                                     | passcode
            "00000000-0000-0000-0000-000000000000" | ""
        }
        // @formatter:on

        final timer = TimerModel.builder()
            .roomId("00000000-0000-0000-0000-000000000000")
            .status(TimerStatus.RUNNING)
            .value(120)
            .finishAt(LocalDateTime.now())
            .build()

        when:
        this.sut.upsert(timer)

        then:
        final upsertedTimer = sql.firstRow("SELECT * FROM timer")
        upsertedTimer.room_id == timer.roomId
        upsertedTimer.status == timer.status.id
        upsertedTimer.value == timer.value
    }

}
