package com.cyz2.kotlinapitest

//基础数据类型扩展
fun Double.interestBy(rate: Double): Double {
    return this * rate
}

class Account {
    var amount: Double = 0.0
    var owner: String = "Nick"
}

//Account类扩展函数
fun Account.interestBy(rate: Double): Double {
    return this.amount * rate
}

//基础数据类型扩展属性
val Int.errorMessage:String
    get() = when(this){
        -1 -> "No Exist"
        else -> "undefined"
    }

// 自定义类型（引用类型）扩展属性
var Account.desc:String
    get() {
        return "Account[User ${this.owner} has Amount:${this.amount}.]"
    }
    set(value) {
        println(value)
    }

class ExtensionFunTest {

    fun check(){
        val rate1 = 10_000.00.interestBy(0.055)
        println("利息1：$rate1")   // 利息1：550.0
        val account = Account()
        val rate2 = account.interestBy(0.068)
        println("利息2:$rate2")      // 利息2:0.0
        println(account.desc)       // Account[User Nick has Amount:0.0.]
        val msg = (-1).errorMessage
        println("Error Message: $msg")  // Error Message: No Exist
    }
}


