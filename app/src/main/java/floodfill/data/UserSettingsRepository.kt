import utils.FloodFillAlgorithm

interface UserSettingsRepository {
    fun saveUserSettings(us: UserSettings)
    fun loadUserSettings(): UserSettings
}

data class UserSettings(
    val fps: Int = 30,
    val algorithm: FloodFillAlgorithm.FloodfillAlgorithm = FloodFillAlgorithm.FloodfillAlgorithm.QUEUE
)