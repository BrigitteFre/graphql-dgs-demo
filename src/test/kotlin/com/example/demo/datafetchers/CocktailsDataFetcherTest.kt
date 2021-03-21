package com.example.demo.datafetchers

import com.example.demo.generated.client.CocktailsGraphQLQuery
import com.example.demo.generated.client.CocktailsProjectionRoot
import com.example.demo.services.IngredientsService
import com.netflix.graphql.dgs.DgsQueryExecutor
import com.netflix.graphql.dgs.autoconfig.DgsAutoConfiguration
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest(classes = [DgsAutoConfiguration::class, CocktailsDataFetcher::class])
class CocktailsDataFetcherTest {

    @MockBean
    lateinit var ingredientsService: IngredientsService

    @Autowired
    lateinit var dgsQueryExecutor: DgsQueryExecutor

    @BeforeEach
    fun before() {
        `when`(ingredientsService.forCocktail("1"))
            .thenAnswer { listOf("pineapple juice", "rum", "coconut milk") }
    }

    @Test
    fun cocktails() {
        val graphQLQueryRequest = GraphQLQueryRequest(
            CocktailsGraphQLQuery.Builder()
                .nameFilter("Pina colada")
                .build(),
            CocktailsProjectionRoot().ingredients()
        )

        val result = dgsQueryExecutor.executeAndExtractJsonPath<List<String>>(
            graphQLQueryRequest.serialize(), "data.cocktails[*].ingredients[*]"
        )

        assertThat(result).contains("rum")
    }

}
