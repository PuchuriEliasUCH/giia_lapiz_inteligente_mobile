package com.giia.lapiz_inteligente.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.giia.lapiz_inteligente.data.datastore.SessionManager
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.giia.lapiz_inteligente.data.repository.AuthRepository
import com.giia.lapiz_inteligente.ui.auth.LoginScreen
import com.giia.lapiz_inteligente.ui.auth.RegisterScreen
import com.giia.lapiz_inteligente.ui.children.ChildCreateScreen
import com.giia.lapiz_inteligente.ui.children.ChildDetailScreen
import com.giia.lapiz_inteligente.ui.children.ChildEditScreen
import com.giia.lapiz_inteligente.ui.children.ChildrenListScreen
import com.giia.lapiz_inteligente.ui.components.GraphiCareBottomBar
import com.giia.lapiz_inteligente.ui.dashboard.DashboardScreen
import com.giia.lapiz_inteligente.ui.exercises.ExerciseCreateScreen
import com.giia.lapiz_inteligente.ui.exercises.ExerciseDetailScreen
import com.giia.lapiz_inteligente.ui.exercises.ExercisesScreen
import com.giia.lapiz_inteligente.ui.profile.ProfileScreen
import com.giia.lapiz_inteligente.ui.sessions.SessionCreateScreen
import com.giia.lapiz_inteligente.ui.sessions.SessionDetailScreen
import com.giia.lapiz_inteligente.ui.splash.SplashScreen
import kotlinx.coroutines.launch

private val bottomNavRoutes = setOf(
    Routes.DASHBOARD,
    Routes.CHILDREN,
    Routes.PROFILE
)

@Composable
fun NavGraph(
    navController: NavHostController,
    authRepository: AuthRepository,
    sessionManager: SessionManager
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                GraphiCareBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(Routes.DASHBOARD) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen(
                    onNavigateToLogin = {
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    },
                    onNavigateToMain = {
                        navController.navigate(Routes.DASHBOARD) {
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
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            restoreState = false
                        }
                    }
                )
            }

            composable(Routes.REGISTER) {
                RegisterScreen(
                    onNavigateToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate(Routes.DASHBOARD) {
                            popUpTo(Routes.LOGIN) { inclusive = true }
                            restoreState = false
                        }
                    }
                )
            }

            composable(Routes.DASHBOARD) {
                val userName by sessionManager.userName.collectAsState(initial = null)
                DashboardScreen(
                    userName = userName,
                    onNavigateToSessionDetail = { sessionId ->
                        navController.navigate(Routes.sessionDetail(sessionId))
                    }
                )
            }

            composable(Routes.CHILDREN) {
                ChildrenListScreen(
                    onNavigateToAdd = {
                        navController.navigate(Routes.CHILD_CREATE)
                    },
                    onNavigateToDetail = { childId ->
                        navController.navigate(Routes.childDetail(childId))
                    },
                    onNavigateToEdit = { childId ->
                        navController.navigate(Routes.childEdit(childId))
                    }
                )
            }

            composable(Routes.PROFILE) {
                val scope = rememberCoroutineScope()
                ProfileScreen(
                    onLogout = {
                        scope.launch {
                            authRepository.logout()
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                                restoreState = false
                            }
                            navController.graph.clear()
                        }
                    }
                )
            }

            composable(Routes.CHILD_CREATE) {
                ChildCreateScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Routes.CHILD_EDIT,
                arguments = listOf(
                    navArgument("childId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val childId = backStackEntry.arguments?.getInt("childId") ?: return@composable
                ChildEditScreen(
                    childId = childId,
                    currentName = "",
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Routes.CHILD_DETAIL,
                arguments = listOf(
                    navArgument("childId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val childId = backStackEntry.arguments?.getInt("childId") ?: return@composable
                ChildDetailScreen(
                    childId = childId,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToEdit = {
                        navController.navigate(Routes.childEdit(childId))
                    },
                    onNavigateToExercises = { _, exerciseId ->
                        navController.navigate(Routes.sessionCreate(childId, exerciseId))
                    },
                    onNavigateToSessionDetail = { sessionId ->
                        navController.navigate(Routes.sessionDetail(sessionId))
                    },
                    onStartNewSession = {
                        navController.navigate(Routes.childSessionCreate(childId))
                    }
                )
            }

            composable(
                route = Routes.CHILD_SESSION_CREATE,
                arguments = listOf(
                    navArgument("childId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val childId = backStackEntry.arguments?.getInt("childId") ?: return@composable
                ExercisesScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = {},
                    onNavigateToSessionCreate = { exerciseId ->
                        navController.navigate(Routes.sessionCreate(childId, exerciseId)) {
                            popUpTo(Routes.childSessionCreate(childId)) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.EXERCISES) {
                ExercisesScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDetail = { exerciseId ->
                        navController.navigate(Routes.exerciseDetail(exerciseId))
                    },
                    onNavigateToCreate = {
                        navController.navigate(Routes.EXERCISE_CREATE)
                    }
                )
            }

            composable(Routes.EXERCISE_CREATE) {
                ExerciseCreateScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onExerciseCreated = { navController.popBackStack() }
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
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToEdit = {
                        navController.navigate(Routes.exerciseEdit(exerciseId))
                    }
                )
            }

            composable(
                route = Routes.EXERCISE_EDIT,
                arguments = listOf(
                    navArgument("exerciseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: return@composable
                ExerciseDetailScreen(
                    exerciseId = exerciseId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.SESSION_CREATE,
                arguments = listOf(
                    navArgument("childId") { type = NavType.IntType },
                    navArgument("exerciseId") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val childId = backStackEntry.arguments?.getInt("childId") ?: return@composable
                val exerciseId = backStackEntry.arguments?.getInt("exerciseId") ?: return@composable
                SessionCreateScreen(
                    childId = childId,
                    exerciseId = exerciseId,
                    onSessionStarted = { sessionId ->
                        navController.navigate(Routes.sessionDetail(sessionId)) {
                            popUpTo(Routes.sessionCreate(childId, exerciseId)) {
                                inclusive = true
                            }
                        }
                    },
                    onNavigateBack = {
                        navController.popBackStack()
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
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
