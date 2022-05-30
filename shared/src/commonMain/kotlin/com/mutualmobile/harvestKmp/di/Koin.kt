package com.mutualmobile.harvestKmp.di


import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocal
import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocalImpl
import com.mutualmobile.harvestKmp.data.local.HarvestUserLocal
import com.mutualmobile.harvestKmp.data.local.HarvestUserLocalImpl
import com.mutualmobile.harvestKmp.data.network.*
import com.mutualmobile.harvestKmp.data.network.Endpoint.REFRESH_TOKEN
import com.mutualmobile.harvestKmp.domain.model.response.LoginResponse
import com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBoot.*
import com.mutualmobile.harvestKmp.domain.usecases.trendingrepos.FetchTrendingReposUseCase
import com.mutualmobile.harvestKmp.domain.usecases.trendingrepos.GetLocalReposUseCase
import com.mutualmobile.harvestKmp.domain.usecases.trendingrepos.SaveTrendingReposUseCase
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.logging.*
import org.koin.core.context.startKoin
import org.koin.dsl.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.component.*

fun initSharedDependencies() = startKoin {
    modules(commonModule, networkModule, localDBRepos, useCaseModule, platformModule())
}

fun initSqlDelightExperimentalDependencies() = startKoin {
    modules(commonModule, networkModule, jsSqliteDeps, useCaseModule, platformModule())
}

val jsSqliteDeps = module {
    single<GithubTrendingLocal> { GithubTrendingLocalImpl() }
    single<HarvestUserLocal> { HarvestUserLocalImpl() }
}

val localDBRepos = module {
    single<GithubTrendingLocal> { GithubTrendingLocalImpl(get()) }
    single<HarvestUserLocal> { HarvestUserLocalImpl(get()) }
}

val networkModule = module {
    single {
        httpClient(get(), get(), get())
    }
}

val commonModule = module {
    single<GithubTrendingAPI> { GithubTrendingAPIImpl(get()) }
    single<PraxisSpringBootAPI> { PraxisSpringBootAPIImpl(get()) }
    single { Settings() }
}

val useCaseModule = module {
    single { FetchTrendingReposUseCase(get()) }
    single { SaveTrendingReposUseCase(get()) }
    single { GetLocalReposUseCase(get()) }
    single { LoginUseCase(get()) }
    single { ExistingOrgSignUpUseCase(get()) }
    single { NewOrgSignUpUseCase(get()) }
    single { GetUserUseCase(get()) }
    single { FindOrgByIdentifierUseCase(get()) }
    single { LogoutUseCase(get(), get(), get()) }
    single { ChangePasswordUseCase(get()) }
    single { FcmTokenUseCase(get()) }
    single { ForgotPasswordUseCase(get()) }
    single { ResetPasswordUseCase(get()) }
    single { SaveSettingsUseCase(get()) }
    single { CurrentUserLoggedInUseCase(get()) }
    single { FindProjectsInOrgUseCase(get()) }
    single { CreateProjectUseCase(get()) }
    single { UpdateProjectUseCase(get()) }
    single { DeleteProjectUseCase(get()) }
    single { FindUsersInOrgUseCase(get()) }
}

class UseCasesComponent : KoinComponent {
    fun provideFetchTrendingReposUseCase(): FetchTrendingReposUseCase = get()
    fun provideSaveTrendingReposUseCase(): SaveTrendingReposUseCase = get()
    fun provideGetLocalReposUseCase(): GetLocalReposUseCase = get()
}

class SpringBootAuthUseCasesComponent : KoinComponent {
    fun provideLoginUseCase(): LoginUseCase = get()
    fun provideSaveSettingsUseCase(): SaveSettingsUseCase = get()
    fun provideExistingOrgSignUpUseCase(): ExistingOrgSignUpUseCase = get()
    fun provideNewOrgSignUpUseCase(): NewOrgSignUpUseCase = get()
    fun provideFindOrgByIdentifier(): FindOrgByIdentifierUseCase = get()
    fun provideLogoutUseCase(): LogoutUseCase = get()
    fun provideChangePasswordUseCase(): ChangePasswordUseCase = get()
    fun provideFcmTokenUseCase(): FcmTokenUseCase = get()
    fun provideForgotPasswordUseCase(): ForgotPasswordUseCase = get()
    fun provideResetPasswordUseCase(): ResetPasswordUseCase = get()
    fun provideGetUserUseCase(): GetUserUseCase = get()
    fun providerUserLoggedInUseCase(): CurrentUserLoggedInUseCase = get()
    fun provideFindProjectsInOrgUseCase(): FindProjectsInOrgUseCase = get()
    fun provideCreateProjectUseCase(): CreateProjectUseCase = get()
    fun provideUpdateProjectUseCase(): UpdateProjectUseCase = get()
    fun provideDeleteProjectUseCase(): DeleteProjectUseCase = get()
    fun provideFindUsersByOrgUseCase(): FindUsersInOrgUseCase = get()
}

class SharedComponent : KoinComponent {
    fun provideGithubTrendingAPI(): GithubTrendingAPI = get()
    fun provideGithubTrendingLocal(): GithubTrendingLocal = get()
    fun provideHarvestUserLocal(): HarvestUserLocal = get()
    fun provideSettings(): Settings = get()
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
