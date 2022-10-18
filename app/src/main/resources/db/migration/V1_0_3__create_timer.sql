CREATE TABLE IF NOT EXISTS `timer`
(
    `room_id`    CHAR(36)     NOT NULL,
    `status`     INT UNSIGNED NOT NULL,
    `value`      INT UNSIGNED NOT NULL DEFAULT 0,
    `finish_at`  DATETIME     NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`room_id`),
    CONSTRAINT `fk_timer_room_id`
        FOREIGN KEY (`room_id`)
            REFERENCES `room` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB