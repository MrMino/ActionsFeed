package pl.edu.pwr.student.actions_feed.dto

data class GithubListWorkflowJobs(
    val totalCount: String,
    val jobs: List<JobItem>
) {
    data class JobItem (
        val id: String,
        val name: String,
        val conclusion: String,
        val html_url: String,
        val steps: List<StepItem>
    )

    data class StepItem (
        val name: String,
        val conclusion: String,
    )
}
