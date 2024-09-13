package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ayush.ggv.instau.domain.usecases.qnausecase.AddAnswerUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.GetAnswersUseCase
import ayush.ggv.instau.model.qna.Answer
import ayush.ggv.instau.paging.PaginationManager
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QnaDetailViewModel(
    private val qnaUseCase: GetAnswersUseCase,
    private val addAnswerUseCase: AddAnswerUseCase
) : ViewModel() {

    private val _answersUiState = MutableStateFlow(AnswersState())
    val answersUiState = _answersUiState.asStateFlow()

    private val _addAnswerUiState = MutableStateFlow(AddAnswerUiState(""))
    val addAnswerUiState = _addAnswerUiState.asStateFlow()

    fun onTextChange(answer: String) {
        _addAnswerUiState.value = _addAnswerUiState.value.copy(answer = answer)
    }

    fun fetchAnswers(token: String, questionId: Long) {
        viewModelScope.launch {
            val answersPagingFlow = PaginationManager.createPagingFlow(
                fetcher = { page, pageSize ->
                    when (val result = qnaUseCase(token, questionId, page, pageSize)) {
                        is Result.Success -> result.data?.answers ?: emptyList()
                        else -> emptyList()
                    }
                }
            ).flow.cachedIn(viewModelScope)

            _answersUiState.value = _answersUiState.value.copy(answers = answersPagingFlow)
        }
    }

    fun addAnswer(token: String, currentUserId: Long, questionId: Long, content: String) {
        viewModelScope.launch {
            _addAnswerUiState.value = _addAnswerUiState.value.copy(isLoading = true)

            when (val result = addAnswerUseCase(token, currentUserId, questionId, content)) {
                is Result.Success -> {
                    _addAnswerUiState.value = _addAnswerUiState.value.copy(
                        isLoading = false,
                        uploadSuccess = true
                    )
                    fetchAnswers(token, questionId) // Refresh answers
                }
                is Result.Error -> {
                    _addAnswerUiState.value = _addAnswerUiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
}

data class AnswersState(
    val isLoading: Boolean = false,
    val answers: Flow<PagingData<Answer>>? = null,
    val errorMessage: String? = null
)

data class AddAnswerUiState(
    val answer: String,
    val isLoading: Boolean = false,
    val uploadSuccess: Boolean = false,
    val error: String? = null
)