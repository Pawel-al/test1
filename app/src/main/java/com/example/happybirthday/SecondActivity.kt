package com.example.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.happybirthday.ui.theme.HappyBirthdayTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class ImageItem(val resId:Int,val name:String)

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            HappyBirthdayTheme {
                Buu()
                }
            }
        }
    }

@Composable
fun GradientButton(onClick: () -> Unit, text: String){
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color.Blue,Color.Green)
                )
            )
            .width(200.dp)
            .height(60.dp)
            .border(3.dp, Color.Black, RoundedCornerShape(32.dp)),
        onClick = onClick
    ) {
       // Box)

    }
}
@Composable
fun Buu(){
    val imagesResIds = listOf(
        ImageItem(R.drawable.egg,"egg"),
        ImageItem(R.drawable.banana,"banana"),
        ImageItem(R.drawable.grape,"grape"),
        ImageItem(R.drawable.apple,"apple"),
        ImageItem(R.drawable.pizza,"pizza"),
        ImageItem(R.drawable.orange,"orange"),
        ImageItem(R.drawable.ice_cream,"ice_cream"),
        ImageItem(R.drawable.fries,"fries")
        )

    var images = remember { mutableStateListOf<ImageItem>() }
    var currentWord = remember { mutableStateOf("") }
    var currentImage = remember { mutableStateOf<ImageItem?>(null) }
    var points = remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()

    var selectedItem = remember { mutableStateOf<ImageItem?>(null) }

    val isGameActive = remember { mutableStateOf(false) }
    val timeLeft = remember { mutableStateOf(60) }


    fun randomizeImages(){
        images.clear()
        val shuffled = imagesResIds.shuffled()
        images.addAll(shuffled.take(4))
        currentImage.value = images.random()
        currentWord.value = currentImage.value?.name ?: ""
    }

    fun checkImage(selectedItem: ImageItem){
        if(selectedItem.name==currentWord.value){
            points.value += 1
        }
        randomizeImages()
    }
    fun startGame(){
        if(!isGameActive.value){
            randomizeImages()
            isGameActive.value = true
            timeLeft.value = 60
        }
    }

    LaunchedEffect(isGameActive.value) {
        if(isGameActive.value){
            delay(1000)
            timeLeft.value -= 1
        }
        isGameActive.value = false
    }

    Column (
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .border(1.5.dp, Color.Black, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        images.take(2).forEach{imageItem ->
                            val size = animateDpAsState(
                                targetValue = if (selectedItem.value == imageItem) 120.dp else 100.dp,
                                animationSpec = tween(durationMillis = 300)
                            )
                        Box(modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .border(4.dp, Color.Black, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                scope.launch {
                                    checkImage(imageItem)
                                }
                            }
                        ){
                            Image(
                                painter = painterResource(id = imageItem.resId),
                                contentDescription = null,
                                modifier = Modifier.clip(RoundedCornerShape(8.dp))
                            )

                        }
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        images.drop(2).forEach { imageItem ->
                            val size = animateDpAsState(
                                targetValue = if (selectedItem.value == imageItem) 120.dp else 100.dp,
                                animationSpec = tween(durationMillis = 300)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .border(4.dp, Color.Black, RoundedCornerShape(8.dp))
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        scope.launch {
                                            selectedItem.value = imageItem
                                            checkImage(imageItem)
                                        }
                                    }
                            ) {
                                Image(painter = painterResource(id = imageItem.resId),
                                        contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(8.dp)))
                            }
                        }
                    }
                }
            }
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = buildAnnotatedString {
                    append("${currentWord.value}")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){}
                }
                    , modifier = Modifier.align(Alignment.CenterHorizontally))

                Text(text = "${timeLeft.value}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineLarge)

                Text(text = "Points: ${points.value}",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge)
            }
        }
        Button(
            onClick = { startGame() },
            colors = ButtonDefaults.buttonColors(
                //containerColor = Color.
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(200.dp)
                .height(60.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(32.dp)),

            )
        { Text(
            text = buildAnnotatedString {
            withStyle(style = SpanStyle(fontSize =25.sp)){
                append("START")
            }
        })
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BuuPreview() {
    HappyBirthdayTheme {
        Buu()
    }
}

