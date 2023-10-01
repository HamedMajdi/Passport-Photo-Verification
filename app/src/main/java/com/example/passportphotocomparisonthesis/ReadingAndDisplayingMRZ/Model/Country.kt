package com.example.passportphotocomparisonthesis.ReadingAndDisplayingMRZ.Model

import android.content.Context

data class Country(
    val name: String,
    val alpha2: String,
    val alpha3: String
)


/*Data Access Object (DAO): The DAO is a design pattern that provides an abstract interface to some
type of database or other persistence mechanism.
By mapping application calls to the persistence layer, DAOs provide some specific data operations without
exposing details of the database.
This isolation supports the single responsibility principle.
It separates what data access the application needs, in terms of domain-specific objects and data types
(the public interface of the DAO), and how these needs can be satisfied with a
specific DBMS, database schema, etc. (the implementation of the DAO).
*/

interface CountryDao {
    suspend fun getCountryByAlpha3(alpha3: String, context: Context): Country?
}