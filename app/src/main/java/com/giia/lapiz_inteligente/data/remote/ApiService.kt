package com.giia.lapiz_inteligente.data.remote

import com.giia.lapiz_inteligente.models.auth.LoginRequest
import com.giia.lapiz_inteligente.models.auth.LoginResponse
import com.giia.lapiz_inteligente.models.auth.RegisterRequest
import com.giia.lapiz_inteligente.models.auth.RegisterResponse
import com.giia.lapiz_inteligente.models.auth.UserProfileResponse
import com.giia.lapiz_inteligente.models.child.ChildResponse
import com.giia.lapiz_inteligente.models.child.CreateChildRequest
import com.giia.lapiz_inteligente.models.child.UpdateChildRequest
import com.giia.lapiz_inteligente.models.exercise.CreateExerciseRequest
import com.giia.lapiz_inteligente.models.exercise.ExerciseDetailResponse
import com.giia.lapiz_inteligente.models.exercise.ExerciseResponse
import com.giia.lapiz_inteligente.models.exercise.StrokeTypeResponse
import com.giia.lapiz_inteligente.models.exercise.UpdateExerciseRequest
import com.giia.lapiz_inteligente.models.session.CreateSessionRequest
import com.giia.lapiz_inteligente.models.session.EndSessionRequest
import com.giia.lapiz_inteligente.models.session.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @GET("users/me")
    suspend fun getProfile(@Header("Authorization") token: String): UserProfileResponse

    @GET("children/")
    suspend fun getChildren(
        @Header("Authorization") token: String,
        @Query("is_active") isActive: Boolean? = null
    ): List<ChildResponse>

    @GET("children/{child_id}")
    suspend fun getChild(
        @Header("Authorization") token: String,
        @Path("child_id") childId: Int
    ): ChildResponse

    @POST("children/")
    suspend fun createChild(
        @Header("Authorization") token: String,
        @Body request: CreateChildRequest
    ): ChildResponse

    @PUT("children/{child_id}")
    suspend fun updateChild(
        @Header("Authorization") token: String,
        @Path("child_id") childId: Int,
        @Body request: UpdateChildRequest
    ): ChildResponse

    @PATCH("children/{child_id}/deactivate")
    suspend fun deactivateChild(
        @Header("Authorization") token: String,
        @Path("child_id") childId: Int
    ): ChildResponse

    @GET("stroke-types")
    suspend fun getStrokeTypes(@Header("Authorization") token: String): List<StrokeTypeResponse>

    @GET("exercises")
    suspend fun getExercises(@Header("Authorization") token: String): List<ExerciseResponse>

    @GET("exercises")
    suspend fun getExercisesByStrokeType(
        @Header("Authorization") token: String,
        @Query("stroke_type_id") strokeTypeId: Int
    ): List<ExerciseResponse>

    @GET("exercises/{id}")
    suspend fun getExerciseDetail(
        @Header("Authorization") token: String,
        @Path("id") exerciseId: Int
    ): ExerciseDetailResponse

    @POST("exercises")
    suspend fun createExercise(
        @Header("Authorization") token: String,
        @Body request: CreateExerciseRequest
    ): ExerciseResponse

    @PUT("exercises/{id}")
    suspend fun updateExercise(
        @Header("Authorization") token: String,
        @Path("id") exerciseId: Int,
        @Body request: UpdateExerciseRequest
    ): ExerciseResponse

    @PATCH("exercises/{id}/deactivate")
    suspend fun deactivateExercise(
        @Header("Authorization") token: String,
        @Path("id") exerciseId: Int
    ): ExerciseResponse

    @POST("sessions")
    suspend fun createSession(
        @Header("Authorization") token: String,
        @Body request: CreateSessionRequest
    ): SessionResponse

    @GET("sessions/{id}")
    suspend fun getSession(
        @Header("Authorization") token: String,
        @Path("id") sessionId: Int
    ): SessionResponse

    @GET("children/{id}/sessions")
    suspend fun getChildSessions(
        @Header("Authorization") token: String,
        @Path("id") childId: Int,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 50
    ): List<SessionResponse>

    @PATCH("sessions/{id}/end")
    suspend fun endSession(
        @Header("Authorization") token: String,
        @Path("id") sessionId: Int,
        @Body request: EndSessionRequest
    ): SessionResponse
}
