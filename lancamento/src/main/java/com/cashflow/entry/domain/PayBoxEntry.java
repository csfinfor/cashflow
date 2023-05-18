package com.cashflow.entry.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pay_box_entry")
public class PayBoxEntry{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="entry_value")
    private BigDecimal entryValue;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="entry_date")
    private LocalDateTime entryDate;

    @Column(name="pay_box_total")
    private BigDecimal payBoxTotal;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="entry_type")
    private EntryType entryType;

}
