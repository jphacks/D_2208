ALTER TABLE `timer`
    DROP COLUMN `value`;
ALTER TABLE `timer`
    ADD `input_time` INT NOT NULL;
ALTER TABLE `timer`
    ADD `remaining_time_at_paused` INT NULL;