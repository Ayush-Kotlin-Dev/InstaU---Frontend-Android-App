package ayush.ggv.instau.data.auth.domain.repository

import ayush.ggv.instau.data.auth.domain.model.AuthResultData
import ayush.ggv.instau.util.Result

interface AuthRepository {
    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): Result<AuthResultData>
    suspend fun signIn(
        email: String,
        password: String
    ): Result<AuthResultData>
}