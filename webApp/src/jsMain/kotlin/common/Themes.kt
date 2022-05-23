package common

import kotlinx.js.jso
import mui.material.PaletteMode.dark
import mui.material.PaletteMode.light
import mui.material.styles.ThemeOptions
import mui.material.styles.TypeBackground
import mui.material.styles.createTheme

object Themes {
    val Light = createTheme(
        jso {
            palette = jso {
                mode = light
                background = jso {
                    paper = "#33fa5d00"
                }
                primary = jso {
                    main = "#fa5d00"
                }
                secondary = jso {
                    main = "#f50057"
                }
            }

        }
    )

    val Dark = createTheme(
        jso {
            palette = jso {
                mode = dark
                background = jso {
                    paper = "#33fa5d00"
                }
                primary = jso {
                    main = "#fa5d00"
                }
                secondary = jso {
                    main = "#f50057"
                }
            }
        }
    )
}