package components

import common.Themes
import mui.material.CssBaseline
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import react.*

typealias ThemeState = StateInstance<Theme>

val AppThemeContext = createContext<ThemeState>()

val ThemeModule = FC<PropsWithChildren> { props ->
    val state = useState(Themes.Light)
    val (theme) = state
    AppThemeContext.Provider {
        value = state
        ThemeProvider {
            this.theme = theme
            CssBaseline()
            +props.children
        }
    }
}