CREATE TABLE IF NOT EXISTS `custom_pointer`
(
    `id`      CHAR(36)     NOT NULL,
    `room_id` CHAR(36)     NOT NULL,
    `label`   VARCHAR(255) NOT NULL,
    `url`     VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`, `room_id`),
    INDEX `fk_custom_pointer_room_id_room_id_idx` (`room_id` ASC) VISIBLE,
    CONSTRAINT `fk_custom_pointer_room_id_room_id`
        FOREIGN KEY (`room_id`)
            REFERENCES `room` (`id`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB