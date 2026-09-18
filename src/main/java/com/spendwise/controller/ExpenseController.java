package com.spendwise.controller;

import com.spendwise.model.Budget;
import com.spendwise.model.Expense;
import com.spendwise.repository.BudgetRepository;
import com.spendwise.repository.ExpenseRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public ExpenseController(
            ExpenseRepository expenseRepository,
            BudgetRepository budgetRepository) {

        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        List<Expense> expenses = expenseRepository.findAll();

        double totalSpent = expenses.stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        Budget budget;

        if (budgetRepository.count() == 0) {
            budget = budgetRepository.save(new Budget(10000));
        } else {
            budget = budgetRepository.findAll().get(0);
        }

        double monthlyBudget = budget.getMonthlyBudget();
        double remainingBudget = monthlyBudget - totalSpent;

        double budgetPercentage = 0;

        if (monthlyBudget > 0) {
            budgetPercentage = (totalSpent / monthlyBudget) * 100;
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("monthlyBudget", monthlyBudget);
        model.addAttribute("remainingBudget", remainingBudget);
        model.addAttribute("budgetPercentage", budgetPercentage);

        return "index";
    }

    @GetMapping("/add")
    public String addExpensePage(Model model) {

        model.addAttribute("expense", new Expense());

        return "add-expense";
    }

    @PostMapping("/add")
    public String addExpense(@ModelAttribute Expense expense) {

        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }

        expenseRepository.save(expense);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteExpense(@PathVariable Long id) {

        expenseRepository.deleteById(id);

        return "redirect:/";
    }

    @GetMapping("/budget")
    public String budgetPage(Model model) {

        Budget budget;

        if (budgetRepository.count() == 0) {
            budget = budgetRepository.save(new Budget(10000));
        } else {
            budget = budgetRepository.findAll().get(0);
        }

        model.addAttribute("budget", budget);

        return "budget";
    }

    @PostMapping("/budget")
    public String updateBudget(@RequestParam double monthlyBudget) {

        Budget budget;

        if (budgetRepository.count() == 0) {
            budget = new Budget(monthlyBudget);
        } else {
            budget = budgetRepository.findAll().get(0);
            budget.setMonthlyBudget(monthlyBudget);
        }

        budgetRepository.save(budget);

        return "redirect:/";
    }
}
