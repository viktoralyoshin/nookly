package com.nookly.booking.reservation.room_block.model;

import com.nookly.booking.reservation.room.model.Room;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "room_blocks")
public class RoomBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "block_date", nullable = false)
    private LocalDate blockDate;

    @Column(name = "block_reason")
    @Enumerated(EnumType.STRING)
    private BlockReason blockReason;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public RoomBlock() {}

    public RoomBlock(UUID id, Room room, LocalDate blockDate, BlockReason blockReason, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.room = room;
        this.blockDate = blockDate;
        this.blockReason = blockReason;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getBlockDate() {
        return blockDate;
    }

    public void setBlockDate(LocalDate blockDate) {
        this.blockDate = blockDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BlockReason getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(BlockReason blockReason) {
        this.blockReason = blockReason;
    }
}
