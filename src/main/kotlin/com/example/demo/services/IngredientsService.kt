package com.example.demo.services

import org.springframework.stereotype.Service

interface IngredientsService {
    fun forCocktail(id: String): List<String>
}

@Service
class IngredientServiceImpl : IngredientsService {

    private val ingredients = mapOf(
        "1" to listOf("pineapple juice", "rum", "coconut milk"),
        "2" to listOf("vermouth", "gin"),
        "3" to listOf("bourbon", "sugar sirup", "Angostura bitters")
    )

    override fun forCocktail(id: String): List<String> {
        return ingredients.getOrDefault(id, emptyList())
    }
}

