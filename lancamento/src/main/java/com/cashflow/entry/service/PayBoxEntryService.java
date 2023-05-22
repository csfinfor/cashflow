package com.cashflow.entry.service;

import com.cashflow.entry.domain.PayBoxEntry;
import com.cashflow.entry.dto.PayBoxEntryDTO;
import com.cashflow.entry.dto.PayBoxEntryPerDayDTO;
import com.cashflow.entry.repository.PayBoxEntryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PayBoxEntryService {

    @Autowired
    private PayBoxEntryRepository repository;

    public Page<PayBoxEntryDTO>     findAll(Pageable pageable) {

        Page<PayBoxEntry> listRepo = repository.findAllByOrderByEntryDateAsc(pageable);
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        Page<PayBoxEntryDTO> listReturn = mapEntityPageIntoDtoPage(listRepo, PayBoxEntryDTO.class);
        return listReturn;
    }

    public List<PayBoxEntryPerDayDTO> findAllPerDay() {
        return repository.findPerDay();
    }

    public PayBoxEntryPerDayDTO findBalanceToDay(){
        Optional<PayBoxEntry> entryLast = repository.findTopByOrderByEntryDateDesc();
        if(entryLast.isPresent()){
            PayBoxEntry entry = entryLast.get();
            return new PayBoxEntryPerDayDTO(DateTimeFormatter.ofPattern("dd/MM/yyyy").format(entry.getEntryDate()), entry.getPayBoxTotal());
        }else{
            return new PayBoxEntryPerDayDTO(new SimpleDateFormat("dd/MM/yyyy").format(new Date()), BigDecimal.ZERO);
        }
    }

    public void salvarLancamentoCredito(PayBoxEntry entry) {
        Optional<PayBoxEntry> entryLast = repository.findTopByOrderByIdDesc();
        if(entryLast.isPresent()){
                entry.setPayBoxTotal(entryLast.get().getPayBoxTotal().add(entry.getEntryValue()));
                entry.setEntryValue(entry.getEntryValue().multiply(BigDecimal.ONE));
        }else{
            entry.setPayBoxTotal(entry.getEntryValue());
        }
        if(entry.getEntryDate() == null){
            entry.setEntryDate(LocalDateTime.now());
        }
        repository.save(entry);
    }

    public void salvarLancamentoDebito(PayBoxEntry entry) {
        Optional<PayBoxEntry> entryLast = repository.findTopByOrderByIdDesc();
        if(entryLast.isPresent()){
            entry.setPayBoxTotal(entryLast.get().getPayBoxTotal().subtract(entry.getEntryValue()));
            entry.setEntryValue(entry.getEntryValue().multiply(BigDecimal.ONE.negate()));
        }else{
            entry.setPayBoxTotal(entry.getEntryValue());
        }
        if(entry.getEntryDate() == null){
            entry.setEntryDate(LocalDateTime.now());
        }
        repository.save(entry);
    }

    public <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        return entities.map(objectEntity -> mapper.convertValue(objectEntity, dtoClass));
    }
}
