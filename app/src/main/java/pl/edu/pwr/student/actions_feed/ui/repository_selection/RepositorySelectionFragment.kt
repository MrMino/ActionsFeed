package pl.edu.pwr.student.actions_feed.ui.repository_selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.pwr.student.actions_feed.dao.Repository
import pl.edu.pwr.student.actions_feed.dao.RepositoryDatabase
import pl.edu.pwr.student.actions_feed.databinding.FragmentRepositorySelectionBinding

class RepositorySelectionFragment : Fragment() {
    private lateinit var repositoryListAdapter: RepositorySelectionListAdapter
    private lateinit var binding: FragmentRepositorySelectionBinding
    private lateinit var database: RepositoryDatabase
    private val repositoryList: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRepositorySelectionBinding.inflate(layoutInflater, container, false)

        database = Room.databaseBuilder(requireContext(), RepositoryDatabase::class.java, "repository.db").build()

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        repositoryListAdapter = RepositorySelectionListAdapter(repositoryList)
        binding.recyclerView.adapter = repositoryListAdapter

        binding.addRepo.setOnClickListener { _ -> addRepository(binding.repositoryPath.text.toString()) }

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
        repositoryList.add(repoPath)
        repositoryListAdapter.notifyItemInserted(repositoryList.size - 1)
        lifecycleScope.launch(Dispatchers.IO) {
            database.repositoryDao().addRepository(Repository(0, repoPath))
        }
    }
}