package com.mutualmobile.harvestKmp.di


import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPI
import com.mutualmobile.harvestKmp.data.network.PraxisSpringBootAPIImpl
import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocal
import com.mutualmobile.harvestKmp.data.local.GithubTrendingLocalImpl
import com.mutualmobile.harvestKmp.data.network.GithubTrendingAPI
import com.mutualmobile.harvestKmp.data.network.GithubTrendingAPIImpl
import com.mutualmobile.harvestKmp.domain.usecases.praxisSpringBootAuth.*
import com.mutualmobile.harvestKmp.domain.usecases.trendingrepos.FetchTrendingReposUseCase
import com.mutualmobile.harvestKmp.domain.usecases.trendingrepos.GetLocalReposUseCase
import com.mutualmobile.harvestKmp.domain.usecases.trendingrepos.SaveTrendingReposUseCase
import com.russhwolf.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.logging.*
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.core.component.get
import io.ktor.client.plugins.contentnegotiation.*
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
    single { httpClient(get()) }
    single { Settings() }
    single<GithubTrendingAPI> { GithubTrendingAPIImpl(get()) }
    single<PraxisSpringBootAPI> { PraxisSpringBootAPIImpl(get()) }
}

val useCaseModule = module {
    single { FetchTrendingReposUseCase(get()) }
    single { SaveTrendingReposUseCase(get()) }
    single { GetLocalReposUseCase(get()) }
    single { LoginUseCase(get()) }
    single { ExistingOrgSignUpUseCase(get()) }
    single { NewOrgSignUpUseCase(get()) }
    single { FindOrgByIdentifierUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { ChangePasswordUseCase(get()) }
    single { FcmTokenUseCase(get()) }
    single { ForgotPasswordUseCase(get()) }
    single { ResetPasswordUseCase(get()) }
    single { SaveSettingsUseCase(get()) }
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
}

class SharedComponent : KoinComponent {
    fun provideGithubTrendingAPI(): GithubTrendingAPI = get()
    fun provideGithubTrendingLocal(): GithubTrendingLocal = get()
    fun provideSettings(): Settings = get()
}

private fun httpClient(httpClientEngine: HttpClientEngine) = HttpClient(httpClientEngine) {

    install(ContentNegotiation) {
        json(Json {
            isLenient = true; ignoreUnknownKeys = true; prettyPrint = true
        })
    }
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}
