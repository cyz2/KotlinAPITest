package com.cyz2.kotlinapitest

class DataClassTest {
    fun foo(){
        val user1 = User("Jean",23)
        val user2 = User("Jean",23)
        val user3 = user2.copy("Jack")
        println(user1 == user2)
        println(user3)

    }
}

data class User(val name: String, val age: Int)