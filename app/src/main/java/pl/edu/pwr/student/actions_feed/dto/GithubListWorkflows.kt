package pl.edu.pwr.student.actions_feed.dto

data class GithubListWorkflows(
    val totalCount: Int,
    val workflowRuns: List<WorkflowItem>
) {
    data class WorkflowItem(
        val id: Int,
        val name: String,
        val workflowId: Int,
        val status: String,
        val conclusion: String,
        val headSha: String,
        val runNumber: Int,
    )
}