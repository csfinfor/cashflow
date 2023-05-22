package com.cashflow.entry.repository;

import com.cashflow.entry.domain.PayBoxEntry;
import com.cashflow.entry.dto.PayBoxEntryPerDayDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PayBoxEntryRepository extends JpaRepository<PayBoxEntry, Long> {
    @Query
    Optional<PayBoxEntry> findTopByOrderByIdDesc();

    @Query
    Page<PayBoxEntry> findAllByOrderByEntryDateAsc(Pageable pageable);

    @Query(value = "SELECT " +
            "new com.cashflow.entry.dto.PayBoxEntryPerDayDTO(to_char(pbe.entryDate, 'DD/MM/YYYY'),sum(pbe.entryValue)) FROM PayBoxEntry pbe " +
            " GROUP BY to_char(pbe.entryDate, 'DD/MM/YYYY')" )
    List<PayBoxEntryPerDayDTO> findPerDay();
    @Query
    Optional<PayBoxEntry> findTopByOrderByEntryDateDesc();
}
