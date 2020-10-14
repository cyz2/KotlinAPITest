package com.cyz2.kotlinapitest.api

class FunTest {
    fun sum(para1: Int, para2: Int) = para1 + para2

    val person1 = PersonP("Nick")
    val person2 = PersonP("Keyar");

    fun changePerson(person: PersonP) {
        var p = person
        p = person2
    }

    fun changeName(person: PersonP) {
        var p = person
        p.name = "Jack"
    }

    fun checkFun() {
        println("Before: person:$person1 name:${person1.name}")
        changePerson(person1)
        println("After change Person: person:$person1 name:${person1.name}")
        changeName(person1)
        println("After change Name: person:$person1 name:${person1.name}")

        val f: (Int, Int) -> Int = ::sum
        // 或者自动推断
        val f2 = ::sum
        println(f(1, 32))        // 33
        println(f.invoke(2, 3))  // 5
        execFunction(f2)        // 6
        fooName(para2 = "XX", para = intArrayOf(7, 7, 9), para1 = "Nick")// 参数顺序可变
        fooName("jack", *intArrayOf(2, 5), para2 = "Nick")

        val makefun = makeArray() //函数调用，返回一个函数
        //调用这个返回的函数，makeArray()内部变量的状态
        println(makefun(5))  // 输出结果：5
        println(makefun(5))  // 输出结果：10
        println(makefun(5))  // 输出结果：15

    }

    fun makeArray(): (Int) -> Int {
        var count = 0
        //返回一个匿名函数，或是使用lambda表达式捕获变量
//        return fun(element: Int): Int {
//            count += element
//            return count
//        }
        return {
            element -> count += element
            count
        }
    }


    fun execFunction(foo: (Int, Int) -> Int) {
        println(foo.invoke(3, 3))
    }

    fun <T> asList(vararg ts: T): List<T> {
        val result = ArrayList<T>()
        for (t in ts)
            result.add(t)
        return result
    }

    fun fooName(para1: String, vararg para: Int, para2: String) {
        var tmp: Int = 0
        para.forEach {
            tmp += it
        }
        println("P1:$para1, vararg:${tmp} p2:$para2")
    }

}

class PersonP(var name: String) {}
