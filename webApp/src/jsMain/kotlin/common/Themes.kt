package common

import kotlinx.js.jso
import mui.material.PaletteMode.dark
import mui.material.PaletteMode.light
import mui.material.styles.ThemeOptions
import mui.material.styles.createTheme

object Themes {
    val Light = createTheme(
        jso {
            palette = jso {
                mode = light
                primary = jso {
                    main = "#fa5d00"
                }
                secondary = jso{
                    main = "#f50057"
                }
            }

        }
    )

    val Dark = createTheme(
        jso {
            palette = jso {
                mode = dark
                primary = jso {
                    main = "#fa5d00"
                }
                secondary = jso{
                    main = "#f50057"
                }
            }
        }
    )
}