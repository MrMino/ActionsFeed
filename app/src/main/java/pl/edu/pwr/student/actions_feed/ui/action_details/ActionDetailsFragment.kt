package pl.edu.pwr.student.actions_feed.ui.action_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pl.edu.pwr.student.actions_feed.api.GitHubJobsService
import pl.edu.pwr.student.actions_feed.databinding.FragmentActionDetailsBinding
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflowJobs
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows
import pl.edu.pwr.student.actions_feed.ui.action_list.ActionListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ActionDetailsFragment : Fragment(), Callback<GithubListWorkflowJobs>{

    private var _binding: FragmentActionDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val args: ActionDetailsFragmentArgs by navArgs()

    private val gson: Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    private val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    private val ghAPI = retrofit.create(GitHubJobsService::class.java)

    private lateinit var jobsListAdapter : JobsListAdapter
    private val jobsList: MutableList<GithubListWorkflowJobs.JobItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionDetailsBinding.inflate(inflater, container, false)

        binding.jobsList.layoutManager = LinearLayoutManager(context)
        jobsListAdapter = JobsListAdapter(jobsList, requireContext())
        binding.jobsList.adapter = jobsListAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(context)
        val runId = args.runId
        val user = args.user
        val repo = args.repository
        val token = preferenceManager.getString("token", null)
        val call = ghAPI.listJobs(user, repo, runId, "token $token")
        call.enqueue(this)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResponse(
        call: Call<GithubListWorkflowJobs>,
        response: Response<GithubListWorkflowJobs>
    ) {
        val jobs = response.body()
        Log.d("JOBS_API", "${jobs!!.totalCount}")
        jobsList.addAll(jobs.jobs)
        binding.progressBar.visibility = ProgressBar.GONE
        jobsListAdapter.notifyDataSetChanged()
        binding.jobsList.visibility = RecyclerView.VISIBLE
    }

    override fun onFailure(call: Call<GithubListWorkflowJobs>, t: Throwable) {
        Log.e("ERROR", "FAILURE IN JOBS LISTING $t")
    }
}