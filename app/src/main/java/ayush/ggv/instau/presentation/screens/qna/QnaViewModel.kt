package ayush.ggv.instau.presentation.screens.qna

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import ayush.ggv.instau.domain.usecases.qnausecase.AddQuestionUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.DeleteQuestionsUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.QnaUseCase
import ayush.ggv.instau.model.qna.QuestionWithAnswer
import ayush.ggv.instau.paging.PaginationManager
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QnaViewModel(
    private val qnaUseCase: QnaUseCase,
    private val addQuestionUseCase: AddQuestionUseCase,
    private val deleteQuestionUseCase: DeleteQuestionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QnaUiState())
    val uiState = _uiState.asStateFlow()

    private val _questionText = mutableStateOf("")
    val questionText: State<String> = _questionText

    fun setQuestionText(content: String) {
        _questionText.value = content
    }
    init {
        fetchQuestionsWithAnswers()
    }

    fun fetchQuestionsWithAnswers() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            val questionsPagingFlow = PaginationManager.createPagingFlow(
                fetcher = { page, pageSize ->
                    when (val result = qnaUseCase(page, pageSize)) {
                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                            result.data?.questions ?: emptyList()
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = result.message
                            )
                            emptyList()
                        }
                        else -> emptyList()
                    }
                }
            ).flow.cachedIn(viewModelScope)

            _uiState.value = _uiState.value.copy(questionsWithAnswers = questionsPagingFlow )
        }
    }

    fun deleteQuestion(questionId: Long) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = deleteQuestionUseCase(questionId)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                    fetchQuestionsWithAnswers() // Refresh questions
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun addQuestion(content: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            when (val result = addQuestionUseCase(content)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                    fetchQuestionsWithAnswers() // Refresh questions
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                is Result.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
}

data class QnaUiState(
    val isLoading: Boolean = false,
    val questionsWithAnswers: Flow<PagingData<QuestionWithAnswer>>? = null,
    val errorMessage: String? = null
)