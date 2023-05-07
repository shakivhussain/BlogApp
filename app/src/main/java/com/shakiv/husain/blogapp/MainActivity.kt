package com.shakiv.husain.blogapp

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shakiv.husain.blogapp.ui.theme.BlogAppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            BlogApp()

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun BlogApp() {
    KeyBoardComposable()
    TextField(value = "", onValueChange = {})
}

@Composable
fun KeyBoardComposable(){
    val view = LocalView.current

    DisposableEffect(key1 = Unit){

        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val insets = ViewCompat.getRootWindowInsets(view)
            val isKeyBoardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime())
            Log.d("TAG", "KeyBoardComposable: $isKeyBoardVisible")
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeGlobalOnLayoutListener { listener }
        }
    }
}

@Composable
fun MediaComposable(){
    val context = LocalContext.current
    DisposableEffect(key1 = Unit ){
        val mediaPlayer = MediaPlayer.create(context,R.raw.tone)
        mediaPlayer.start()
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }
}


@Composable
fun DisposableEffectExample(){
    var state = remember { mutableStateOf(false) }
    DisposableEffect(key1 = state.value){
        Log.d("TAG", "DisposableEffectExample: Started")
        onDispose {
            Log.d("TAG", "DisposableEffectExample: onDispose")
        }
    }
    Button(onClick = { state.value=!state.value }) {
        Text(text = "Change State")
    }
}

@Composable
fun RemmemberUpdatedState(){
    var counter = remember { mutableStateOf(0) }
    LaunchedEffect(key1 = Unit){
        delay(2000)
        counter.value=10
    }
    NewCounter(counter.value)
}

@Composable fun NewCounter(value: Int) {
    var state = rememberUpdatedState(newValue = value)
    LaunchedEffect(key1 = Unit){
        delay(5000)
        Log.d("TAG", "NewCounter: ${state.value} ")
    }
    Text(text = value.toString())
}

@Composable
fun CoroutineScopeComposable() {
    val counter = remember { mutableStateOf(0) }
    var scope = rememberCoroutineScope()
    var text = "Counter is running ${counter.value}"
    if (counter.value == 10) {
        text = "Counter Stopped."
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineSmall)

        Button(onClick = {
            scope.launch {

                Log.d("TAG", "CoroutineScopeComposable: Started...")

                try {
                    for (i in 1..10) {
                        Log.d("TAG", "CoroutineScopeComposable: $counter")
                        counter.value++
                        delay(1000)
                    }
                } catch (e: Exception) {
                    Log.e("TAG", "Exception : ${e.message.toString()}:  ")
                }
            }
        }) {

            Text(text = "Start")

        }

    }

}


@Composable
fun LaunchedEffectComposable() {
    var counter = remember { mutableStateOf(0) }
    LaunchedEffect(key1 = Unit) {
        try {
            for (i in 1..10) {
                counter.value++
                delay(1000)
            }
        } catch (e: Exception) {
            Log.e("TAG", "Exception : ${e.message.toString()}:  ")
        }
    }

    Button(onClick = {
        counter.value++
    }) {
        Text(text = "Inc Count : ${counter.value}")
    }
}


@Composable
fun SideEffectExample() {

    /*
*  This function will side effect here because,
*  fetching data inside the composable function
*
*/
    val categoryState = remember { mutableStateOf(emptyList<String>()) }

    // Will Run only one time
    LaunchedEffect(key1 = Unit) {
        categoryState.value = fetchData()
    }

    LazyColumn {

        items(categoryState.value) {
            Text(text = "$it")

        }
    }

}

fun fetchData(): List<String> {
    return listOf<String>("One", "Two", "Three")
}


@Composable
fun App() {

    var theme = remember { mutableStateOf(false) }

    // LocalContext.current.applicationContext

    BlogAppTheme(theme.value) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {

            Text(text = "Shakiv Husain", style = MaterialTheme.typography.titleSmall)

            Button(onClick = {
                theme.value = !theme.value
            }) {
                Text(text = "Theme Change.")
            }
        }

    }
}


var count: Int = 0

@Composable
fun HasSideEffect() {
    count++
    Text(text = "Count : $count")
}