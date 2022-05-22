import com.mutualmobile.harvestKmp.datamodel.*
import com.mutualmobile.harvestKmp.domain.model.GithubReposItem
import com.mutualmobile.harvestKmp.features.trending.GithubTrendingDataModel
import csstype.*
import harvest.material.TopAppBar
import kotlinx.coroutines.*
import mui.material.*
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import mui.material.StackDirection.row
import mui.system.responsive
import mui.system.sx


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
            dataModel.activate()
        }
    }

    TopAppBar{
        title = "Trending $search Repos"
        subtitle = "Status : $message"
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
                sx {
                    margin = Margin(12.px, 12.px)
                }
                Stack {
                    this.direction = responsive(row)
                    sx {
                        margin = Margin(12.px, 12.px)
                    }
                    Avatar {
                        src = repo.avatar
                        sx {
                            margin = Margin(2.px, 12.px)
                        }
                    }
                    Stack{
                        this.direction = responsive(StackDirection.column)
                        Typography {
                            +"${repo.url}"
                        }
                        Typography{
                            +"${repo.name}: ${repo.author}"
                        }
                    }
                }
                onClick = {

                }
            }
        }
    }
}

