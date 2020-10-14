package com.cyz2.kotlinapitest.api

import java.util.*

/**
 * Kotlin中数组由[Array<T>]表示，里面就几个方法
 * 创建数组的3个函数
 *  arrayOf()
 *  arrayOfNulls()
 *  工厂函数（Array()）
 *
 * */
class ArrayTestClass {

    fun checkArrays() {
//        createArrayByArrayOf()
//        createArrayByArrayOfNull()
//        createArrayByEmptyArray()
//        createArrayByArray()
//        createArrayByXXXArray()
//        createStringArray()
//        createMultiDimenArray()
        arrayOperations()
    }

    /**
     * 创建一个数组，参数是一个可变参数的泛型对象
     */
    fun createArrayByArrayOf() {
        var arrT = arrayOf(1, 2, "3", 'X', 12.3f, 1000L)
        print("createArrayByArrayOf:\t")
        for (a in arrT) {
            print("$a \t")
        }
        println()
    }

    /**
     * 用于创建一个指定数据类型且可以为空元素的给定元素个数的数组
     */
    fun createArrayByArrayOfNull() {
        var arrOfNull = arrayOfNulls<Int>(5)
        print("createArrayByArrayOfNull:\t")
        //如若不予数组赋值则元素值为null
        for (v in arrOfNull) {
            print("$v \t")
        }
        println()
        //为数组赋值
        arrOfNull[0] = 10   // 与java一样，可以这样修改数据
        arrOfNull.set(2, 1)  // kotlin可以通过set函数进行修改数据
        print("createArrayByArrayOfNull:\t")
        for (index in arrOfNull.indices) {
            print("${arrOfNull[index]} \t")     // 遍历initArr索引的元素，从0开始
//            print("${arrOfNull.get(index)}\t")// 可以通过get(索引)来获取元素
        }
        println()
    }

    /***
     * 创建一个长度为0空数组
     */
    fun createArrayByEmptyArray() {
        var arrE = emptyArray<Int>()
        println("createArrayByEmptyArray: size is ${arrE.size}")
//        arrE[0] = 12 // ArrayIndexOutOfBoundsException
    }

    /**
     * 使用一个工厂函数Array()，它使用数组大小和返回给定其索引的每个数组元素的初始值的函数。
     * Array() => 第一个参数表示数组元素的个数，第二个参数则为使用其元素下标组成的表达式
     */
    fun createArrayByArray() {
        var arrF = Array(5, { index -> index * index })
        print("createArrayByArray:\t")
        for (f in arrF) {
            print("$f \t")
        }
        println()
    }

    /**
     * 创建固定类型的数组
     */
    fun createArrayByXXXArray() {
        var arrInt = intArrayOf(10, 15, 20, 25)
        var arrInt2 = IntArray(10) //创建容量10的Int数组
//        var arr = Array(10) //错误
        var arrLong: LongArray = longArrayOf(12L, 1254L, 123L, 111L)

        var arrChar: CharArray = charArrayOf('H', 'e', 'l', 'l', 'o')
        // 字符数组转成字符串
        println("Array : ${arrChar.contentToString()} ")    // [H, e, l, l, o]
        var strFromArray = String(arrChar)
        println("String Converted from Array : ${String(arrChar)}")  // Hello

    }

    /**
     * 创建一个字符串数组
     * 字符串和字符串数组转化，以及查找、遍历操作
     */
    fun createStringArray() {
        val longString =
            "2313;133434;1232321;2313567;1232312;1232321;342342;2313567;1232321;2313567"
        // 以;过滤并转成Array
        val arrStr = longString.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        println("String Array size:${arrStr.size}")
        arrStr.forEach { print("$it \t") }
        println()

        var cmpString = "2313567"
//        var matchString = "12345"
        // 从Array 中确认有没需要查找的子串(元素)
        val found = Arrays.stream(arrStr).anyMatch { t -> t == cmpString }
        var newList = StringBuilder()
        // 没有找到，则将字串添加到原字串尾部
        if (!found) {
            println("Not Found!")
            newList = StringBuilder(longString).append(';').append(cmpString)
        } else {
            // 找到匹配串，则移除匹配的子串，形成新的字符串
            println("Found!")
            for (checkSubString in arrStr) {
                if (cmpString != checkSubString) {
                    if (newList.isEmpty()) {
                        newList.append(checkSubString)
                    } else {
                        newList.append(';').append(checkSubString)
                    }
                }
            }
        }
        println("newLongString = $newList")
    }

    fun createMultiDimenArray() {
        //创建一个2*4的Int二维数组
        var arr2Dimen = Array(2) { IntArray(4) }
        println("size of arr2Dimen is ${arr2Dimen.size}")       // 5
        println("size of arr2Dimen[0] is ${arr2Dimen[0].size}") // 10
        println("arr2Dimen[1] = ${arr2Dimen[1]}")               // [I@bfd8485
        println("arr2Dimen[1][1] = ${arr2Dimen[1][1]}")         // 0
        var line = 0;
        for (firstDimens in arr2Dimen) {
            print("Line ${++line}: ")
            for (secondDimens in firstDimens) {
                print("$secondDimens ")     // 默认值都是 0
            }
            println()
        }

        //三个长度为5的SelfType类型的二维数组，自定义类型的话需要在大括号里面操作
        val arrClass = Array(3) { y -> Array<SelfType>(5, { x: Int -> SelfType(5 * y + (x + 1)) }) }
        line = 1
        for (types in arrClass) {
            print("Line ${line++}: ")
            for (type in types) {
                print("${type.mNum}  ")
            }
            println()
        }
        // 泛型二维数组
        var arrSquire = arrayOf(
            arrayOf(1, 2, 3),
            arrayOf("one", "two", "three"),
            arrayOf(1.2f, 2.3f, 'F')
        )

        // 三维数组
        val arr3 = Array(3) { Array(4) { IntArray(5) } }

    }

    class SelfType(num: Int) {
        var mNum: Int? = null

        init {
            this.mNum = num
        }
    }


    fun arrayOperations() {
        var arrInt = intArrayOf(10, 15, 20, 25)
        // 创建的列表保留对原始Array的引用
        // asList函数创建一个重用相同Array实例的列表,这意味着对原始数组的更改也会对List产生影响
        var arrayAsList = arrInt.asList()
        println("arrayAsList before: $arrayAsList")
        arrInt[2] = 888
        println("arrayAsList after: $arrayAsList")
        //toList/toMutableList创建的列表由原始Array的副本支持.
        var arrayToList = arrInt.toList()
        arrInt[2] = 444
        println("arrayToList after: $arrayToList")
        var arrayToMutableList = arrInt.toMutableList()
        arrInt[2] = 333
        println("arrayToMutableList after: $arrayToMutableList")

        // 判断数组中是否每个元素都符合条件
        var resultAll = arrInt.all { it > 50 }
        println("All of elements > 50: $resultAll")

        // 查找数组中是否有满足条件的元素
        var resultAny = arrInt.any { it > 50 }
        println("Any of elements > 50: $resultAny")

        // 数组每个元素做映射，返回一个Map列表
        val associateMap = arrInt.associate { it/5 to it * 10 }
        println("AssociateMap: $associateMap")

        // 元素值批量替换
        arrInt.fill(0,1,arrInt.size)
        println("arrayAsList after fill: $arrayAsList")
    }
}
