package pl.edu.pwr.student.actions_feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.edu.pwr.student.actions_feed.dto.GithubListWorkflows

class ActionsViewModel : ViewModel() {
    val actionData: MutableLiveData<List<GithubListWorkflows.WorkflowItem>> =
        MutableLiveData<List<GithubListWorkflows.WorkflowItem>>()
}
