
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun BottomToTopFilledCircle(
    modifier: Modifier = Modifier,
    circleColor: Color = Color.Blue,
    backgroundColor: Color = Color.White,
    progress: Float // Progress should be a value between 0 and 1
) {
    Canvas(Modifier.size(200.dp)) {
        val centerY = size.height
        val centerX = size.width / 2f
        val radius = size.width / 2f

        // Draw background
        drawCircle(color = backgroundColor, center = Offset(centerX, centerY), radius = radius)

        // Calculate the y-coordinate of the top of the filled circle based on the progress
        val filledCircleTopY = centerY - radius * 2 * progress

        // Draw filled circle
        drawCircle(color = circleColor, center = Offset(centerX, filledCircleTopY), radius = radius)
    }
}