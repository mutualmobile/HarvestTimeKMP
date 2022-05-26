package com.mutualmobile.harvestKmp.di


import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocal
import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocalImpl
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
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.component.get
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun initSharedDependencies() = startKoin {
    modules(commonModule, localDBRepos, useCaseModule, platformModule())
}

fun initSqlDelightExperimentalDependencies() = startKoin {
    modules(commonModule, jsSqliteDeps, useCaseModule, platformModule())
}

val jsSqliteDeps = module {
    single<GithubTrendingLocal> { GithubTrendingLocalImpl() }
}

val localDBRepos = module {
    single<GithubTrendingLocal> { GithubTrendingLocalImpl(get()) }
}


val commonModule = module {
    single { httpClient(get(), get(), get()) }
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
    single { FindOrgByIdentifierUseCase(get()) }
    single { LogoutUseCase(get(), get()) }
    single { ChangePasswordUseCase(get()) }
    single { FcmTokenUseCase(get()) }
    single { ForgotPasswordUseCase(get()) }
    single { ResetPasswordUseCase(get()) }
    single { GetUserUseCase(get()) }
    single { SaveSettingsUseCase(get()) }
    single { CurrentUserLoggedInUseCase(get()) }
    single { CreateProjectUseCase(get()) }
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
    fun provideCreateProjectUseCase(): CreateProjectUseCase = get()
    fun provideFindUsersByOrgUseCase(): FindUsersInOrgUseCase = get()
}

class SharedComponent : KoinComponent {
    fun provideGithubTrendingAPI(): GithubTrendingAPI = get()
    fun provideGithubTrendingLocal(): GithubTrendingLocal = get()
}

private fun httpClient(
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
                this.loadTokens {
                    BearerTokens(
                        settings.getString(Constants.JWT_TOKEN, ""),
                        settings.getString(Constants.REFRESH_TOKEN, "")
                    )
                }
                this.refreshTokens {
                    val oldRefreshToken = this.oldTokens?.refreshToken
                    val refreshTokens =
                        this.client.post("${Endpoint.SPRING_BOOT_BASE_URL}$REFRESH_TOKEN") {
                            contentType(ContentType.Application.Json)
                            setBody(LoginResponse(refreshToken = oldRefreshToken))
                        }.body<LoginResponse>()
                    saveSettingsUseCase.invoke(refreshTokens.token, refreshTokens.refreshToken)
                    BearerTokens(
                        settings.getString(Constants.JWT_TOKEN, ""),
                        settings.getString(Constants.REFRESH_TOKEN, "")
                    )
                }
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
