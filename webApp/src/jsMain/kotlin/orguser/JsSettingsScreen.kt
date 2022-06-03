package orguser

import harvest.ChangePasswordUI
import mui.material.Box
import mui.material.ListItem
import mui.material.ListItemText
import mui.material.List
import react.ReactNode
import react.VFC
import react.router.useNavigate
import react.useState

val JsSettingsScreen = VFC {
    var message by useState("")
    var changePassword by useState(false)
    val navigate = useNavigate()

    Box{
        List {
            ListItem {
                ListItemText {
                    primary = ReactNode("Change Password")
                    secondary =
                        ReactNode("Recommended: Update your password regularly or use a secure password manager")
                    onClick = {
                        changePassword = true
                    }
                }
            }
        }

        ChangePasswordUI{
            drawerOpen = changePassword
            onOpen = {
                changePassword = true
            }
            onClose = {
                changePassword = false
            }
        }

    }
}
