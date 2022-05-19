import androidx.compose.runtime.mutableStateOf
import com.mutualmobile.harvestKmp.features.trending.GithubTrendingDataModel

class TrendingReposVM {

  var uiState = mutableStateOf<GithubTrendingDataModel.DataState>(GithubTrendingDataModel.EmptyState)
    private set

  private val trendingDataModel = GithubTrendingDataModel(onDataState = { stateNew ->
    uiState.value = stateNew
  })

  init {
    trendingDataModel.activate()
  }
}
