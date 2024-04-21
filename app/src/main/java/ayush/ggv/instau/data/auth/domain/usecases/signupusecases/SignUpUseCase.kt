package ayush.ggv.instau.data.auth.domain.usecases.signupusecases

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
        //all checks for username and password
        if(name.isBlank() || name.length < 3) {
            return Result.Error("Name should be atleast 3 characters long")
        }
        if(email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.Error("Invalid email")
        }
        if(password.isBlank() || password.length < 6) {
            return Result.Error("Password should be atleast 6 characters long")
        }
        return repository.signUp(name, email, password)
    }
}