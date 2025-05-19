package com.nookly.booking.reservation.room_block.repository;

import com.nookly.booking.reservation.room.model.Room;
import com.nookly.booking.reservation.room_block.model.RoomBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IRoomBlockRepository extends JpaRepository<RoomBlock, UUID> {

    Boolean existsByRoomIdAndBlockDate(UUID roomId, LocalDate blockDate);

    @Query("SELECT rb FROM RoomBlock rb WHERE rb.room.id = :roomId AND rb.blockDate BETWEEN :startDate AND :endDate")
    List<RoomBlock> findByRoomIdAndBlockDateBetween(@Param("roomId") UUID roomId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    RoomBlock findByRoomIdAndBlockDate(UUID roomId, LocalDate blockDate);

    UUID room(Room room);
}
