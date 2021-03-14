package com.example.demo.datafetchers

import com.example.demo.generated.DgsConstants
import com.example.demo.generated.types.Cocktail
import com.example.demo.generated.types.Glass
import com.example.demo.services.IngredientsService
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment
import com.netflix.graphql.dgs.InputArgument


@DgsComponent
class CocktailsDataFetcher(private val ingredientsService: IngredientsService) {

    private val cocktailList = listOf(
        Cocktail(id = "1", name = "Pina colada", glass = Glass.LONGDRINK, alcoholic = true),
        Cocktail(id = "2", name = "Martini", glass = Glass.MARTINI, alcoholic = true),
        Cocktail(id = "3", name = "Old fashioned", glass = Glass.TUMBLER, alcoholic = true)
    )

    /**
     * The datafetcher resolves the cocktails field on Query.
     * It uses an @InputArgument to get the nameFilter from the Query.
     * All fields are fetched, only the requested ones end up in the response.
     */
    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Cocktails)
    fun cocktails(@InputArgument("nameFilter") nameFilter: String?): List<Cocktail> {
        return if (nameFilter != null) {
            cocktailList.filter { it.name?.contains(nameFilter) == true }
        } else {
            cocktailList
        }
    }

    /**
     * Seperate child datafetchers can be used for "expensive" queries. This way they are only fetched when requested.
     * This datafetcher only gets executed when the ingredient field is included in the query
     */
    @DgsData(parentType = "Cocktail", field = DgsConstants.COCKTAIL.Ingredients)
    fun ingredients(dfe: DgsDataFetchingEnvironment): List<String> {
        val cocktail: Cocktail = dfe.getSource()
        return ingredientsService.forCocktail(cocktail.id)
    }
}


