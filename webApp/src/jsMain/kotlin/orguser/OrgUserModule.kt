package orguser

import react.FC
import react.PropsWithChildren
import react.createContext

val DrawerItemsContext = createContext<DrawerItems>()

val DrawerItemsModule = FC<PropsWithChildren> { props ->
    val users = useDrawerItems()
    DrawerItemsContext.Provider {
        value = users
        +props.children
    }

}