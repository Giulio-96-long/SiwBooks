package com.example.demo.exception;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.http.HttpStatus;

import com.example.demo.serviceImpl.service.ErrorLogService;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogService errorLogService;

    // 400 – bad request
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBadRequest(Exception ex, Model model) {
        errorLogService.saveErrorLog("ControllerAdvice/400", ex);
        model.addAttribute("status", 400);
        model.addAttribute("message", "Richiesta non valida. Controlla i dati inviati.");
        return "error/400";
    }       
    
    // 403 – non autorizzato
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDenied(AccessDeniedException ex, Model model) {
        errorLogService.saveErrorLog("ControllerAdvice/403", ex);
        model.addAttribute("status", 403);
        model.addAttribute("message", "Non sei autorizzato a visualizzare questa pagina.");
        return "error/403";
    }
    
    // 404 – handler mancante
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        errorLogService.saveErrorLog("ControllerAdvice/404", ex);
        model.addAttribute("status", 404);
        model.addAttribute("message", "Pagina non trovata.");
        return "error/404";
    }
    
    @ExceptionHandler(SQLException.class)
    public String handleSqlException(SQLException ex, Model model) {
        errorLogService.saveErrorLog("ControllerAdvice/500-SQL", ex);
        model.addAttribute("status", 500);
        model.addAttribute("message", "Errore interno al database. Riprova più tardi.");
        return "error/500";
    }

    // 500 – errore generico
    @ExceptionHandler({IOException.class, Exception.class})
    public String handleServerError(Exception ex, Model model) {
        errorLogService.saveErrorLog("ControllerAdvice/500", ex);

        model.addAttribute("status", 500);
        model.addAttribute("message", "Si è verificato un errore interno. Riprova più tardi.");
        return "error/500";
    }
}