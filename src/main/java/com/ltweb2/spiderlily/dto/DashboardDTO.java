package com.ltweb2.spiderlily.dto;

public class DashboardDTO {
    private long totalCategories;
    private long totalBooks;
    private long totalUsers;

    public DashboardDTO(long totalCategories, long totalBooks, long totalUsers) {
        this.totalCategories = totalCategories;
        this.totalBooks = totalBooks;
        this.totalUsers = totalUsers;
    }

    // --- GETTERS ---
    public long getTotalCategories() {
        return totalCategories;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public long getTotalUsers() {
        return totalUsers;
    }
}