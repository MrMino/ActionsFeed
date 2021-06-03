package pl.edu.pwr.student.actions_feed.dto

import java.sql.Timestamp

data class GithubListWorkflows(
    val totalCount: Int,
    val workflowRuns: List<WorkflowItem>
) {
    data class GitPersona(
        val name: String,
        val email: String,
    )

    data class CommitInfo(
        val id: String,
        val tree_id: String,
        val message: String,
        val timestamp: Timestamp,
        val author: GitPersona,
        val committer: GitPersona,
    )

    data class WorkflowItem(
        val id: Int,
        val name: String,
        val workflowId: Int,
        val head_commit: CommitInfo,
        val head_branch: String,
        val status: String,
        val conclusion: String,
        val headSha: String,
        val runNumber: Int,
    )
}