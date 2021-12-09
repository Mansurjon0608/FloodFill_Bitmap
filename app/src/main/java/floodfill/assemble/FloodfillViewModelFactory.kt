package com.maximot.floodfill.assemble

import FloodfillViewModel
import ImageRepository
import UserSettingsRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.maximot.floodfill.floodfill.data.FloodfillerRepository
import com.maximot.floodfill.floodfill.data.ImageProcessingService



@Suppress("UNCHECKED_CAST")
class FloodfillViewModelFactory(
    private val imageRepository: ImageRepository,
    private val imageProcessingService: ImageProcessingService,
    private val floodFillerRepository: FloodfillerRepository,
    private val userSettingsRepository: UserSettingsRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FloodfillViewModel::class.java)) {
            return FloodfillViewModel(
                imageRepository,
                imageProcessingService,
                floodFillerRepository,
                userSettingsRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}