package com.giia.lapiz_inteligente.navigation

object Routes {
    const val SPLASH = "splash"

    const val LOGIN = "login"
    const val REGISTER = "register"

    const val MAIN = "main"

    const val CHILDREN = "children"
    const val CHILD_CREATE = "children/create"
    const val CHILD_EDIT = "children/{childId}/edit"
    const val CHILD_DETAIL = "children/{childId}"

    const val CHILD_SESSION_HISTORY = "children/{childId}/history"
    const val SESSION_CREATE = "sessions/create/{childId}/{exerciseId}"
    const val SESSION_DETAIL = "sessions/{sessionId}"
    const val CHILD_SESSION_CREATE = "children/{childId}/session-create"

    const val EXERCISES = "exercises"
    const val EXERCISE_DETAIL = "exercises/{exerciseId}"
    const val EXERCISE_CREATE = "exercises/create"
    const val EXERCISE_EDIT = "exercises/{exerciseId}/edit"

    const val DASHBOARD = "dashboard"

    const val PROFILE = "profile"

    fun childDetail(childId: Int) = "children/$childId"
    fun childEdit(childId: Int) = "children/$childId/edit"
    fun childSessionHistory(childId: Int) = "children/$childId/history"
    fun sessionCreate(childId: Int, exerciseId: Int) = "sessions/create/$childId/$exerciseId"
    fun sessionDetail(sessionId: Int) = "sessions/$sessionId"
    fun exerciseDetail(exerciseId: Int) = "exercises/$exerciseId"
    fun childSessionCreate(childId: Int) = "children/$childId/session-create"
    fun exerciseEdit(exerciseId: Int) = "exercises/$exerciseId/edit"
}
