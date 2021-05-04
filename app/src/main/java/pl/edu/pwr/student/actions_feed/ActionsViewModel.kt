package pl.edu.pwr.student.actions_feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ActionsViewModel : ViewModel() {
    val actionData: MutableLiveData<MutableList<ActionData>> = MutableLiveData<MutableList<ActionData>>()
}

enum class ActionStatus {
    Started,
    Finished,
    Failed,
}

class ActionData(val status: ActionStatus, val name: String)
