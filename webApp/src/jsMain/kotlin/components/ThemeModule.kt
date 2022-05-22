package components

import common.Themes
import mui.material.CssBaseline
import mui.material.styles.ThemeProvider
import mui.system.Theme
import react.*

typealias ThemeState = StateInstance<Theme>
val AppThemeContext = createContext<ThemeState>()

// TODO figure why AppThemeContext is
/**
 * Unresolved reference. None of the following candidates is applicable because of receiver type mismatch:
public open operator fun <P : Props> ElementType<TypeVariable(P)>.invoke(): Unit defined in react.ChildrenBuilder
public open operator fun <P : Props> ElementType<TypeVariable(P)>.invoke(block: TypeVariable(P).() -> Unit): Unit where P : ChildrenBuilder defined in react.ChildrenBuilder
public open operator fun <T> Provider<TypeVariable(T)>.invoke(value: TypeVariable(T), block: ChildrenBuilder.() -> Unit): Unit defined in react.ChildrenBuilder

 */
val ThemeModule = FC<PropsWithChildren> { props ->
    val state = useState(Themes.Light)
    val (theme) = state

    /*AppThemeContext(state) {
        ThemeProvider {
            this.theme = theme

            CssBaseline()
            +props.children
        }
    }*/

    ThemeProvider {
        this.theme = theme

        CssBaseline()
        +props.children
    }
}