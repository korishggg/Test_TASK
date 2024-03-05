package org.example.task.service.dto;

public record Repository(String name, boolean fork, Owner owner) {
    public record Owner(String login) {
    }
}