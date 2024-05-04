package ayush.ggv.instau.domain.usecases.signupusecases

import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ayush.ggv.instau.util.Result

class SignUpUseCase : KoinComponent {

    private val repository: AuthRepository by inject()

    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ) : Result<AuthResultData> {
        if(email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error("Invalid email")
        }
        return repository.signUp(name, email, password)
    }
}