package project

import com.mutualmobile.harvestKmp.datamodel.Routes
import com.mutualmobile.harvestKmp.features.harvest.GetUserDataModel
import harvest.material.TopAppBar
import mui.material.Box
import mui.material.Typography
import react.VFC
import react.router.dom.useSearchParams
import react.useEffectOnce

val JSOrgProjectUsersList = VFC {

    val searchParams = useSearchParams()

    val projectId: String? = searchParams.component1().get(Routes.Keys.id)

    val dataModel = GetUserDataModel(onDataState = {

    })

    useEffectOnce {
        dataModel.activate()
        dataModel.getlistofUsers(projectId)
    }


    Box{
        TopAppBar{
            title = "Project users"
            subtitle = "20 people!"
        }
    }



}