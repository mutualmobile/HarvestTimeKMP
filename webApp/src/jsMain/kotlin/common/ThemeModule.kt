package common

import mui.material.CssBaseline
import mui.material.styles.ThemeProvider
import react.*

val ThemeModule = FC<PropsWithChildren> { props ->
    ThemeProvider {
        this.theme = Themes.Dark

        CssBaseline()
        +props.children
    }
}