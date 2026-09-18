package com.spendwise.controller;

import com.spendwise.model.Expense;
import com.spendwise.repository.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AnalyticsController {

    private final ExpenseRepository expenseRepository;

    public AnalyticsController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping("/analytics")
    public String analytics(Model model) {

        List<Expense> expenses = expenseRepository.findAll();

        double totalSpent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Map<String, Double> categoryTotals = new LinkedHashMap<>();

        for (Expense expense : expenses) {
            categoryTotals.merge(
                    expense.getCategory(),
                    expense.getAmount(),
                    Double::sum
            );
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("categoryTotals", categoryTotals);

        return "analytics";
    }
}
