package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.data.qna.domain.QnaRepository
import ayush.ggv.instau.domain.usecases.qnausecase.QnaDetailUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.QnaUseCase
import ayush.ggv.instau.model.qna.Answer
import ayush.ggv.instau.model.qna.AnswersResponse
import ayush.ggv.instau.model.qna.QuestionResponse
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QnaDetailViewModel(
    private val qnaUseCase: QnaDetailUseCase
) : ViewModel(
) {
    // Combined UI state for questions and answers

    private val _answersUiState = mutableStateOf(AnswersState())
    val answersUiState: State<AnswersState> = _answersUiState


    fun fetchQuestionsWithAnswers(token : String , questionId : Long ) {
        viewModelScope.launch {
            _answersUiState.value = _answersUiState.value.copy(isLoading = true)

            try {
                // Simulate a network call or fetch from repository
                val answers = qnaUseCase(token, questionId , 1 , 10)
                when (answers) {
                    is Result.Success -> {
                        _answersUiState.value = _answersUiState.value.copy(
                            isLoading = false,
                            answers = answers.data?.answers
                        )
                        Log.d(
                            "QnaViewModel",
                            "fetchQuestionsWithhhhAnswers: Success - ${answers.data?.answers}"
                        )
                    }

                    is Result.Error -> {
                        _answersUiState.value = _answersUiState.value.copy(
                            isLoading = false,
                            errorMessage = answers.message
                        )
                        Log.e(
                            "QnaViewModel",
                            "fetchQuestionsWithhhhAnswers: Error - ${answers.message}"
                        )
                    }

                    is Result.Loading -> {
                        // Handle loading state if needed
                        Log.d("QnaViewModel", "fetchQuestionsWithAnswers: Loading")
                    }
                }
            } catch (e: Exception) {
                _answersUiState.value = _answersUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error fetching data: ${e.message}"
                )
                Log.e("QnaViewModel", "fetchQuestionsWithAnswers: Exception - ${e.message}")
            }
        }
    }
}

data class AnswersState(
    val isLoading: Boolean = false,
    val answers: List<Answer>? = emptyList(),
    val errorMessage: String? = null
)
