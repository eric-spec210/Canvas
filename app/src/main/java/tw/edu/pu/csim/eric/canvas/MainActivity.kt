package tw.edu.pu.csim.eric.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import tw.edu.pu.csim.eric.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CanvasScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun CanvasScreen(modifier: Modifier = Modifier) {
    var canvasW by remember { mutableStateOf(0f) }
    var canvasH by remember { mutableStateOf(0f) }

    // 小小兵的水平位移
    var minionX by remember { mutableStateOf(0f) }
    var minionY by remember { mutableStateOf(0f) }

    var gameOver by remember { mutableStateOf(false) }

    // 每秒往右移 10 像素
    LaunchedEffect(gameOver) {
        while (!gameOver) {
            delay(10L)
            minionX += 5
            minionY += 10
            // 選擇性：超出畫布右邊就回到左邊
            if (canvasH > 0f && minionY > canvasH) minionY = 0f
            if (canvasW > 0f && minionX > canvasW) minionX = 0f
        }
    }

    val minionsImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.minions)

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged {            // 在這裡取得畫布尺寸
                canvasW = it.width.toFloat()
                canvasH = it.height.toFloat()
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(Color.Magenta, 100f, Offset(100f, 100f))

            drawRect(
                Color.Blue,
                Offset(300f, 200f),
                Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            drawLine(
                Color.Red,
                Offset(200f, 800f),
                Offset(size.width, size.height),
                strokeWidth = 10f
            )

            // 依 offsetX 平移後畫小小兵
            translate(left = minionX, top = minionY) {
                drawImage(minionsImage)
            }
        }
        Column() {
            Text("畫布寬度 $canvasW,長度 $canvasH")
            Button(
                onClick = { gameOver = !gameOver }
            )
            {
                if(gameOver){ Text("繼續遊戲") }
                else{ Text("停止遊戲") }
            }
        }
    }
}

