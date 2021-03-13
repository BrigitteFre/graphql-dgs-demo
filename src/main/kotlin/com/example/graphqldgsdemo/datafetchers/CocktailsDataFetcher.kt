package com.example.graphqldgsdemo.datafetchers

import com.example.demo.generated.DgsConstants
import com.example.demo.generated.types.Cocktail
import com.example.demo.generated.types.Glass
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsData
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class CocktailsDataFetcher {

    private val cocktailList = listOf(
        Cocktail(
            name = "Pina colada",
            glass = Glass.LONGDRINK,
            ingredients = listOf("pineapple juice", "rum", "coconut milk"),
            alcoholic = true
        ),
        Cocktail(name = "Martini", glass = Glass.MARTINI, ingredients = listOf("vermouth", "gin"), alcoholic = true),
        Cocktail(
            name = "Old fashioned",
            glass = Glass.TUMBLER,
            ingredients = listOf("bourbon", "sugar sirup", "Angostura bitters"),
            alcoholic = true
        )
    )

    /**
     * The datafetcher resolves the cocktails field on Query.
     * It uses an @InputArgument to get the nameFilter from the Query.
     */
    @DgsData(parentType = DgsConstants.QUERY_TYPE, field = DgsConstants.QUERY.Cocktails)
    fun cocktails(@InputArgument("nameFilter") nameFilter: String?) : List<Cocktail> {
        return if (nameFilter != null) {
            cocktailList.filter { it.name?.contains(nameFilter) == true }
        } else {
            cocktailList
        }
    }
}


