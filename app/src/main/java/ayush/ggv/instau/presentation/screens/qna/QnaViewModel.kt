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
    init {
        fetchQuestionsWithAnswers()
    }
    fun addQuestion(content: String) {
        viewModelScope.launch {
            try {
                _qnaUiState.value = _qnaUiState.value.copy(isLoading = true)
                when (val result = addQuestionUseCase(content)) {
                    is Result.Success -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            errorMessage = null
                        )
                        fetchQuestionsWithAnswers() // Refresh questions
                    }
                    is Result.Error -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
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

    fun fetchQuestionsWithAnswers() {
        viewModelScope.launch {
            _qnaUiState.value = _qnaUiState.value.copy(isLoading = true)
            try {
                when (val questions = qnaUseCase()) {
                    is Result.Success -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            questionsWithAnswers = questions.data?.questions
                        )
                    }
                    is Result.Error -> {
                        _qnaUiState.value = _qnaUiState.value.copy(
                            isLoading = false,
                            errorMessage = questions.message
                        )
                    }
                    is Result.Loading -> {
                        // Handle loading state if needed
                    }
                }
            } catch (e: Exception) {
                _qnaUiState.value = _qnaUiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error fetching data: ${e.message}"
                )
            }
        }
    }
}

data class QnaUiState(
    val isLoading: Boolean = false,
    val questionsWithAnswers: List<QuestionWithAnswer>? = emptyList(),
    val errorMessage: String? = null
)