package com.giia.lapiz_inteligente.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import com.giia.lapiz_inteligente.data.repository.AuthRepository
import com.giia.lapiz_inteligente.ui.auth.LoginScreen
import com.giia.lapiz_inteligente.ui.auth.RegisterScreen
import com.giia.lapiz_inteligente.ui.children.ChildCreateScreen
import com.giia.lapiz_inteligente.ui.children.ChildEditScreen
import com.giia.lapiz_inteligente.ui.children.ChildrenListScreen
import com.giia.lapiz_inteligente.ui.dashboard.DashboardScreen
import com.giia.lapiz_inteligente.ui.dashboard.MainMenuScreen
import com.giia.lapiz_inteligente.ui.exercises.ExerciseDetailScreen
import com.giia.lapiz_inteligente.ui.exercises.ExercisesScreen
import com.giia.lapiz_inteligente.ui.sessions.SessionDetailScreen
import com.giia.lapiz_inteligente.ui.sessions.SessionHistoryScreen
import com.giia.lapiz_inteligente.ui.sessions.SessionsScreen
import com.giia.lapiz_inteligente.ui.splash.SplashScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    authRepository: AuthRepository
) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.HOME) {
            val scope = rememberCoroutineScope()
            MainMenuScreen(
                onLogout = {
                    scope.launch {
                        authRepository.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                },
                onNavigateToChildren = {
                    navController.navigate(Routes.CHILDREN)
                },
                onNavigateToExercises = {
                    navController.navigate(Routes.EXERCISES)
                },
                onNavigateToCreateSession = {
                    navController.navigate(Routes.CREATE_SESSION)
                },
                onNavigateToSessionHistory = {
                    navController.navigate(Routes.SESSION_HISTORY)
                },
                onNavigateToDashboard = {
                    navController.navigate(Routes.DASHBOARD)
                }
            )
        }
        composable(Routes.CHILDREN) {
            ChildrenListScreen(
                onNavigateToAdd = {
                    navController.navigate(Routes.ADD_CHILD)
                },
                onNavigateToEdit = { child ->
                    val encodedName = java.net.URLEncoder.encode(child.name, "UTF-8")
                    navController.navigate(
                        Routes.EDIT_CHILD
                            .replace("{childId}", child.child_id.toString())
                            .replace("{childName}", encodedName)
                    )
                }
            )
        }
        composable(Routes.ADD_CHILD) {
            ChildCreateScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Routes.EDIT_CHILD,
            arguments = listOf(
                navArgument("childId") { type = NavType.IntType },
                navArgument("childName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val childId = backStackEntry.arguments?.getInt("childId") ?: return@composable
            val encodedName = backStackEntry.arguments?.getString("childName") ?: ""
            val childName = java.net.URLDecoder.decode(encodedName, "UTF-8")
            ChildEditScreen(
                childId = childId,
                currentName = childName,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.EXERCISES) {
            ExercisesScreen(
                onNavigateToDetail = { exerciseId ->
                    navController.navigate(
                        Routes.EXERCISE_DETAIL.replace("{exerciseId}", exerciseId.toString())
                    )
                }
            )
        }
        composable(
            route = Routes.EXERCISE_DETAIL,
            arguments = listOf(
                navArgument("exerciseId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: return@composable
            ExerciseDetailScreen(
                exerciseId = exerciseId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.CREATE_SESSION) {
            SessionsScreen(
                onSessionCreated = { sessionId ->
                    navController.navigate(
                        Routes.SESSION_DETAIL.replace("{sessionId}", sessionId.toString())
                    ) {
                        popUpTo(Routes.CREATE_SESSION) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Routes.SESSION_DETAIL,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: return@composable
            SessionDetailScreen(
                sessionId = sessionId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSessionEnded = {
                    navController.navigate(Routes.SESSION_HISTORY) {
                        popUpTo(Routes.HOME)
                    }
                }
            )
        }
        composable(Routes.SESSION_HISTORY) {
            SessionHistoryScreen()
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSessionMetrics = { sessionId ->
                    navController.navigate(
                        Routes.SESSION_METRICS.replace("{sessionId}", sessionId.toString())
                    )
                }
            )
        }
        composable(
            route = Routes.SESSION_METRICS,
            arguments = listOf(
                navArgument("sessionId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getInt("sessionId") ?: return@composable
            SessionDetailScreen(
                sessionId = sessionId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSessionEnded = {
                    navController.popBackStack(Routes.DASHBOARD, inclusive = false)
                }
            )
        }
    }
}
