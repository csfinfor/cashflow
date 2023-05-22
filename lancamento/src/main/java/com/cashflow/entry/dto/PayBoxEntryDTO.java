package com.cashflow.entry.dto;

import com.cashflow.entry.domain.EntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayBoxEntryDTO {

    @NotNull(message = "o campo entryValue é obrigatório")
    private BigDecimal entryValue;
    private String entryDate;
    private EntryType entryType;
    private BigDecimal payBoxTotal;
}
