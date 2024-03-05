package org.example.task.web;

import org.example.task.dto.BranchInfo;
import org.example.task.dto.RepositoryInfo;
import org.example.task.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@WebFluxTest(GithubController.class)
public class GithubControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GitHubService gitHubService;

    @Test
    void getRepositoryInfoByUserName_ReturnsRepositories() {
        Mockito.when(gitHubService.getRepositoryInfoByUserName("testUser"))
                .thenReturn(Flux.just(
                        new RepositoryInfo(
                                "testRepo",
                                "testUser",
                                List.of(new BranchInfo("main", "abc123"))))
                );

        webTestClient.get().uri("/api/v1/users/testUser/repositories")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].repositoryName").isEqualTo("testRepo")
                .jsonPath("$[0].ownerLogin").isEqualTo("testUser")
                .jsonPath("$[0].branchesInfo[0].branchName").isEqualTo("main");
    }


}
