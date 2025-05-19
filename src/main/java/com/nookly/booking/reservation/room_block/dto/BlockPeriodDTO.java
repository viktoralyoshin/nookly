package com.nookly.booking.reservation.room_block.dto;

import com.nookly.booking.reservation.room_block.model.BlockReason;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class BlockPeriodDTO {
    @NotNull(message = "Start Date can't be null")
    private LocalDate startDate;
    @NotNull(message = "End Date can't be null")
    private LocalDate endDate;
    @NotNull(message = "Block Reason can't be null")
    private BlockReason blockReason;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BlockReason getBlockReason() {
        return blockReason;
    }

    public void setBlockReason(BlockReason blockReason) {
        this.blockReason = blockReason;
    }
}
