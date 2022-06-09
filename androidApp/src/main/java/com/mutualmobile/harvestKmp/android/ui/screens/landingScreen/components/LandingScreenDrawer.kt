package com.mutualmobile.harvestKmp.android.ui.screens.landingScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mutualmobile.harvestKmp.MR
import com.mutualmobile.harvestKmp.datamodel.DataState
import com.mutualmobile.harvestKmp.datamodel.EmptyState
import com.mutualmobile.harvestKmp.datamodel.SuccessState
import com.mutualmobile.harvestKmp.domain.model.request.HarvestOrganization
import com.mutualmobile.harvestKmp.domain.model.response.ApiResponse
import com.mutualmobile.harvestKmp.domain.model.response.GetUserResponse
import com.mutualmobile.harvestKmp.features.datamodels.authApiDataModels.GetUserDataModel
import com.mutualmobile.harvestKmp.features.datamodels.orgApiDataModels.FindOrgByIdentifierDataModel

@Composable
fun LandingScreenDrawer(
    navController: NavController,
    currentDrawerScreen: LandingScreenDrawerItemType,
    closeDrawer: () -> Unit,
    onScreenChanged: (LandingScreenDrawerItemType) -> Unit,
    goToSettingsScreen: () -> Unit,
    orgIdentifier: String?,
) {
    var userState: DataState by remember { mutableStateOf(EmptyState) }
    remember {
        mutableStateOf(
            GetUserDataModel { newState ->
                userState = newState
            }.activate()
        )
    }

    var organizationState: DataState by remember { mutableStateOf(EmptyState) }
    val findOrgByIdentifierDataModel by remember {
        mutableStateOf(
            FindOrgByIdentifierDataModel { newState ->
                organizationState = newState
            }.apply {
                activate()
            }
        )
    }

    LaunchedEffect(userState) {
        if (userState is SuccessState<*>) {
            orgIdentifier?.let { nnOrganizationName ->
                println("Org is: $nnOrganizationName")
                findOrgByIdentifierDataModel.findOrgByIdentifier(identifier = nnOrganizationName)
            }
        }
    }

    UserInfoSection(
        userName = (userState as? SuccessState<GetUserResponse>)?.data?.firstName,
        organisationName = (organizationState as? SuccessState<ApiResponse<HarvestOrganization>>)?.data?.data?.name
    )

    LandingScreenDrawerItemType.values().forEach { drawerItem ->
        LandingScreenDrawerItem(
            itemType = drawerItem,
            isSelected = currentDrawerScreen == drawerItem
        ) {
            onScreenChanged(drawerItem)
            closeDrawer()
        }
    }

    Divider(
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(horizontal = 8.dp),
        color = Color.White.copy(alpha = 0.075f),
        thickness = 0.6.dp
    )

    SettingsButton(goToSettingsScreen = goToSettingsScreen)
}

@Composable
private fun SettingsButton(goToSettingsScreen: () -> Unit) {
    Text(
        text = stringResource(id = MR.strings.drawer_settings_btn_txt.resourceId),
        style = MaterialTheme.typography.subtitle2.copy(
            color = MaterialTheme.colors.surface.copy(alpha = 0.6f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { goToSettingsScreen() }
            .padding(16.dp),
    )
}
