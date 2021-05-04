package pl.edu.pwr.student.actions_feed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import pl.edu.pwr.student.actions_feed.databinding.FragmentActionListBinding

class ActionListFragment : Fragment() {
    private lateinit var binding: FragmentActionListBinding
    private val actionsViewModel: ActionsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActionListBinding.inflate(inflater, container, false)

        actionsViewModel.actionData.observe(viewLifecycleOwner, {
            Log.d(null, "Repos updated $it") // TODO: Replace me
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_ActionListFragment_to_ActionDetailsFragment)
        }
    }
}
