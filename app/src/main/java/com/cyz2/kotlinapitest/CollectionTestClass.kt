package com.cyz2.kotlinapitest

class CollectionTestClass {

    // listOf创建一个 只读 列表集合，可以含有空值、重复值以及不同类型数据
    val listInt = listOf(1, 3, 0, 2, 9, 2, 5, 21, 4, 6)
    // listOf创建一个列表，可以含有空值和重复值
    val listOfNull = listOf("one", "two", null, "two", "three", null)
    //        listOfNull[0] = 3 // 编译错误，无法修改：No set method providing array access
    // listOfNotNull可以过滤元素中的空值
    val listOfNotNull = listOfNotNull("one","two", null, "two", "three", null)

    fun checkCollectionAPIs() {
        checkCollectionAPIsByList()
        checkListAPIs()
        checkListExtendAPIs()
        checkIterableAPIsByList()


    }

    /**
     *  集合的公共行为
     *  以List为例，说明Collection相关接口
     */
    fun checkCollectionAPIsByList(){
        // size 集合大小
        println("listOfNull size is ${listOfNull.size}, listOfNotNull size is ${listOfNotNull.size}") // 6 4
        // isEmpty() 集合是否是空的
        println("listOfNull is Empty: ${listOfNull.isEmpty()}") // false
        // contains()/containsAll() 集合中是否包含指定元素/元素集合
        println("${listOfNotNull.contains(null)}, ${listOfNull.containsAll(listOfNotNull)}") // false true
        // iterator() 获取迭代器，用于遍历数组
        val iter = listOfNull.iterator()
        while (iter.hasNext()) {
            print("${iter.next()} ") // one two null two three null
        }
        println()

        for (i in listOfNotNull.indices) {
//        for (i in 0 until listOfNotNull.size) {
            // list支持索引遍历,[]是运算符重载, list[i]是调用List的get()方法;
            // 因为支持快速随机访问，推荐使用for循环，会优于迭代器获取
            print("${listOfNotNull[i]} ") // one two two three
        }
        println()
    }

    /**
     * List类提供的接口
     * */
    fun checkListAPIs() {
        // indexOf()/lastIndexOf() 获取value第一次/最后一次出现的位置
        val indexTwo = listOfNotNull.indexOf("two")
        val lastindexTwo = listOfNotNull.lastIndexOf("two")
        val indexFour = listOfNotNull.indexOf("four") // 不存在的元素
        println("\"two\" first index at $indexTwo and last index at $lastindexTwo, \"four\" at index:$indexFour") // 1 2 -1
        // subList() 获取子集合[fromIndex,toIndex)
        val listSub = listOfNotNull.subList(0, 2)
        println("subList(0, 2) size is ${listSub.size}: $listSub") // 2  [one, two]
        // get() 根据下标获取value
//        println("listSub get index 3: ${listSub.get(3)}") // 会有风险：IndexOutOfBoundsException
    }

    /**
     * List扩展函数
     * */
    fun checkListExtendAPIs(){
        // getOrNull() 获取索引位置的值，超出索引会返回null,在判空后调用get()
        println("listSub getOrNull index 7: ${listOfNotNull.getOrNull(7)}") // null
        // getOrElse() 获取索引位置的值，超出索引会返回表达式的值，在判空后调用get()
        println("listSub getOrElse index 7: ${listOfNotNull.getOrElse(7) { "unKnow" }}") // unKnow
        // elementAtXXX() 同上
        println("listSub getOrElse index 7: ${listOfNotNull.elementAtOrNull(7) }") // null
        // slice() 获取指定索引范围内的子集合
        val listSince = listOfNotNull.slice(0..2)
        println("slice(0..2) size is ${listSince.size}: $listSince") // 3 [one, two, two]
    }

