package org.example.task.service;

import org.example.task.dto.BranchInfo;
import org.example.task.dto.RepositoryInfo;
import org.example.task.exception.ClientErrorException;
import org.example.task.exception.ResourceNotFoundException;
import org.example.task.service.dto.Branch;
import org.example.task.service.dto.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Service
public class GitHubService {

    private static final String REPOS_PER_USER = "/users/{userName}/repos";
    private static final String BRANCHES_PER_USER_AND_REPO = "/repos/{userName}/{branchName}/branches";

    private final WebClient gitHubClient;

    public GitHubService(WebClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public Flux<RepositoryInfo> getRepositoryInfoByUserName(String userName) {
        return gitHubClient.get()
                .uri(REPOS_PER_USER, userName)
                .retrieve()
                .bodyToFlux(Repository.class)
                .filter(repository -> !repository.fork())
                .flatMap(repo -> getBranchesForRepository(userName, repo.name())
                        .collectList()
                        .map(branches -> new RepositoryInfo(repo.name(), repo.owner().login(), branches))
                )
                .onErrorResume(
                        WebClientResponseException.NotFound.class,
                        e -> Mono.error(new ResourceNotFoundException("Resource is not found"))
                );
    }

    public Flux<BranchInfo> getBranchesForRepository(String username, String repoName) {
        return gitHubClient.get()
                .uri(BRANCHES_PER_USER_AND_REPO, username, repoName)
                .retrieve()
                .bodyToFlux(Branch.class)
                .map(branch -> new BranchInfo(branch.name(), branch.commit().sha()));
    }

}




