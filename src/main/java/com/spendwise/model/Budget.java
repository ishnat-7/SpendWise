package com.spendwise.model;

import jakarta.persistence.*;

@Entity
@Table(name = "budgets")
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double monthlyBudget = 10000;

    public Budget() {
    }

    public Budget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public Long getId() {
        return id;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
