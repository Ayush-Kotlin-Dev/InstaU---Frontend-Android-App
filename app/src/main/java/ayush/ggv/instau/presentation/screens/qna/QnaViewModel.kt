package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.qnausecase.QnaUseCase
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QnaViewModel(
   private val qnaUseCase: QnaUseCase
) : ViewModel() {

    // Combined UI state for questions and answers

    private val _qnaUiState = mutableStateOf(QnaUiState())
    val qnaUiState: State<QnaUiState> = _qnaUiState

    init {
        fetchQuestionsWithAnswers()
    }

    fun fetchQuestionsWithAnswers() {
        viewModelScope.launch {
            _qnaUiState.value = _qnaUiState.value.copy(isLoading = true)

            try {
                // Simulate a network call or fetch from repository
                val questions = getQuestionsFromApi()
                when (questions) {
                    is Result.Success -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            questionsWithAnswers = questions.data?.questions
                        )
                        Log.d(
                            "QnaViewModel",
                            "fetchQuestionsWithAnswers: Success - ${questions.data?.questions}"
                        )
                    }

                    is Result.Error -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            errorMessage = questions.message
                        )
                        Log.e(
                            "QnaViewModel",
                            "fetchQuestionsWithAnswers: Error - ${questions.message}"
                        )
                    }

                    is Result.Loading -> {
                        // Handle loading state if needed
                        Log.d("QnaViewModel", "fetchQuestionsWithAnswers: Loading")
                    }
                }
            } catch (e: Exception) {
                _qnaUiState.value = _qnaUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error fetching data: ${e.message}"
                )
                Log.e("QnaViewModel", "fetchQuestionsWithAnswers: Exception - ${e.message}")
            }
        }
    }

    suspend fun getQuestionsFromApi(): Result<QuestionResponse> {
        return withContext(Dispatchers.IO) {
            // Simulated delay to mimic network call
            qnaUseCase("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJzb2NpYWxhcHBrdG9yMzA3MSIsImlzcyI6bnVsbCwiZW1haWwiOiJheXVzaEBnbWFpbC5jb20ifQ.TqNhQIjgCVp47J_Fl8yFmRJgg3OHRbzT5wtNSE52ROg")
        }
    }
}

data class QnaUiState(
    val isLoading: Boolean = false,
    val questionsWithAnswers: List<QuestionWithAnswer>? = emptyList(),
    val errorMessage: String? = null
)
