package org.example.task.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.task.service.dto.Branch;
import org.example.task.service.dto.Repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(GitHubIntegrationTest.TestConfig.class)
public class GitHubIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;
    private static MockWebServer mockWebServer;
    private static ObjectMapper objectMapper;


    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public WebClient testWebClient() {
            return WebClient.builder()
                    .baseUrl(mockWebServer.url("/").toString())
                    .build();
        }
    }

    @Test
    void fetchNonForkedRepoIntegration() throws JsonProcessingException {
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

        webTestClient.get().uri("/api/v1/users/{userName}/repositories", userName)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].repositoryName").isEqualTo(expectedRepoName)
                .jsonPath("$[0].ownerLogin").isEqualTo(userName)
                .jsonPath("$[0].branchesInfo[0].branchName").isEqualTo(expectedBranchName);
    }

    @Test
    void fetchNonForkedRepoIntegration_WhenUserIsNotFound() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(404));

        webTestClient.get().uri("/api/v1/users/{userName}/repositories", "notExistingUser")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.value())
                .jsonPath("$.message").isEqualTo("Resource is not found");
    }


    @Test
    void fetchNonForkedRepoIntegration_WhenXmlAcceptHeaderExists() {
        webTestClient.get()
                .uri("/api/v1/users/{userName}/repositories", "userName")
                .accept(MediaType.APPLICATION_XML)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE.value())
                .expectBody()
                .jsonPath("$.status").isEqualTo(HttpStatus.NOT_ACCEPTABLE.value())
                .jsonPath("$.message").isEqualTo("XML Accept header is not supported.");
    }

}
