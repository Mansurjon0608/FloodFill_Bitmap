

import android.graphics.Bitmap
import android.graphics.Point
import com.maximot.floodfill.floodfill.data.ImageProcessingService
import utils.BitmapGenerator
import utils.FloodFillAlgorithm
import utils.Floodfiller
import java.lang.Math.random
import kotlin.math.cos

class ImageProcessingServiceImpl : ImageProcessingService {
    override fun generateImage(width: Int, height: Int): Bitmap {
        val seed = (
                    (System.nanoTime() + (cos((width and height).toFloat()) * 1000)) * random()
                ).toLong()
        return BitmapGenerator.generate(width, height, seed)
    }

    override fun floodfillImagePart(
        image: Bitmap,
        x: Int, y: Int,
        color: Int,
        algorithm: FloodFillAlgorithm.FloodfillAlgorithm
    ): Floodfiller {
        return Floodfiller.create(image, Point(x, y), color, algorithm)
    }
}