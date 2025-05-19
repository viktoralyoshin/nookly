package com.nookly.booking.reservation.room_block.dto;

import com.nookly.booking.reservation.room_block.model.BlockReason;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class BlockDayDTO {
    @NotNull(message = "Date can't be null")
    private LocalDate date;
    @NotNull(message = "Block Reason can't be null")
    private BlockReason blockReason;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BlockReason getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(BlockReason blockReason) {
        this.blockReason = blockReason;
    }
}
