package pl.edu.pwr.student.actions_feed

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pwr.student.actions_feed.api.GitHubService
import pl.edu.pwr.student.actions_feed.dao.RepositoryDatabase
import pl.edu.pwr.student.actions_feed.databinding.ActivityMainBinding
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration
import java.time.Instant

class MainActivity : AppCompatActivity(), Callback<GithubListWorkflows> {
    private val actionsViewModel: ActionsViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var preferenceManager: SharedPreferences
    private lateinit var repositoryDatabase: RepositoryDatabase
    private val gson: Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        requestQueue = Volley.newRequestQueue(this)
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)
        repositoryDatabase = Room.databaseBuilder(applicationContext, RepositoryDatabase::class.java, "repository.db")
            .build()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val retrofit = Retrofit.Builder().baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
                val ghAPI = retrofit.create(GitHubService::class.java)

                while (true) {
                    val start = Instant.now()
                    val token = preferenceManager.getString("token", null)
                    val waitTime = Duration.ofSeconds(preferenceManager.getString("waitTime", "15")!!.toLong())
                    if (token != null) {
                        runBackgroundRefresh(ghAPI, token)
                    }
                    val end = Instant.now()
                    val delta = Duration.between(end, start)
                    delay((waitTime - delta).toMillis())
                }
            }
        }
    }

    private fun runBackgroundRefresh(ghAPI: GitHubService, token: String) {
        val repositories = repositoryDatabase.repositoryDao().getAll()

        for (repoPath in repositories) {
            val split = repoPath.split("/")

            Log.d(null, "${split[0]}, ${split[1]}, $token")

            val call = ghAPI.listRepos(split[0], split[1], "token $token")

            call.enqueue(this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_global_SettingsFragment)
                true
            }
            R.id.action_select_repositories -> {
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_global_RepositorySelectionFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onResponse(
        call: Call<GithubListWorkflows>,
        response: Response<GithubListWorkflows>
    ) {
        val workflows = response.body() ?: return
        Log.d(null, "workflows $workflows ${response.isSuccessful}")
        if (workflows.workflowRuns.any()) {
        val data = actionsViewModel.actionData.value?.toMutableMap() ?: mutableMapOf()
            data[workflows.workflowRuns.first().repository.fullName] = workflows.workflowRuns
            actionsViewModel.actionData.value = data
        }
    }

    override fun onFailure(call: Call<GithubListWorkflows>, t: Throwable) {
        Log.d(null, "Workflow query failed $t")
    }
}