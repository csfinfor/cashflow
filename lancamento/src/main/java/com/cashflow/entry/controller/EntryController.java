package com.cashflow.entry.controller;

import com.cashflow.entry.domain.PayBoxEntry;
import com.cashflow.entry.dto.PayBoxEntryDTO;
import com.cashflow.entry.dto.PayBoxEntryPerDayDTO;
import com.cashflow.entry.service.PayBoxEntryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/entrys")
@AllArgsConstructor
public class EntryController {

    @Autowired
    private PayBoxEntryService service;

    @Operation(summary  = "Retorna todos os lançamentos"
            ,description = "Retorna todos os lançamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/xml", schema = @Schema(implementation = PayBoxEntryDTO.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = PayBoxEntryDTO.class)) })})
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity listAll(Pageable pageable){
        Page<PayBoxEntryDTO> dtoList = service.findAll(pageable);
        return new ResponseEntity(dtoList, HttpStatus.OK);
    }
    @Operation(summary  = "Retorna o saldo por dia"
            ,description = "Retorna o saldo por dia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/xml", schema = @Schema(implementation = PayBoxEntryPerDayDTO.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = PayBoxEntryPerDayDTO.class)) })})
    @GetMapping("/perDay")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity listAllPerDay(){
        List<PayBoxEntryPerDayDTO> dtoList = service.findAllPerDay();
        return new ResponseEntity(dtoList, HttpStatus.OK);
    }

    @Operation(summary  = "Retorna o saldo atual para o dia de hoje"
            ,description = "Retorna o saldo atual para o dia de hoje")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/xml", schema = @Schema(implementation = PayBoxEntryPerDayDTO.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = PayBoxEntryPerDayDTO.class)) })})
    @GetMapping("/balanceToDay")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity balanceToDay(){
        PayBoxEntryPerDayDTO dto = service.findBalanceToDay();
        return new ResponseEntity(dto , HttpStatus.OK);
    }

    @Operation(summary  = "Persistir lançamentos de credito do fluxo de caixa"
            ,description = "Persistir lançamentos de credito do fluxo de caixa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/xml", schema = @Schema(implementation = PayBoxEntry.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = PayBoxEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PostMapping("/credit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity saveEntryCredit(@Valid @RequestBody PayBoxEntryDTO entry){
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        PayBoxEntry entryConvert =  mapper.convertValue(entry, PayBoxEntry.class);
        service.salvarLancamentoCredito(entryConvert);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary  = "Persistir lançamentos de debito do fluxo de caixa"
            ,description = "Persistir lançamentos de debito  do fluxo de caixa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = { @Content(mediaType = "application/xml", schema = @Schema(implementation = PayBoxEntry.class)), @Content(mediaType = "application/json", schema = @Schema(implementation = PayBoxEntry.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input")})
    @PostMapping("/debit")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity saveEntryDebit(@Valid @RequestBody PayBoxEntryDTO entry){
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
        PayBoxEntry entryConvert =  mapper.convertValue(entry, PayBoxEntry.class);
        service.salvarLancamentoDebito(entryConvert);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
