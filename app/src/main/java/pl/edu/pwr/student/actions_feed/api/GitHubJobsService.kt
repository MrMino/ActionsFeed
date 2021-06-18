package pl.edu.pwr.student.actions_feed.api

import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflowJobs
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GitHubJobsService {
    @GET("repos/{user}/{repository}/actions/runs/{runId}/jobs")
    fun listJobs(
        @Path("user") user: String,
        @Path("repository") repository: String,
        @Path("runId") runId: Int,
        @Header("Authorization") auth: String
    ): Call<GithubListWorkflowJobs>
}
