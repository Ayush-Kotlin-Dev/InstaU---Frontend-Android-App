package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.qnausecase.AddQuestionUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.QnaUseCase
import ayush.ggv.instau.model.qna.QuestionsResponse
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QnaViewModel(
    private val qnaUseCase: QnaUseCase,
    private val addQuestionUseCase: AddQuestionUseCase
) : ViewModel() {

    private val _qnaUiState = mutableStateOf(QnaUiState())
    val qnaUiState: State<QnaUiState> = _qnaUiState

    private val _questionText = mutableStateOf("")
    val questionText: State<String> = _questionText

    fun setQuestionText(content: String) {
        _questionText.value = content
    }

    fun addQuestion(content: String) {
        viewModelScope.launch {
            try {
                _qnaUiState.value = _qnaUiState.value.copy(isLoading = true)
                Log.d("QnaService", "addQuestion: $content")
                val result = addQuestionUseCase(content)
                when (result) {
                    is Result.Success -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                        Log.d("QnaService", "addQuestion: Success")
                    }

                    is Result.Error -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                        Log.d("QnaService", "addQuestion: Error - ${result.message}")

                    }

                    is Result.Loading -> {
                        // Handle loading state if needed
                    }
                }
            } catch (e: Exception) {
                _qnaUiState.value = _qnaUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error adding question: ${e.message}"
                )

            }
        }
    }

    fun fetchQuestionsWithAnswers(token: String) {
        viewModelScope.launch {
            _qnaUiState.value = _qnaUiState.value.copy(isLoading = true)

            try {
                // Simulate a network call or fetch from repository
                val questions = getQuestionsFromApi(token)
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

    suspend fun getQuestionsFromApi(token: String): Result<QuestionsResponse> {
        return withContext(Dispatchers.IO) {
            // Simulated delay to mimic network call
            qnaUseCase(token)
        }
    }
}

data class QnaUiState(
    val isLoading: Boolean = false,
    val questionsWithAnswers: List<QuestionWithAnswer>? = emptyList(),
    val errorMessage: String? = null
)

data class AddQuestionUiState(
    val content : String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
