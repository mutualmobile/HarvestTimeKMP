package orgadmin

import com.mutualmobile.harvestKmp.data.network.UserRole
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.domain.model.response.OrgProjectResponse
import com.mutualmobile.harvestKmp.features.harvest.FindProjectsInOrgDataModel
import com.mutualmobile.harvestKmp.features.harvest.FindUsersInOrgDataModel
import react.VFC
import react.useEffectOnce
import react.useState

val JsProjectAssignScreen = VFC {
    val selectionInfo = useState<Map<String, List<String>>>()
    var projects by useState<List<OrgProjectResponse>>()
    val limit = 20
    var currentPage by useState(0)
    var totalPages by useState(0)
    var isLoading by useState(false)
    val userType = UserRole.ORG_USER.role

    val onDataState = { newData: DataState ->

    }

    val findProjectsInOrgDataModel = FindProjectsInOrgDataModel(onDataState = onDataState)
    val usersInOrgDataModel = FindUsersInOrgDataModel(onDataState = onDataState)

    useEffectOnce {
        findProjectsInOrgDataModel.activate()
        usersInOrgDataModel.activate()

        findProjectsInOrgDataModel.findProjectInOrg(
            offset = currentPage, limit = limit, orgId = null
        )
        usersInOrgDataModel.findUsers(
            userType = userType.toInt(),
            orgIdentifier = null, isUserDeleted = false,
            0, limit
        )
    }
}