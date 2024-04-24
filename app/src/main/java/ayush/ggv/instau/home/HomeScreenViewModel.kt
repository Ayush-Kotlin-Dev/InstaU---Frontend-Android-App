package ayush.ggv.instau.home

import ayush.ggv.instau.common.fakedata.Post

class HomeScreenViewModel {
}

data class PostsUiState(
    val isLoading : Boolean = false,
    val posts : List<Post>
)