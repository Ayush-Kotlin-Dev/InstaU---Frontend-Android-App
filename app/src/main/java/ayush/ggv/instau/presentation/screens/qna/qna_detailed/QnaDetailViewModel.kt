package ayush.ggv.instau.presentation.screens.qna.qna_detailed

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ayush.ggv.instau.domain.usecases.qnausecase.AddAnswerUseCase
import ayush.ggv.instau.domain.usecases.qnausecase.QnaDetailUseCase
import ayush.ggv.instau.model.qna.Answer
import ayush.ggv.instau.util.Result
import kotlinx.coroutines.launch

class QnaDetailViewModel(
    private val qnaUseCase: QnaDetailUseCase,
    private val addAnswerUseCase : AddAnswerUseCase
) : ViewModel(
) {
    // Combined UI state for questions and answers

    private val _answersUiState = mutableStateOf(AnswersState())
    val answersUiState: State<AnswersState> = _answersUiState

    private val _addAnswerUiState = mutableStateOf(addAnsweruiState(""))
    val addAnswerUiState : State<addAnsweruiState> = _addAnswerUiState

    fun onTextChange(answer : String){
        _addAnswerUiState.value = _addAnswerUiState.value.copy(answer = answer)
    }


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
    fun addAnswer(token : String , currentUserId : Long , questionId : Long , content : String) {
        viewModelScope.launch {
            _addAnswerUiState.value = _addAnswerUiState.value.copy(isLoading = true)

            try {
                val answers = addAnswerUseCase(token, currentUserId , questionId , content)
                when (answers) {
                    is Result.Success -> {
                        _addAnswerUiState.value = _addAnswerUiState.value.copy(
                            isLoading = false,
                            uploadSuccess = true
                        )
                        Log.d(
                            "QnaViewModel",
                            "fetchQuestionsWithhhhAnswers: Success - ${answers.data?.answers}"
                        )
                    }

                    is Result.Error -> {
                        _addAnswerUiState.value = _addAnswerUiState.value.copy(
                            isLoading = false,
                            error = answers.message
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

data class addAnsweruiState(
    val answer : String ,
    val isLoading : Boolean = false,
    val uploadSuccess : Boolean = false,
    val error : String? = null
)
