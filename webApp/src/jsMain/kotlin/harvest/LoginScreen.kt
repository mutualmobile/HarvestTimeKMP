package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import kotlinx.coroutines.*
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.html.js.onChangeFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import styled.css
import styled.styledDiv

external interface LoginProps : Props

val JSLoginScreen = fc<LoginProps> {
    var message by useState("")
    var state by useState<DataState>()
    var email by useState("")
    var password by useState("")
    val dataModel = LoginDataModel(onDataState = { stateNew ->
        state = stateNew
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is LoginDataModel.SuccessState -> {
                val response = stateNew.loginResponse
                message = "Logged In!"
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
        MainScope().launch {
            dataModel.activate()
        }
    }

    h1 {
        +"Login Form"
    }
    h1 {
        +"Login Status :"
        +message
    }

    styledDiv{
        css {
            display = Display.flex
            flexDirection = FlexDirection.column
        }

        div{
            input {
                attrs{
                    placeholder = "Enter email address"
                    value = email
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        email = target.value
                    }
                }
            }

            input {
                attrs{
                    placeholder = "Enter Password"
                    value = password
                    onChangeFunction = {
                        val target = it.target as HTMLInputElement
                        password = target.value
                    }
                }
            }

            button {
                +"Login!"

                attrs {
                    onClick = {
                        dataModel.login(email, password)
                    }
                }
            }
        }


    }

}

