package pl.edu.pwr.student.actions_feed.ui.repository_selection

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pl.edu.pwr.student.actions_feed.dao.Repository
import pl.edu.pwr.student.actions_feed.dao.RepositoryDatabase
import pl.edu.pwr.student.actions_feed.databinding.FragmentRepositorySelectionBinding
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositorySelectionFragment : Fragment() {
    private lateinit var repositoryListAdapter: RepositorySelectionListAdapter
    private lateinit var binding: FragmentRepositorySelectionBinding
    private lateinit var database: RepositoryDatabase
    private val repositoryList: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositorySelectionBinding.inflate(layoutInflater, container, false)

        database =
            Room.databaseBuilder(requireContext(), RepositoryDatabase::class.java, "repository.db")
                .build()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        repositoryListAdapter = RepositorySelectionListAdapter(repositoryList)
        binding.recyclerView.adapter = repositoryListAdapter

        binding.addRepo.setOnClickListener { _ -> addRepository(binding.repositoryPath.text.toString()) }
        //Ugly for now
        binding.deleteRepo.setOnClickListener { _ -> deleteRepository(binding.repositoryPath.text.toString()) }

        initializeRecyclerView()

        return binding.root
    }

    private fun initializeRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {
            repositoryList.clear()
            repositoryList.addAll(database.repositoryDao().getAll())
            repositoryListAdapter.notifyDataSetChanged()
        }
    }

    private fun addRepository(repoPath: String) {
        //First check if is already in list
        if (!repositoryList.contains(repoPath)) {
            checkExistence(repoPath)
        } else {
            showDialogWindow("Duplication", "You are already watching this repo")
        }
    }

    private fun checkExistence(repoPath: String) = runBlocking {
        var flag = true
        val job = CoroutineScope(Dispatchers.IO).launch {
            val url = URL("https://github.com/$repoPath")
            val connection = url.openConnection() as HttpsURLConnection
            try {
                if (connection.responseCode == HttpsURLConnection.HTTP_NOT_FOUND)
                    flag = false
            } finally {
                connection.disconnect()
            }
        }
        job.join()
        if (flag) acceptRepository(repoPath)
        else showDialogWindow("Lack of Existence", "That repo does not exist")
    }

    private fun acceptRepository(repoPath: String) {
        repositoryList.add(repoPath)
        repositoryListAdapter.notifyItemInserted(repositoryList.size - 1)
        lifecycleScope.launch(Dispatchers.IO) {
            database.repositoryDao().addRepository(Repository(0, repoPath))
        }
    }

    //Ugly for now
    private fun deleteRepository(repoPath: String) {
        if (repositoryList.contains(repoPath)) {
            lifecycleScope.launch(Dispatchers.IO) {
                database.repositoryDao().deleteRepositoryByName(repoPath)
            }
            repositoryListAdapter.deleteItem(repoPath)
        } else {
            showDialogWindow("Lack of repo", "You are not watching that repo yet")
        }
    }

    private fun showDialogWindow(title: String, message: String) {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _: DialogInterface, _: Int -> }
        builder.show()
    }
}