package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import kotlinx.coroutines.*
import mui.material.*
import mui.system.responsive
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.router.useNavigate

val JSLoginScreen = VFC {
    var message by useState("")
    var email by useState("")
    var state by useState<DataState>()
    var password by useState("")
    val mainScope = MainScope()

    val dataModel = LoginDataModel(onDataState = { stateNew ->
        state = stateNew
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                val response = stateNew.data
                message = "Logged In!"
                val navigator = useNavigate()
                navigator.invoke(to = "/trendingui")
            }
            Complete -> {
                message = "Completed loading!"
            }
            EmptyState -> {
                message = "Empty state"
            }
            is ErrorState -> {
                message = stateNew.throwable.message ?: "Error"
            }
        }
    })

    useEffectOnce {
        mainScope.launch {
            dataModel.activate()
        }
    }

    Typography{
        this.align = TypographyAlign.center
        +"Login Form"
    }

    Typography{
        this.align = TypographyAlign.center
        +"Login Status: "
        +message

    }

    Box{
        Card {


            Stack {
                this.spacing = responsive(8)
                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = email
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        email = target.value
                    }
                    this.placeholder = "Enter email address"
                }


                TextField {
                    this.variant = FormControlVariant.outlined
                    this.value = password
                    this.onChange = {
                        val target = it.target as HTMLInputElement
                        password = target.value
                    }
                    this.placeholder = "Enter Password"
                }



                Button {
                    this.onClick = {
                        mainScope.launch {
                            dataModel.login(email, password)
                        }
                    }
                    +"Login"
                }
            }


        }
    }

}

