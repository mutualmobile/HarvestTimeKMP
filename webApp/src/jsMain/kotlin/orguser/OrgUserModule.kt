package orguser

import react.FC
import react.PropsWithChildren
import react.createContext

val OrgUserDrawerItemsContext = createContext<DrawerItems>()

val OrgUserDrawerItemsModule = FC<PropsWithChildren> { props ->
    val users = useOrgUserDrawerItems()

    OrgUserDrawerItemsContext.Provider {
        value = users
        +props.children
    }

}