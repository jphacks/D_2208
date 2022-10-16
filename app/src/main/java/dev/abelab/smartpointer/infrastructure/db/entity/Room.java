package dev.abelab.smartpointer.infrastructure.db.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column room.id
     *
     * @mbg.generated
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column room.token
     *
     * @mbg.generated
     */
    private String token;
}