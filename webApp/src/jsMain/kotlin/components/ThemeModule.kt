package components

import common.Themes
import kotlinx.browser.window
import mui.material.CssBaseline
import mui.material.styles.Theme
import mui.material.styles.ThemeProvider
import org.w3c.dom.MediaQueryList
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import react.*

typealias ThemeState = StateInstance<Theme>

val AppThemeContext = createContext<ThemeState>()

val ThemeModule = FC<PropsWithChildren> { props ->
    // TODO how do we implement this light/dark mode here ?
    val currentTheme = window.matchMedia("(prefers-color-scheme: dark)");
    val state = useState(if (currentTheme.matches) Themes.Dark else Themes.Light)

    val listener = object : EventListener {
        override fun handleEvent(event: Event) {
            if (event is MediaQueryList) {
                val newThemeDark = currentTheme.matches;
                state.component2().invoke(if (newThemeDark) Themes.Dark else Themes.Light)
            }
        }
    }
    currentTheme.addListener(listener)

    useEffect {
        cleanup {
            currentTheme.removeListener(listener)
        }
    }

    AppThemeContext.Provider {
        value = state
        ThemeProvider {
            this.theme = state.component1()
            CssBaseline()
            +props.children
        }
    }
}