import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.GithubReposItem
import com.mutualmobile.harvestKmp.features.trending.GithubTrendingDataModel
import csstype.*
import kotlinx.coroutines.*
import mui.material.*
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import mui.material.StackDirection.row
import mui.material.styles.TypographyVariant
import mui.system.responsive
import mui.system.sx
import react.dom.html.ReactHTML.div


val TrendingUI = VFC {
    var trendingRepos: List<GithubReposItem> by useState(emptyList())
    var message: String by useState("")
    var state by useState<DataState>()
    var search by useState("Kotlin")

    val dataModel = GithubTrendingDataModel(onDataState = { stateNew ->
        state = stateNew
        when (stateNew) {
            is LoadingState -> {
                message = "Loading..."
            }
            is SuccessState<*> -> {
                trendingRepos = stateNew.data as List<GithubReposItem>
                message = "Found repos..."
            }
            Complete -> {
                message = "Completed loading!"
            }
            EmptyState -> {
                message = "Emty state"
            }
            is ErrorState -> {
                message = stateNew.throwable.message ?: "Error"
            }
        }
    })

    useEffectOnce {
        MainScope().launch {
            setupDriver()
            message = "Driver created";
            dataModel.activate()
            message = "DataModel activated";
        }
    }

    Box {
        sx { flexGrow = number(1.0) }

        AppBar{
            position = AppBarPosition.static

            Toolbar{
                Typography {
                    sx { flexGrow = number(1.0) }
                    variant = TypographyVariant.h6
                    component = div
                    +"Trending $search Repos"
                }

                Typography {
                    sx { flexGrow = number(1.0) }
                    variant = TypographyVariant.subtitle1
                    component = div
                    +"Status : $message"
                }

                Button {
                    color = ButtonColor.inherit
                    +"Refresh"

                    onClick = {
                        dataModel.refresh()
                    }
                }
            }
        }
    }



    Stack {
        TextField {
            placeholder = "Search by language..."
            value = search
            onChange = {
                val target = it.target as HTMLInputElement
                search = target.value
            }
        }

        Button {
            +"Search Now"

            onClick = {
                dataModel.filterRecords(search)
            }
        }

    }

    Stack {
        for (repo in trendingRepos) {
            Card {
                Stack {
                    this.direction = responsive(row)
                    Avatar {
                        sx {
                            margin = Margin(12.px, 12.px)
                        }
                        src = repo.avatar
                    }
                    ListItemText {
                        +"${repo.url}"
                        +"${repo.name}: ${repo.author}"
                    }
                }
                onClick = {

                }
            }
        }
    }
}

