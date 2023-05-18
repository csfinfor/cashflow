package com.cashflow.entry.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class PayBoxEntryPerDayDTO {
    private String entryDate;
    private BigDecimal payBoxTotal;
}
