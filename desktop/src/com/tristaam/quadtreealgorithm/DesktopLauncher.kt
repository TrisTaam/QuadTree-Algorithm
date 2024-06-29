package com.tristaam.quadtreealgorithm

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration

fun main() {
    val config = Lwjgl3ApplicationConfiguration()
    config.setForegroundFPS(Constant.MAX_FPS)
    config.setTitle(Constant.TITLE)
    config.setWindowedMode(Constant.WIDTH, Constant.HEIGHT)
    config.setWindowPosition(-1, -1)
    config.setResizable(false)
    Lwjgl3Application(QuadTreeAlgorithm(), config)
}
