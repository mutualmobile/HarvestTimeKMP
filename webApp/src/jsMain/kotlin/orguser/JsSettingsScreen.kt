package orguser

import com.mutualmobile.harvestKmp.datamodel.BROWSER_SCREEN_ROUTE_SEPARATOR
import com.mutualmobile.harvestKmp.datamodel.HarvestRoutes
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
    val navigate = useNavigate()

    Box{
        List {
            ListItem {
                ListItemText {
                    primary = ReactNode("Change Password")
                    secondary =
                        ReactNode("Recommended: Update your password regularly or use a secure password manager")
                    onClick = {
                        navigate(BROWSER_SCREEN_ROUTE_SEPARATOR + HarvestRoutes.Screen.CHANGE_PASSWORD)
                    }
                }
            }
        }

    }
}
