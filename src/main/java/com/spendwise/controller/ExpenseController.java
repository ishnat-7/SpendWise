package com.spendwise.controller;

import com.spendwise.model.Expense;
import com.spendwise.repository.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Dashboard - show all expenses
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("expenses", expenseRepository.findAll());
        return "index";
    }

    // Show Add Expense page
    @GetMapping("/add")
    public String addExpensePage(Model model) {
        model.addAttribute("expense", new Expense());
        return "add-expense";
    }

    // Save Expense
    @PostMapping("/add")
    public String addExpense(@ModelAttribute Expense expense) {

        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }

        expenseRepository.save(expense);

        return "redirect:/";
    }

    // Delete Expense
    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {

        expenseRepository.deleteById(id);

        return "redirect:/";
    }
}
