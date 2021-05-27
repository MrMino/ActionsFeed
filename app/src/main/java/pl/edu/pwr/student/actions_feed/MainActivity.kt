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
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.edu.pwr.student.actions_feed.databinding.ActivityMainBinding
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows
import java.time.Duration
import java.time.Instant
import java.util.*

class MainActivity : AppCompatActivity() {
    private val actionsViewModel: ActionsViewModel by viewModels()
    private val waitTime: Duration = Duration.ofSeconds(15) // TODO: Move this to settings
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestQueue: RequestQueue
    private lateinit var preferenceManager: SharedPreferences
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

        binding.fab.setOnClickListener { _ ->
            val nav = findNavController(R.id.nav_host_fragment_content_main)
            nav.navigate(R.id.action_ActionListFragment_to_ActionRepositorySelectionFragment)
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                while (true) {
                    val start = Instant.now()
                    runBackgroundRefresh()
                    val end = Instant.now()
                    val delta = Duration.between(end, start)
                    delay((waitTime - delta).toMillis())
                }
            }
        }
    }

    private suspend fun runBackgroundRefresh() {
        val repoPath = preferenceManager.getString("repository", null)
            ?: return

        val rq = object : StringRequest(
            Method.GET,
            "https://api.github.com/repos/$repoPath/actions/runs",
            {
                lifecycleScope.launch {
                    withContext(Dispatchers.Main) {
                        Log.d(null, it)
                        val obj = gson.fromJson(it, GithubListWorkflows::class.java)
                        actionsViewModel.actionData.value = obj.workflowRuns
                    }
                }
            },
            null
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val token = preferenceManager.getString("token", null)
                val headers = HashMap<String, String>()
                if (token != null) {
                    headers.put("Authorization", "token $token")
                }
                return headers
            }
        }

        requestQueue.add(rq)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}