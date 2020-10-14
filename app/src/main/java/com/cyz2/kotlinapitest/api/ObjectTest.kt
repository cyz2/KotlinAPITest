package com.cyz2.kotlinapitest.api

//class ObjectTest {
//    fun test() {
//        UserDAO.create()
//        var datas = UserDAO.findALL()
//
//        val sing:Singleton = Singleton.instance
//        sing.method()
//        val sing2:Singleton2 = Singleton2.getInstance()
//        sing2.method()
//    }
//}

class Person private constructor(val gender: String) {
    //companion object 可以有名字
    companion object Common {
        const val MALE = "male"
        const val FEMALE = "female"
        fun getMale(person: Person): String {
            return person.gender
        }
        fun createPerson(gender:String): Person {
            return Person(gender)
        }
    }
}

val personGender1 = Person.FEMALE // 可以通过类名直接访问
val personGender2 = Person.MALE// 也可以通过类名和伴生对象的名字访问
val person3 = Person.createPerson(Person.MALE)

interface DAOInterface {
    fun create(): Int
    fun findALL(): Array<Any>?
}

object UserDAO : DAOInterface {
    private var datas: Array<Any>? = null

    override fun findALL(): Array<Any>? {
        return datas
    }

    override fun create(): Int {
//        object Singleton{ // is a singleton and cannot be local
//            val x = 10
//        }
        return 0
    }

    object Singleton{
        val x = 10
    }

}

class Outer{
    object Singleton2{
        val y = 10
    }
}


class Singleton private constructor(){ // 私有化构造函数
    var i = 1
    fun method() {
        i++
        println(i)
    }
    companion object{
        // 静态成员获取单例对象
        fun getInstance(): Singleton {
            return Inner.INSTANCE
        }
    }

    // 静态内部类
    private object Inner{
        val INSTANCE = Singleton()
    }
}

class Singleton2 private constructor() {
    var i = 1
    fun method() {
        i++
        println(i)
    }

    companion object {
        @JvmStatic
        val instance: Singleton2 by lazy { Singleton2() }
    }
}




