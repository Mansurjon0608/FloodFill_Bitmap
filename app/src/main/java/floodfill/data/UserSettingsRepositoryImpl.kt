

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import utils.FloodFillAlgorithm

class UserSettingsRepositoryImpl(context: Context) : UserSettingsRepository {

    companion object {
        private const val PREFS_NAME = "USER_SETTINGS"
        private const val PREFS_FPS_ARG = "FPS_ARG"
        private const val PREFS_ALGORITHM_ARG = "ALGORITHM_ARG"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveUserSettings(us: UserSettings) {
        sharedPreferences.edit {
            putString(PREFS_ALGORITHM_ARG, us.algorithm.name)
            putInt(PREFS_FPS_ARG, us.fps)
        }
    }

    override fun loadUserSettings() = UserSettings(
        fps = sharedPreferences.getInt(PREFS_FPS_ARG, 30),
        algorithm = FloodFillAlgorithm.FloodfillAlgorithm.valueOf(
            sharedPreferences.getString(PREFS_ALGORITHM_ARG, FloodFillAlgorithm.FloodfillAlgorithm.QUEUE.name)!!
        )
    )
}