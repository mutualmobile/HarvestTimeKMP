package harvest

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.features.harvest.LoginDataModel
import kotlinx.coroutines.*
import kotlinx.css.Display
import kotlinx.css.FlexDirection
import kotlinx.css.display
import kotlinx.css.flexDirection
import kotlinx.html.js.onChangeFunction
import mui.material.*
import mui.system.ResponsiveStyleValue
import mui.system.responsive
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
        val scope = MainScope()
        scope.launch {
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

    Box{
        Card {


            Stack {
                this.attrs.spacing = responsive(8)
                TextField {
                    this.attrs.variant = FormControlVariant.outlined
                    this.attrs.value = email
                    this.attrs.onChange = {
                        val target = it.target as HTMLInputElement
                        email = target.value
                    }
                    this.attrs.placeholder = "Enter email address"
                }


                TextField {
                    this.attrs.variant = FormControlVariant.outlined
                    this.attrs.value = password
                    this.attrs.onChange = {
                        val target = it.target as HTMLInputElement
                        password = target.value
                    }
                    this.attrs.placeholder = "Enter Password"
                }

                if(state is LoadingState){
                    CircularProgress()
                }

                Button {
                    this.attrs.onClick = {
                        dataModel.login(email, password)
                    }
                    +"Login"
                }
            }


        }
    }

}

