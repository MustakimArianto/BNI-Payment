package id.co.integrapratama.sdk.core

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.Calendar
import kotlin.random.Random

class StanManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val LAST_RESET_DAY_KEY = "LAST_RESET_DAY_KEY"
        private const val STAN_KEY = "STAN_KEY"
    }

    fun getCurrentStan(): Long {
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val lastResetDay = sharedPreferences.getInt(LAST_RESET_DAY_KEY, -1)
        if (currentDay != lastResetDay) {
            resetStan()
            saveLastResetDay(currentDay)
        }
        return sharedPreferences.getLong(STAN_KEY, 1L)
    }

    fun increaseStan() {
        saveStan(sharedPreferences.getLong(STAN_KEY, 1L) + 1)
    }

    fun increaseStanUpRandom() {
        val randomNumber = Random.nextLong(1, 1000)
        saveStan(sharedPreferences.getLong(STAN_KEY, 1L) + randomNumber)
    }

    fun setStanManual(value: Long) {
        saveStan(value)
    }

    private fun resetStan() {
        saveStan(1L)
    }

    fun getGenerateRandomStan():Long{
        val randomInteger = Random.nextInt(1, 1000)
        saveStan(sharedPreferences.getLong(STAN_KEY, 1L) + randomInteger.toLong())
        return sharedPreferences.getLong(STAN_KEY, 1L)
    }

    private fun saveStan(value: Long) {
        sharedPreferences.edit { putLong(STAN_KEY, value) }
    }

    private fun saveLastResetDay(value: Int) {
        sharedPreferences.edit { putInt(LAST_RESET_DAY_KEY, value) }
    }
}