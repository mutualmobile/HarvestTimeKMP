package com.mutualmobile.harvestKmp.di

import com.mutualmobile.harvestKmp.data.local.HarvestUserLocal
import com.mutualmobile.harvestKmp.data.local.HarvestUserLocalImpl
import com.mutualmobile.harvestKmp.data.network.Constants
import com.mutualmobile.harvestKmp.data.network.Endpoint
import com.mutualmobile.harvestKmp.data.network.Endpoint.REFRESH_TOKEN
import com.mutualmobile.harvestKmp.data.network.authUser.AuthApi
import com.mutualmobile.harvestKmp.data.network.authUser.UserForgotPasswordApi
import com.mutualmobile.harvestKmp.data.network.authUser.impl.AuthApiImpl
import com.mutualmobile.harvestKmp.data.network.authUser.impl.UserForgotPasswordApiImpl
import com.mutualmobile.harvestKmp.data.network.org.OrgApi
import com.mutualmobile.harvestKmp.data.network.org.OrgProjectsApi
import com.mutualmobile.harvestKmp.data.network.org.OrgUsersApi
import com.mutualmobile.harvestKmp.data.network.org.UserProjectApi
import com.mutualmobile.harvestKmp.data.network.org.UserWorkApi
import com.mutualmobile.harvestKmp.data.network.org.impl.OrgApiImpl
import com.mutualmobile.harvestKmp.data.network.org.impl.OrgProjectsApiImpl
import com.mutualmobile.harvestKmp.data.network.org.impl.OrgUsersApiImpl
import com.mutualmobile.harvestKmp.data.network.org.impl.UserProjectApiImpl
import com.mutualmobile.harvestKmp.data.network.org.impl.UserWorkApiImpl
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.domain.usecases.CurrentUserLoggedInUseCase
import com.mutualmobile.harvestKmp.domain.usecases.SaveSettingsUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.ChangePasswordUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.ExistingOrgSignUpUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.FcmTokenUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.GetUserUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.LoginUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.LogoutUseCase
import com.mutualmobile.harvestKmp.domain.usecases.authApiUseCases.NewOrgSignUpUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgApiUseCases.FindOrgByIdUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgApiUseCases.FindOrgByIdentifierUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases.CreateProjectUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases.DeleteProjectUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases.FindProjectsInOrgUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases.GetListOfUsersForAProjectUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases.GetProjectsFromIdsUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgProjectsUseCases.UpdateProjectUseCase
import com.mutualmobile.harvestKmp.domain.usecases.orgUsersApiUseCases.FindUsersInOrgUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userForgotPasswordApiUseCases.ForgotPasswordUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userForgotPasswordApiUseCases.ResetPasswordUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases.AssignProjectsToUsersUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases.DeleteWorkTimeUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases.GetUserAssignedProjectsUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userProjectUseCases.LogWorkTimeUseCase
import com.mutualmobile.harvestKmp.domain.usecases.userWorkUseCases.GetWorkLogsForDateRangeUseCase
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.RefreshTokensParams
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initSharedDependencies() = startKoin {
    modules(
        commonModule,
        networkModule,
        localDBRepos,
        useCaseModule,
        authApiUseCaseModule,
        orgApiUseCaseModule,
        orgProjectsUseCaseModule,
        orgUsersApiUseCaseModule,
        forgotPasswordApiUseCaseModule,
        userProjectUseCaseModule,
        userWorkUseCaseModule,
        platformModule()
    )
}

fun initSqlDelightExperimentalDependencies() = startKoin {
    modules(
        commonModule,
        networkModule,
        jsSqliteDeps,
        useCaseModule,
        authApiUseCaseModule,
        orgApiUseCaseModule,
        orgProjectsUseCaseModule,
        orgUsersApiUseCaseModule,
        forgotPasswordApiUseCaseModule,
        userProjectUseCaseModule,
        userWorkUseCaseModule,
        platformModule()
    )
}

val jsSqliteDeps = module {
    single<HarvestUserLocal> { HarvestUserLocalImpl() }
}

val localDBRepos = module {
    single<HarvestUserLocal> { HarvestUserLocalImpl(get()) }
}

val networkModule = module {
    single {
        httpClient(get(), get(), get())
    }
}

val commonModule = module {
    single<AuthApi> { AuthApiImpl(get()) }
    single<UserForgotPasswordApi> { UserForgotPasswordApiImpl(get()) }
    single<OrgApi> { OrgApiImpl(get()) }
    single<OrgProjectsApi> { OrgProjectsApiImpl(get()) }
    single<OrgUsersApi> { OrgUsersApiImpl(get()) }
    single<UserProjectApi> { UserProjectApiImpl(get()) }
    single<UserWorkApi> { UserWorkApiImpl(get()) }
    single { Settings() }
}

val useCaseModule = module {
    single { SaveSettingsUseCase(get()) }
    single { CurrentUserLoggedInUseCase(get()) }
}

val authApiUseCaseModule = module {
    single { ChangePasswordUseCase(get()) }
    single { ExistingOrgSignUpUseCase(get()) }
    single { FcmTokenUseCase(get()) }
    single { GetUserUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get(), get(), get(), get()) }
    single { NewOrgSignUpUseCase(get()) }
}

val orgApiUseCaseModule = module {
    single { FindOrgByIdentifierUseCase(get()) }
    single { FindOrgByIdUseCase(get()) }
}

