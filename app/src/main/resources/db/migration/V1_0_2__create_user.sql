CREATE TABLE IF NOT EXISTS `user`
(
    `id`         CHAR(36)     NOT NULL DEFAULT (UUID()),
    `room_id`    CHAR(36)     NOT NULL,
    `name`       VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    INDEX `fk_user_room_id_idx` (`room_id` ASC) VISIBLE,
    UNIQUE INDEX `room_id_and_name_UNIQUE` (`room_id` ASC, `name` ASC) VISIBLE,
    CONSTRAINT `fk_user_room_id`
        FOREIGN KEY (`room_id`)
            REFERENCES `room` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB