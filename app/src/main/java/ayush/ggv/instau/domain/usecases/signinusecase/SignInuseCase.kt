package ayush.ggv.instau.domain.usecases.signinusecase

import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import org.koin.core.component.KoinComponent
import ayush.ggv.instau.util.Result
import org.koin.core.component.inject
class SignInuseCase : KoinComponent {
    private val repository: AuthRepository by inject()

        suspend operator fun invoke(
            email: String,
            password: String
        ): Result<AuthResultData> {
            //all checks for username and password
            if(email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return Result.Error("Invalid email")
            }
            if(password.isBlank() || password.length < 6) {
                return Result.Error("Password should be atleast 6 characters long")
            }

            return repository.signIn(email, password)
        }

}