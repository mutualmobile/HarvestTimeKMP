package orguser

import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.CreateProjectResponse
import com.mutualmobile.harvestKmp.domain.model.response.FindUsersInOrgResponse
import com.mutualmobile.harvestKmp.features.harvest.CreateProjectDataModel
import com.mutualmobile.harvestKmp.features.harvest.FindUsersInOrgDataModel
import csstype.*
import kotlinx.js.jso
import mui.icons.material.Add
import mui.material.*
import mui.system.sx
import react.*

val JsOrgUsersScreen = VFC {
    var message by useState("")
    var users by useState<List<FindUsersInOrgResponse>>()
    var currentPage by useState(0)
    val limit = 10

    val dataModel = FindUsersInOrgDataModel(onDataState = { stateNew ->
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                message = try {
                    val response = (stateNew.data as ApiResponse<List<FindUsersInOrgResponse>>)
                    users = response.data
                    response.message ?: "Some message"
                } catch (ex: Exception) {
                    ex.message ?: ""
                }
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
        dataModel.activate()
        dataModel.findUsers(
            userType = 2, orgIdentifier = null, isUserDeleted = false,
            currentPage, limit
        )
    }

    Box {
        sx {
            position = Position.relative
            transform = translatez(0.px)
            flexGrow = number(1.0)
            alignSelf = AlignSelf.flexEnd
            alignItems = AlignItems.baseline
        }
        Pagination {
            //count={data.sub.length%10===0 ? data.sub.length/10 : data.sub.length/10 +1} page={page} onChange={(event,val)=> setPage(val)}
            count =
                if (((users?.size ?: 0) % limit) == 0) ((users?.size
                    ?: 0) / limit) else ((users?.size ?: 0) / limit + 1)
            page = currentPage
            onChange = { event, value ->
                page = value
                dataModel.findUsers(
                    userType = 2, // TODO extract user role as const
                    orgIdentifier = null, isUserDeleted = false,
                    currentPage, limit
                )
            }

        }
        List {
            users?.map { user ->
                ListItem {
                    ListItemText {
                        primary = ReactNode("${user.firstName ?: ""} ${user.lastName ?: ""}")
                        secondary =
                            ReactNode("${user.email}")
                    }
                }
            }
        }
    }
    Fab {
        variant = FabVariant.extended
        sx {
            transform = translatez(0.px)
            position = Position.absolute
            bottom = 16.px
            right = 16.px
        }
        color = FabColor.primary
        Add()
        onClick = {

        }
        +"Create User"
    }
}