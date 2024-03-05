package org.example.task.service.dto;

public record Branch(String name, Commit commit) {
    public record Commit(String sha) {
    }
}