package com.mutualmobile.harvestKmp.data.network

object Endpoint {
    const val SPRING_BOOT_BASE_URL = "https://harvestkmp.mmharvest.com"

    private const val API_URL = "/api/v1"
    private const val ADMIN = "/admin"
    private const val ORG_ADMIN = "/org-admin"
    const val UN_AUTH_API = "$API_URL/public"
    const val ORGANIZATIONS = "$UN_AUTH_API/organizations"
    const val UN_AUTH_ORGANISATION = "$UN_AUTH_API/organization"

    const val SIGNUP = "$UN_AUTH_API/signup"
    const val LOGIN = "$UN_AUTH_API/login"
    const val FORGOT_PASSWORD = "$UN_AUTH_API/forgotPassword"
    const val RESET_PASSWORD_ENDPOINT = "$UN_AUTH_API/resetPassword"
    const val SERVICE_LOCATIONS = "$UN_AUTH_API/locations"
    const val EMAIL_VERIFY = "/emailVerify"
    const val FCM_TOKEN = "$API_URL/fcmToken"
    const val CHANGE_PASSWORD = "$API_URL/changePassword"
    const val LOGOUT = "$API_URL/logout"
    const val REFRESH_TOKEN = "$API_URL/refreshToken"

    const val USER = "$API_URL/user"
    private const val USER_PROJECT = "$USER/project"

    const val NOTIFICATIONS = "$API_URL/notifications"
    const val NOTIFICATION_COUNT = "$API_URL/notificationCount"

    const val ORGANIZATION = "$API_URL/organization"
    const val ORG_USERS = "$API_URL/organization/users"
    const val ORG_USER = "$API_URL/organization/user"
    const val TIME_ENTRIES = "$API_URL/organization-project-user/times"
    const val TIME_ENTRY = "$API_URL/organization-project-user/time"
    const val ORG_PROJECT = "$ORGANIZATION/project"

    const val LIST_USERS_IN_PROJECT = "$ORG_PROJECT/list-users"

    //ADMIN
    const val LIST_USERS = "$API_URL$ADMIN/users"
    const val ASSIGN_PROJECT = "$API_URL$ORG_ADMIN/assign-user-project"

    const val LOG_WORK = "$USER_PROJECT/log-work"


    object Params {
        const val START_DATE = "startDate"
        const val END_DATE = "endDate"
        const val EMAIL = "email"
        const val TOKEN = "token"
        const val PASSWORD = "password"
        const val FILE = "file"

        const val OFFSET: String = "offset"
        const val LIMIT: String = "limit"
        const val TYPE: String = "type"
        const val STATUS: String = "status"
        const val USER_ID: String = "userId"

        const val ID: String = "id"

        //filtering
        const val SEARCH_KEY = "search"

        //sorting
        const val SORT_BY = "sortBy"
        const val SORT_ORDER = "sortOrder"

        const val NOTIFICATION_ID = "notificationId"

        const val ORG_IDENTIFIER = "identifier"
    }
}
