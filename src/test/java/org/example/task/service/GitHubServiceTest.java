package org.example.task.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.task.exception.ResourceNotFoundException;
import org.example.task.service.dto.Branch;
import org.example.task.service.dto.Repository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

class GitHubServiceTest {

    private static MockWebServer mockWebServer;
    private static GitHubService gitHubService;
    private static ObjectMapper objectMapper;

    @BeforeAll
    public static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        WebClient webClient = WebClient.builder()
                .baseUrl(mockWebServer.url("/").toString())
                .build();
        gitHubService = new GitHubService(webClient);
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getRepositoryInfoByUserName_ReturnsRepositories() throws JsonProcessingException {
        var userName = "testUser";
        var expectedRepoName = "notForkedRepo";
        var expectedBranchName = "main";

        var repos = List.of(
                new Repository(expectedRepoName, false, new Repository.Owner(userName)),
                new Repository("forkedRepo", true, new Repository.Owner(userName))
        );
        var repoJsonResponse = objectMapper.writeValueAsString(repos);
        mockWebServer.enqueue(new MockResponse()
                .setBody(repoJsonResponse)
                .addHeader("Content-Type", "application/json"));

        var branch = new Branch(expectedBranchName, new Branch.Commit("12345"));
        var branchJsonResponse = objectMapper.writeValueAsString(branch);
        mockWebServer.enqueue(new MockResponse()
                .setBody(branchJsonResponse)
                .addHeader("Content-Type", "application/json"));

        StepVerifier.create(gitHubService.getRepositoryInfoByUserName(userName))
                .expectNextMatches(repoInfo ->
                        repoInfo.repositoryName().equals(expectedRepoName) &&
                                repoInfo.ownerLogin().equals(userName) &&
                                repoInfo.branchesInfo().get(0).branchName().equals(expectedBranchName))
                .verifyComplete();
    }

    @Test
    void getRepositoryInfoByUserName_HandlesNotFound() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        StepVerifier.create(gitHubService.getRepositoryInfoByUserName("nonexistentUser"))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

}