
    override fun activate() {
    }

    override fun destroy() {
        dataModelScope.cancel()
    }

    override fun refresh() {
    }

    fun signUp(
        firstName: String,
        lastName: String,
        company: String,
        email: String,
        password: String
    ) {
        currentLoadingJob?.cancel()
        currentLoadingJob = dataModelScope.launch {
            onDataState(LoadingState)
            when (val signUpResponse = useCasesComponent.provideExistingOrgSignUpUseCase()(firstName, lastName, company, email, password)) {
                is NetworkResponse.Success -> {
                    onDataState(SuccessState(signUpResponse.data))
                    println("SUCCESS ${signUpResponse.data.message}")
                }
                is NetworkResponse.Failure -> {
                    onDataState(ErrorState(signUpResponse.exception))
                    println("FAILED, ${signUpResponse.exception.message}")
                }
            }
        }
    }
}