val orgProjectsUseCaseModule = module {
    single { CreateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { FindProjectsInOrgUseCase(get()) }
    single { GetListOfUsersForAProjectUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { GetProjectsFromIdsUseCase(get()) }
}

val orgUsersApiUseCaseModule = module {
    single { FindUsersInOrgUseCase(get()) }
}

val forgotPasswordApiUseCaseModule = module {
    single { ForgotPasswordUseCase(get()) }
    single { ResetPasswordUseCase(get()) }
}

val userProjectUseCaseModule = module {
    single { AssignProjectsToUsersUseCase(get()) }
    single { DeleteWorkTimeUseCase(get()) }
    single { GetUserAssignedProjectsUseCase(get()) }
    single { LogWorkTimeUseCase(get()) }
}

val userWorkUseCaseModule = module {
    single { GetWorkLogsForDateRangeUseCase(get()) }
}


class SharedComponent : KoinComponent {
    fun provideHarvestUserLocal(): HarvestUserLocal = get()
    fun provideSettings(): Settings = get()
}

class UseCasesComponent : KoinComponent {
    fun provideSaveSettingsUseCase(): SaveSettingsUseCase = get()
    fun providerUserLoggedInUseCase(): CurrentUserLoggedInUseCase = get()
}

class AuthApiUseCaseComponent : KoinComponent {
    fun provideChangePasswordUseCase(): ChangePasswordUseCase = get()
    fun provideExistingOrgSignUpUseCase(): ExistingOrgSignUpUseCase = get()
    fun provideFcmTokenUseCase(): FcmTokenUseCase = get()
    fun provideGetNetworkUserUseCase(): GetUserUseCase = get()
    fun provideLoginUseCase(): LoginUseCase = get()
    fun provideLogoutUseCase(): LogoutUseCase = get()
    fun provideNewOrgSignUpUseCase(): NewOrgSignUpUseCase = get()
}

class OrgApiUseCaseComponent : KoinComponent {
    fun provideFindOrgByIdentifier(): FindOrgByIdentifierUseCase = get()
    fun provideFindOrgById(): FindOrgByIdUseCase = get()
}

class OrgProjectsUseCaseComponent : KoinComponent {
    fun provideCreateProjectUseCase(): CreateProjectUseCase = get()
    fun provideUpdateProjectUseCase(): UpdateProjectUseCase = get()
    fun provideDeleteProjectUseCase(): DeleteProjectUseCase = get()
    fun provideFindProjectsInOrgUseCase(): FindProjectsInOrgUseCase = get()
    fun provideGetListOfUsersForAProjectUseCase(): GetListOfUsersForAProjectUseCase = get()
    fun provideGetProjectsFromIdsUseCase(): GetProjectsFromIdsUseCase = get()
}

class OrgUsersApiUseCaseComponent : KoinComponent {
    fun provideFindUsersInOrgUseCase(): FindUsersInOrgUseCase = get()
}

class ForgotPasswordApiUseCaseComponent : KoinComponent {
    fun provideForgotPasswordUseCase(): ForgotPasswordUseCase = get()
    fun provideResetPasswordUseCase(): ResetPasswordUseCase = get()
}

class UserProjectUseCaseComponent : KoinComponent {
    fun provideAssignProjectsToUsersUseCase(): AssignProjectsToUsersUseCase = get()
    fun provideLogWorkTimeUseCase(): LogWorkTimeUseCase = get()
    fun provideDeleteWorkTimeUseCase(): DeleteWorkTimeUseCase = get()
    fun provideGetUserAssignedProjectsUseCase(): GetUserAssignedProjectsUseCase = get()
}

class UserWorkUseCaseComponent : KoinComponent {
    fun provideGetWorkLogsForDateRangeUseCase(): GetWorkLogsForDateRangeUseCase = get()
}

fun httpClient(
    httpClientEngine: HttpClientEngine,
    settings: Settings,
    saveSettingsUseCase: SaveSettingsUseCase
) =
    HttpClient(httpClientEngine) {

        install(ContentNegotiation) {
            json(Json {
                isLenient = true; ignoreUnknownKeys = true; prettyPrint = true
            })
        }
        install(Auth) {
            this.bearer {
                sendWithoutRequest { request -> !request.url.encodedPath.startsWith("/public") }
                this.loadTokens {
                    BearerTokens(
                        settings.getString(Constants.JWT_TOKEN, ""),
                        settings.getString(Constants.REFRESH_TOKEN, "")
                    )
                }
                this.refreshTokens {
                    refreshToken(settings, saveSettingsUseCase)
                }
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

private suspend fun RefreshTokensParams.refreshToken(
    settings: Settings,
    saveSettingsUseCase: SaveSettingsUseCase
): BearerTokens {
    try {
        val oldRefreshToken = settings.getString(Constants.REFRESH_TOKEN, "")
        val refreshTokensResponse =
            this.client.post("${Endpoint.SPRING_BOOT_BASE_URL}$REFRESH_TOKEN") {
                contentType(ContentType.Application.Json)
                markAsRefreshTokenRequest()
                setBody(LoginResponse(refreshToken = oldRefreshToken))
            }
        if (refreshTokensResponse.body<String>().isNotEmpty()) {
            val refreshTokens = refreshTokensResponse.body<LoginResponse>()
            saveSettingsUseCase.invoke(
                refreshTokens.token,
                refreshTokens.refreshToken
            )
            BearerTokens(
                settings.getString(Constants.JWT_TOKEN, ""),
                settings.getString(Constants.REFRESH_TOKEN, "")
            )
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
    return BearerTokens(
        "",
        ""
    )
}