    /**
     *  集合的工具方法
     *  Iterable 扩展函数
     *  */
    fun checkIterableAPIsByList() {
        // filter() 条件过滤，返回符合条件的集合
        val listFilter = listOfNotNull.filter { "wo" in it }
        print("listFilter size is ${listFilter.size}: ") // 2

        // forEach() 遍历集合
        listFilter.forEach { print("$it ") } // two two
        println()

        // take()/takeLast() 根据传入的参数挑出该集合【前/后n个元素】的子集合
        val listTake = listOfNotNull.take(3)
        println("listTake(3) size is ${listTake.size}: $listTake") // 3: [one, two, two]
        // takeWhile()/takeLastWhile 从头/尾部开始取值，不符合立即停止
        val listTakeLastWhile = listInt.takeLastWhile { it > 3 }
        println("listTakeLastWhile{ it > 3} size is ${listTakeLastWhile.size}: $listTakeLastWhile") // 2 [4, 6]
        // takeIf()/takeUnless() 判断集合是否符合/不符合条件，如果符合/不符合则返回集合，不符合/符合返回null
        val listTakeIf = listInt.takeIf { it.size == 8 }
        println("list takeIf { it.size == 10} is $listTakeIf ") // null

        // drop()/dropLast() 返回除了前/后n个元素之外的所有元素集合。
        // dropWhile()/dropLastWhile 从头/末尾开始，移除连续符合条件的元素，返回剩余元素
        // 注意，遍历开始一旦遇到不满足条件的就返回了，不会管中间是否有满足条件的元素
        val listrDropedWhile = listOfNull.dropWhile { it == null }
        println("list dropWhile { it == null } size is ${listrDropedWhile.size}: $listrDropedWhile") // 6，返回整个集合

        // find()/findLast() 返回符合条件的第一个/最后一个元素，没找到返回null
        // 其实调用的是firstOrNull()/lastOrNull()
        val findResult = listOfNull.find { it != null }
//        var firstValue = listOfNull.firstOrNull{ it != null}
        println("list find { it != null }： $findResult") // one

        // reversed() 翻转集合
        val listReverse = listOfNull.reversed()
        println("list reversed to：$listReverse") // [null, three, two, null, two, one]

        // sorted()/sortedDescending() 对同一类型的集合按照升序/降序排列
        // sortedBy() 根据条件排序，比如：奇数偶数分离
        val listIntSorted = listInt.sortedBy { it % 2 }
        println("list Sorted By %2 to：$listIntSorted")  // [0, 2, 2, 2, 4, 6, 1, 3, 9, 5]

        // 判断集合中是否每个元素都符合指定条件
        val resultAllNotNull = listOfNull.all { it != null }
        val resultAllNotNull2 = listOfNotNull.all { it != null }
        // 判断集合中是否有任意一个元素符合指定条件
        val resultAnyNull = listOfNull.any { it == null }
        println(" $resultAllNotNull, $resultAllNotNull2; $resultAnyNull")   // false, true; true

        //plus() 拼接两个列表集合，返回一个新集合
        var listPlus = listInt.plus(listOfNull)
        println("listInt.plus(listOfNull) : $listPlus")
        // 输出：listInt.plus(listOfNull) : [1, 3, 0, 2, 9, 2, 5, 2, 4, 6, one, two, null, two, three, null]
        // 操作符重载，本质也是调用list.plus(list2)
        listPlus = listInt + listOfNotNull
        println("listInt + : $listPlus")
        // 输出：listInt + : [1, 3, 0, 2, 9, 2, 5, 2, 4, 6, one, two, two, three]

        //partition() 根据条件拆分成两个列表，返回Pair(first, second)
        val (listEven,listOdd) = listInt.partition { it % 2 == 0 }
        println("After partition listOdd: $listOdd")    // [1, 3, 9, 5]
        println("After partition listEven: $listEven")  // [0, 2, 2, 2, 4, 6]

        // zip() 将两个集合按照索引配对，返回一个List<Pair<Int, Int>>
        val listPair = listOdd.zip(listEven)
        println("After zip listPair: $listPair")    // [(1, 0), (3, 2), (9, 2), (5, 2)]

        //unzip() 与zip的功能相反，将一个由pair组成的list分解成两个list
        val (listOdd2,listEven2) = listPair.unzip()
        println("After unzip listOdd2: $listOdd2")      // [1, 3, 9, 5]
        println("After unzip listEven2: $listEven2")    // [0, 2, 2, 2]

    }

