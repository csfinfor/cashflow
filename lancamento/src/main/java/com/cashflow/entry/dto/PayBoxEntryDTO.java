package com.cashflow.entry.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PayBoxEntryDTO {

    @NotNull(message = "o campo entryValue é obrigatório")
    private BigDecimal entryValue;
    private LocalDateTime entryDate;

    @NotNull(message = "o campo entryType é obrigatório")
    private Integer entryType;
    private BigDecimal payBoxTotal;
}
