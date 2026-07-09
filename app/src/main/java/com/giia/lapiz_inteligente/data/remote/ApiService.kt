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
import com.giia.lapiz_inteligente.models.pencil.CreatePencilRequest
import com.giia.lapiz_inteligente.models.pencil.PencilResponse
import com.giia.lapiz_inteligente.models.pencil.UpdatePencilRequest
import com.giia.lapiz_inteligente.models.pencil.UpdatePencilStatusRequest
import com.giia.lapiz_inteligente.models.session.CreateSessionRequest
import com.giia.lapiz_inteligente.models.session.EndSessionRequest
import com.giia.lapiz_inteligente.models.session.SessionResponse
import retrofit2.http.Body
import retrofit2.http.GET
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
    suspend fun getProfile(): UserProfileResponse

    @GET("children/")
    suspend fun getChildren(
        @Query("is_active") isActive: Boolean? = null
    ): List<ChildResponse>

    @GET("children/{child_id}")
    suspend fun getChild(
        @Path("child_id") childId: Int
    ): ChildResponse

    @POST("children/")
    suspend fun createChild(
        @Body request: CreateChildRequest
    ): ChildResponse

    @PUT("children/{child_id}")
    suspend fun updateChild(
        @Path("child_id") childId: Int,
        @Body request: UpdateChildRequest
    ): ChildResponse

    @PATCH("children/{child_id}/deactivate")
    suspend fun deactivateChild(
        @Path("child_id") childId: Int
    ): ChildResponse

    @GET("stroke-types")
    suspend fun getStrokeTypes(): List<StrokeTypeResponse>

    @GET("exercises")
    suspend fun getExercises(): List<ExerciseResponse>

    @GET("exercises")
    suspend fun getExercisesByStrokeType(
        @Query("stroke_type_id") strokeTypeId: Int
    ): List<ExerciseResponse>

    @GET("exercises/{id}")
    suspend fun getExerciseDetail(
        @Path("id") exerciseId: Int
    ): ExerciseDetailResponse

    @POST("exercises")
    suspend fun createExercise(
        @Body request: CreateExerciseRequest
    ): ExerciseResponse

    @PUT("exercises/{id}")
    suspend fun updateExercise(
        @Path("id") exerciseId: Int,
        @Body request: UpdateExerciseRequest
    ): ExerciseResponse

    @PATCH("exercises/{id}/deactivate")
    suspend fun deactivateExercise(
        @Path("id") exerciseId: Int
    ): ExerciseResponse

    @POST("pencils/")
    suspend fun createPencil(@Body request: CreatePencilRequest): PencilResponse

    @GET("pencils/")
    suspend fun getPencils(@Query("status") status: String? = null): List<PencilResponse>

    @GET("pencils/{pencil_id}")
    suspend fun getPencil(@Path("pencil_id") pencilId: Int): PencilResponse

    @PUT("pencils/{pencil_id}")
    suspend fun updatePencil(
        @Path("pencil_id") pencilId: Int,
        @Body request: UpdatePencilRequest
    ): PencilResponse

    @PATCH("pencils/{pencil_id}/status")
    suspend fun updatePencilStatus(
        @Path("pencil_id") pencilId: Int,
        @Body request: UpdatePencilStatusRequest
    ): PencilResponse

    @POST("sessions")
    suspend fun createSession(@Body request: CreateSessionRequest): SessionResponse

    @GET("sessions/active")
    suspend fun getActiveSession(
        @Query("child_id") childId: Int? = null,
        @Query("pencil_id") pencilId: Int? = null
    ): SessionResponse

    @GET("sessions/{id}")
    suspend fun getSession(
        @Path("id") sessionId: Int
    ): SessionResponse

    @GET("children/{id}/sessions")
    suspend fun getChildSessions(
        @Path("id") childId: Int,
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 50
    ): List<SessionResponse>

    @PATCH("sessions/{id}/end")
    suspend fun endSession(
        @Path("id") sessionId: Int,
        @Body request: EndSessionRequest
    ): SessionResponse
}