    fun checkMutableListAPI() {
        //set.add;   set.remove;  set.clear ...跟java一样。（set集合不允许重复，java也是一样）
    }

    fun checkSetAPI() {
        val set = setOf(1, 2, 3, 'a')
    }

    fun checkMapAPI() {

    }

    /**
     * List 和 Set都实现自Collection接口，进而实现了Iterable接口的
     * 而所有的Iterable都可以通过for遍历
     */
    fun checkCollection() {
        // List支持存入相同的值
        val list = listOf("one", "two", "two", "three", "four")
//        list.filter { "one" in it }
        print("checkCollection list: ")
//        listNumbers[0] = 3 // 编译错误，无法修改：No set method providing array access
        // list支持索引遍历,[]是运算符重载, list[i] 是调用List的get方法;
        for (i in 0 until list.size) {
            print("${list[i]} ")
        }
        println()
        // 输出：checkCollection list: one two two three four

        // set 不能存入重复元素
        val set = setOf('b', 'c', 'a', 'a', 'b')
        print("checkCollection set: ")
        // set无法通过索引进行遍历
        for (num in set) {
            print("$num ")
        }
        println()
        // 输出：checkCollection set: b c a

        // List 和 Set可以相互转换
        val list2Set = list.toSet()
        print("list2set: ")
        //利用foreach结合Lambda表达式遍历，所有继承自Iterable的类都可以使用foreach接口
        list2Set.forEach { print("$it ") }
        println()
        val set2List = set.toList()

        // Map中同样的key存入不同的值，会被覆盖
        val map = mapOf('a' to 'A', 'b' to 'B', 'a' to 'C')
        println("Map size is ${map.size}")  // Map size is 2

        // 遍历Key，然后通过Key获得Value
        for (key in map.keys) {
            print("$key = ${map[key]}; ")
        }
        println()
        //不同于List和Set，这个foreach是Map的扩展函数，内部也是使用for循环实现
        map.forEach { print("${it.key} = ${it.value}, ") }
        println()
        map.forEach { (key, value) ->
            print("$key = $value, ")
        }
        println()
        // 输出： a = C, b = B,

        // 可变集合和不可变集合可以相互转换：List、Set、Map都可以
        val mutableList = list.toMutableList()
        val list2 = mutableList.toList()

    }

    fun checkTransform() {
        //list转Map
//        val listAssociate = listOfNull.associate { it to it }
//        println("list2Map: $listAssociate") //{1=1, 3=3, a=a, null=null, 34.5=34.5}
    }


    fun checkUnknowAPIs(){
        // maxOf() 获取符合表达式的最大值
        // Returns the largest value among all values produced by [selector] function
        //
        val maxOf = listInt.maxOf { it > 5 }
        println("maxOf : $maxOf")
        // maxByOrNull()  Returns the first element yielding the largest value of the given function or `null` if there are no elements.
        val maxByOrNull = listInt.maxByOrNull { it >5}
        println("maxByOrNull : $maxByOrNull")
        //  * Returns the largest value among all values produced by [selector] function
        // * applied to each element in the collection or `null` if there are no elements.
        val maxOfOrNull = listInt. maxOfOrNull{ it >10}
        println("maxOfOrNull : $maxOfOrNull")


        val nameToAge = listOf("Alice" to 42, "Bob" to 28, "Carol" to 51)
        var oldestPerson = nameToAge.maxByOrNull { it.second }
        println(oldestPerson) // (Carol, 51)

        val emptyList = emptyList<Pair<String, Int>>()
        val emptyMax = emptyList.maxByOrNull { it.second }
        println(emptyMax) // null
    }

}