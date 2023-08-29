package kr.jm.composewebview

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.flow.collectLatest
import kr.jm.composewebview.ui.theme.ComposeWebViewTheme

class MainActivity : ComponentActivity() {

    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeWebViewTheme {

                MainScreen(viewModel)
            }
        }
    }

    override fun onBackPressed() {
        viewModel.backPressed()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(50.dp),
                actions = {
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.undo() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "back",
                            tint = Color.DarkGray
                        )
                    }
                    IconButton(onClick = { viewModel.redo() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "forward",
                            tint = Color.DarkGray
                        )
                    }
                }
            )
        }
    ) {
        MyWebView(viewModel, snackbarHostState)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MyWebView(
    viewModel: MainViewModel,
    snackbarHostState: SnackbarHostState
) {

    val webView = rememberWebView()


    LaunchedEffect(Unit) {
        viewModel.undoSharedFlow.collectLatest {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                snackbarHostState.showSnackbar("뒤로 갈 수 없습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.redoSharedFlow.collectLatest {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                snackbarHostState.showSnackbar("앞으로 갈 수 없습니다.")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.backPressedFlow.collectLatest {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                snackbarHostState.showSnackbar("뒤로 갈 수 없습니다.")
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { webView },
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun rememberWebView(): WebView {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            loadUrl("https://velog.io/@godmin66")
        }
    }
    return webView
}