package ayush.ggv.instau.data.auth.data

import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.data.auth.domain.repository.AuthRepository
import ayush.ggv.instau.data.toAuthResultData
import ayush.ggv.instau.util.Result

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData> = withContext(Dispatchers.IO) {
        try {
            val request = SignUpRequest(name, email, password)
            val authResponse = authService.signUp(request)
            if (authResponse.data == null) {
                Result.Error(
                    message =authResponse.errorMessage ?: "Sign up failed"
                )
            } else {
                Result.Success(authResponse.data.toAuthResultData())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }

    }

override suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData> = withContext(Dispatchers.IO) {
        try {
            val request = SignInRequest(email, password)
            val authResponse = authService.signIn(request)
            if (authResponse.data == null) {
                Result.Error(
                    message = authResponse.errorMessage ?: "Sign in failed"
                )
            } else {
                Result.Success(authResponse.data.toAuthResultData())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Oops! we could not send your request. Please try again later.")
        }
    }

}