package pl.edu.pwr.student.actions_feed.api

import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface GitHubService {
    @GET("repos/{user}/{repository}/actions/runs")
    fun listRepos(
        @Path("user") user: String,
        @Path("repository") repository: String,
        @Header("Authorization") auth: String
    ): Call<GithubListWorkflows>
}