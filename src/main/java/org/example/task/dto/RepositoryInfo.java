package org.example.task.dto;

import java.util.List;

public record RepositoryInfo(String repositoryName, String ownerLogin, List<BranchInfo> branchesInfo) {
}
