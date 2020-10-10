package com.cyz2.kotlinapitest

class InnerClassTest {

    fun checkFoo() {
        val outer = OuterClass()
        val nested = OuterClass.Nested()
        println(nested.foo())
        println(nested.getBarFromOuter(outer))

        var data = Data.Builder()
            .id("keyar")
            .num(13)
            .build()

        val outer2 = Outer2()
        val value = outer2.Inner().foo()
        val outer2value = outer2.Inner().foo2()
    }
}

class OuterClass {
    private val bar: Int = 1

    class Nested {
        fun foo() = 2

        fun getBarFromOuter(outer: OuterClass): Int {
            return outer.bar
        }
    }
}

class Data private constructor(id:String,num:Int){
    var id:String = id
        private set
    var num:Int = num
        private set
    class Builder{
        private lateinit var id:String
        private var num:Int = 0

        fun id(id:String):Builder{
            this.id = id
            return this
        }

        fun num(num:Int):Builder{
            this.num = num
            return this
        }

        fun build():Data{
            return Data(id,num)
        }
    }
}

class Outer2 {
    private val bar: Int = 1

    inner class Inner {
        fun foo() = bar

        fun foo2() {
            val x = this@Outer2.bar
        }
    }

    fun createInner(): Inner {
        return Inner()
    }
}

