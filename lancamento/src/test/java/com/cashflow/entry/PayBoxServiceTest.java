package com.cashflow.entry;

import com.cashflow.entry.domain.EntryType;
import com.cashflow.entry.domain.PayBoxEntry;
import com.cashflow.entry.dto.PayBoxEntryDTO;
import com.cashflow.entry.dto.PayBoxEntryPerDayDTO;
import com.cashflow.entry.repository.PayBoxEntryRepository;
import com.cashflow.entry.service.PayBoxEntryService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PayBoxServiceTest {

    @InjectMocks
    PayBoxEntryService payBoxEntryService;

    @Mock
    private PayBoxEntryRepository payBoxEntryRepository;

    @Test
    void findAllTest(){
        Pageable pageable = PageRequest.of(0, 20);
        when(payBoxEntryRepository.findAllByOrderByEntryDateAsc(pageable)).thenReturn(generatePageEntitys());
        Page<PayBoxEntryDTO> retorno = payBoxEntryService.findAll(pageable);
        Assertions.assertEquals(2, retorno.getTotalElements());
    }

    @Test
    void findBalanceToDayTest(){
        List<PayBoxEntry> entrys = generateList();
        when(payBoxEntryRepository.findTopByOrderByEntryDateDesc()).thenReturn(Optional.of(entrys.get(0)));
        PayBoxEntryPerDayDTO retorno = payBoxEntryService.findBalanceToDay();
        Assertions.assertNotNull(retorno);
    }
    @Test
    void findBalanceToDayNoRecords(){
        when(payBoxEntryRepository.findTopByOrderByEntryDateDesc()).thenReturn(Optional.empty());
        PayBoxEntryPerDayDTO retorno = payBoxEntryService.findBalanceToDay();
        Assertions.assertEquals(BigDecimal.ZERO, retorno.getPayBoxTotal());
    }
    @Test
    void findAllPerDayTest(){
        when(payBoxEntryRepository.findPerDay()).thenReturn(generatePerDayList());
        List<PayBoxEntryPerDayDTO> retorno = payBoxEntryService.findAllPerDay();
        Assertions.assertEquals(2, retorno.size());
    }
    @Test
    void salvarLancamentoCreditoTest(){
        PayBoxEntry e = new PayBoxEntry(null, new BigDecimal(700.00), null, new BigDecimal(700.00), EntryType.CREDIT );
        when(payBoxEntryRepository.findTopByOrderByIdDesc()).thenReturn(Optional.empty());
        when(payBoxEntryRepository.save(e)).thenReturn(generateList().get(0));
        payBoxEntryService.salvarLancamentoCredito(e);
        Assertions.assertEquals(e.getPayBoxTotal(), new BigDecimal(700.00));
    }

    @Test
    void salvarLancamentoDebitTest(){
        PayBoxEntry e = new PayBoxEntry(null, new BigDecimal(500.00), null, new BigDecimal(700.00), EntryType.DEBIT );
        when(payBoxEntryRepository.findTopByOrderByIdDesc()).thenReturn(Optional.of(generateList().get(0)));
        when(payBoxEntryRepository.save(e)).thenReturn(generateList().get(1));
        payBoxEntryService.salvarLancamentoDebito(e);
        Assertions.assertEquals(e.getPayBoxTotal(), new BigDecimal(200.00));
    }

    private List<PayBoxEntry> generateList(){
        PayBoxEntry e = new PayBoxEntry(1l, new BigDecimal(700.00), LocalDateTime.now(), new BigDecimal(700.00), EntryType.CREDIT );
        PayBoxEntry e2 = new PayBoxEntry(2l, new BigDecimal(500.00), LocalDateTime.now(), new BigDecimal(200.00), EntryType.DEBIT );

        return List.of(e, e2);
    }

    private List<PayBoxEntryPerDayDTO> generatePerDayList(){
        PayBoxEntryPerDayDTO e = new PayBoxEntryPerDayDTO("16/05/2023", new BigDecimal(700.00));
        PayBoxEntryPerDayDTO e2 = new PayBoxEntryPerDayDTO("15/05/2023", new BigDecimal(500.00));
        return List.of(e, e2);
    }

    private Page<PayBoxEntry> generatePageEntitys(){
        Page<PayBoxEntry> page = new PageImpl<>(generateList());
        return page;
    }
}
