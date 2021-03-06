
import ImageRepository
import UserSettings
import UserSettingsRepository
import android.graphics.Bitmap
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maximot.floodfill.floodfill.data.*
import utils.FloodFillAlgorithm
import java.io.IOException
import kotlin.math.max


class FloodfillViewModel(
    private val imageRepository: ImageRepository,
    private val imageProcessingService: ImageProcessingService,
    private val floodFillerRepository: FloodfillerRepository,
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    companion object {
        const val FILLING_COLOR: Int = 0xFFFF6101.toInt()
    }

    val height
        get() = image.value?.height ?: 0
    val width
        get() = image.value?.width ?: 0

    val fps = MutableLiveData<Int>()
    val image = MutableLiveData<Bitmap>()
    val algorithm = MutableLiveData<FloodFillAlgorithm.FloodfillAlgorithm>()
    val isBusy = MutableLiveData<Boolean>()

    private val floodfillingExecutor = FloodFillingExecutor()

    init {
        isBusy.value = true
        try {
            image.value = imageRepository.load()
            try {
                val fillers = floodFillerRepository.getFloodfillersFor(image.value!!)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    fillers.forEach {
                        floodfillingExecutor.addFiller(it)
                    }
                }
            } catch (e: IOException) {
                // NO-OP
            }

        } catch (e: IOException) {
            System.err.println(e)
            onGenerateImage(128, 128)
        }
        val userSettings = userSettingsRepository.loadUserSettings()
        isBusy.value = false
        fps.value = userSettings.fps
        algorithm.value = userSettings.algorithm
    }

    fun onBitmapClicked(x: Int, y: Int) {
        onNewPoint(x, y)
    }

    fun onNewFps(fps: Int) {
        this.fps.value = fps
        updateFps()
    }

    fun onNewAlgorithm(algorithm: FloodFillAlgorithm.FloodfillAlgorithm) {
        this.algorithm.value = algorithm
    }

    fun onGenerateImage(width: Int, height: Int) {
        isBusy.value = true
        floodfillingExecutor.clear()
        image.value = generateImage(max(width, 32), max(height, 32))
        saveImage()
        isBusy.value = false
    }

    override fun onCleared() {
        saveImage()
        try {
            image.value?.recycle()
        } catch (e: Exception) {
            // NO-OP
        }
        super.onCleared()
    }

    fun onStart() {
        floodfillingExecutor.start()
    }

    fun onStop() {
        floodfillingExecutor.stop()
        saveImage()
        saveFillers()
        saveSettings()
    }

    private fun onNewPoint(x: Int, y: Int) {
        val floodFiller =
            imageProcessingService.floodfillImagePart(
                image.value ?: return,
                x,
                y,
                FILLING_COLOR,
                algorithm.value ?: return
            )
        updateFps()
        floodfillingExecutor.addFiller(floodFiller)
    }

    private fun generateImage(width: Int, height: Int): Bitmap {
        return imageProcessingService.generateImage(width, height)
    }

    private fun updateFps() {
        if (fps.value == null) {
            fps.value = 30
        }
        floodfillingExecutor.fps = fps.value!!
    }

    private fun saveSettings() {
        userSettingsRepository.saveUserSettings(
            UserSettings(
                fps.value!!,
                algorithm.value!!
            )
        )
    }

    private fun saveImage() {
        imageRepository.save(image.value ?: return)
    }

    private fun saveFillers() {
        floodFillerRepository.saveFloodfillers(floodfillingExecutor.getFillers())
    }
}
