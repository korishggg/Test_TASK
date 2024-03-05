package org.example.task.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.task.dto.ErrorStatus;
import org.example.task.dto.RepositoryInfo;
import org.example.task.service.GitHubService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1")
public class GithubController {

    private final GitHubService gitHubService;

    public GithubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @Operation(summary = "Get not forked repositories info by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Founded Not forked repos", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RepositoryInfo.class)) }),
            @ApiResponse(responseCode = "404", description = "Repo by username is not found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStatus.class)) }),
            @ApiResponse(responseCode = "406", description = "XML format is not allowed", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorStatus.class)) })
    })
    @GetMapping(value = "/users/{userName}/repositories")
    public Flux<RepositoryInfo> getRepositoryInfoByUserName(@PathVariable String userName) {
        return gitHubService.getRepositoryInfoByUserName(userName);
    }
}
