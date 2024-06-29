package com.tristaam.quadtreealgorithm

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class QuadTreeAlgorithm : Game() {
    lateinit var spriteBatch: SpriteBatch

    override fun create() {
        spriteBatch = SpriteBatch()
        val mainScreen = MainScreen(this)
        setScreen(mainScreen)
        Gdx.input.inputProcessor = mainScreen
    }

    override fun render() {
        super.render()
    }

    override fun dispose() {
        super.dispose()
        spriteBatch.dispose()
    }
}
