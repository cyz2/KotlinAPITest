package com.cyz2.kotlinapitest.api

class CollectionTestClass {

    // listOf创建一个 只读 列表集合，可以含有空值、重复值以及不同类型数据
    val listInt = listOf(1, 3, 0, 2, 9, 2, 5, 21, 4, 6)

    // listOf创建一个列表，可以含有空值和重复值
    val listOfNull = listOf("one", "two", null, "two", "three", null)

    //        listOfNull[0] = 3 // 编译错误，无法修改：No set method providing array access
    // listOfNotNull可以过滤元素中的空值
    val listOfNotNull = listOfNotNull("one", "two", null, "two", "three", null)

    fun checkCollectionAPIs() {
        checkCollectionAPIsByList()
        checkListAPIs()
        checkListExtendAPIs()
        checkIterableAPIsByList()
        checkMutableListAPIs()
        checkSetAPIs()
        checkMapAPIs()
        checkTransform()
        checkNewFuns()
    }

    /**
     *  集合的公共行为
     *  以List为例，说明Collection相关接口
     */
    fun checkCollectionAPIsByList() {
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
    fun checkListExtendAPIs() {
        // getOrNull() 获取索引位置的值，超出索引会返回null,在判空后调用get()
        println("listSub getOrNull index 7: ${listOfNotNull.getOrNull(7)}") // null
        // getOrElse() 获取索引位置的值，超出索引会返回表达式的值，在判空后调用get()
        println("listSub getOrElse index 7: ${listOfNotNull.getOrElse(7) { "unKnow" }}") // unKnow
        // elementAtXXX() 同上
        println("listSub getOrElse index 7: ${listOfNotNull.elementAtOrNull(7)}") // null
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
        val (listEven, listOdd) = listInt.partition { it % 2 == 0 }
        println("After partition listOdd: $listOdd")    // [1, 3, 9, 5]
        println("After partition listEven: $listEven")  // [0, 2, 2, 2, 4, 6]

        // zip() 将两个集合按照索引配对，返回一个List<Pair<Int, Int>>
        val listPair = listOdd.zip(listEven)
        println("After zip listPair: $listPair")    // [(1, 0), (3, 2), (9, 2), (5, 2)]

        //unzip() 与zip的功能相反，将一个由pair组成的list分解成两个list
        val (listOdd2, listEven2) = listPair.unzip()
        println("After unzip listOdd2: $listOdd2")      // [1, 3, 9, 5]
        println("After unzip listEven2: $listEven2")    // [0, 2, 2, 2]

        // 将集合分割成大小最大为size的多个小结合，返回返回List<List<T>>
        println("After chunked 3: ${listInt.chunked(3)}") // [[1, 3, 0], [2, 9, 2], [5, 21, 4], [6]]
    }

    fun checkMutableCollectionAPIs(mutableList: MutableList<String?>) {
        // 在指定位置添加元素，不指定则添加在末尾
        mutableList.add("two")
        mutableList.add(0, "two")
        println("Mutablelist add : $mutableList")
        // 在指定位置之后加入集合，不指定则添加在最后
        mutableList.addAll(listOf("ten", "twenty", "thirty"))
        println("Mutablelist after addAll : $mutableList")

        // 删除第一个指定元素
        mutableList.remove("two")
        println("Mutablelist after remove : $mutableList")
        // 删除指定位置的元素
        mutableList.removeAt(5)
        println("Mutablelist after remove index 5 : $mutableList")
        // 删除指定集合中含有的元素
        mutableList.removeAll(listOf(null, "twenty"))
        println("Mutablelist after removeAll : $mutableList")
        // 删除所有符合条件的元素
        mutableList.removeIf { it.equals("two") }
        println("Mutablelist after removeIf : $mutableList")

        // 获取两个集合的合集
        mutableList.retainAll(listOf("thousand", "hundred", "ten"))
        println("Mutablelist retainAll : $mutableList")

        // 清空集合
        mutableList.clear()
        println("Mutablelist clear : $mutableList")
    }

    fun checkMutableListAPIs() {
        // 只读集合和可变集合可以相互转换
        val mutableList = listOfNull.toMutableList()
        // Mutable集合可以修改
        mutableList[2] = "six"
        println("Mutablelist: $mutableList")
        val tmp = mutableList.toList()
//        tmp[0] = 2 // 编译错误：Not set method providing array access

        // 替代某个位置的值
        mutableList.set(1, "XXX")
        println("Mutablelist set : $mutableList")

        // 删除集合中第一个元素，如果有就返回元素，如果没有则抛出NoSuchElementException异常
        println("Mutablelist after removeFirst:${mutableList.removeFirst()}: $mutableList")

        /** removeFirst()相关函数还有 */
//        mutableList.removeFirstOrNull()   // 集合是空的则返回Null，不抛异常
//        mutableList.removeLast()
//        mutableList.removeLastOrNull()

        checkMutableCollectionAPIs(mutableList)
    }

    /**
     * Set集合同样继承自Collection，由一串无序的、不能重复的元素构成。
     * 和List的差别是，不能通过序号访问，不能有重复元素，
     * 其他继承自Collection和Iterable的接口使用方式同List
     */
    fun checkSetAPIs() {
        val set = setOf(1, 2, "three", 'a', null)
        println("set: $set")  // set: b c a
        // set 不能存入重复元素
        val setChar = setOf('b', 'c', 'a', 'a', 'b')
        println("set: $setChar")  // set: b c a
        // 相比于List，布置没有set方法，连get方法都没有
//        setChar[0]  // 编译错误： Not get method providing array access
        // 将集合分割成多个大小最多为size，返回List<List<T>>
        println("set contain z: ${setChar.contains('z')}")  // false
        setChar.contains('z')
        // List转成Set，包括去重
        val setFromList = listOfNull.toSet()
        println("set: $setFromList")
        checkMutableSet()
    }

    fun checkMutableSet() {
        val mutableSet = mutableSetOf(9, 2, 3, 3, 3, 8, 3, 1, 2, 4, 0)
        // 有add()和remove()接口
        mutableSet.remove(9)
        mutableSet.add(7)
        println("MutableSet: $mutableSet")  // [2, 3, 8, 1, 4, 0, 7]
        // HashSet，也是MutableSet
        val hashset = hashSetOf(9, 2, 3, 3, 3, 8, 3, 1, 2, 4, 0)
        println("HashSet Size ${hashset.size}:${hashset}") // 7:[0, 1, 2, 3, 4, 8, 9]
        // LinkedHashSet，也是MutableSet
        val linkedHashSet = linkedSetOf(9, 2, 3, 3, 3, 8, 3, 1, 2, 4, 0)
        linkedHashSet.add(7)
        linkedHashSet.remove(9)
        println("LinkedHashSet :${linkedHashSet}")  // [2, 3, 8, 1, 4, 0, 7]
        println("HashSet first ${hashset.first()}, LinkedHashSet first:${linkedHashSet.first()}") // 0, 2
        // TreeSet
        val sortSet = sortedSetOf(9, 2, 3, 3, 3, 8, 3, 1, 2, 4, 0)
        println("sortSet:${sortSet}")  // [0, 1, 2, 3, 4, 8, 9]
    }

    fun checkMapAPIs() {
        // Map中同样的key存入不同的值，会被覆盖
        val map = mapOf('d' to 'D', 'a' to 'C', 'b' to 'B', 'a' to 'A')
        println("Map size is ${map.size}")  // Map size is 2

        // 遍历Key，然后通过Key获得Value
        for (key in map.keys) {
            print("$key = ${map[key]}, ")
        }
        println()
        //不同于List和Set，这个foreach是Map的扩展函数，内部也是使用for循环实现
        map.forEach { print("${it.key} = ${it.value}, ") }
        println()
        map.forEach { (key, value) ->
            print("$key = $value, ")
        }
        println()
        // 输出： d = D, a = A, b = B,

        checkMutableMapAPIs()

        val mutableMap = map.toMutableMap()
    }

    fun checkMutableMapAPIs() {
        val mutableMap = mutableMapOf(1 to "one", 2 to "two", 3 to "Three")
        println("MutableMap: $mutableMap") //{1=one, 2=two, 3=Three}
        mutableMap.put(4, "four")
        println("MutableMap: $mutableMap") // {1=one, 2=two, 3=Three, 4=four}
        mutableMap.clear()
        println("MutableMap: $mutableMap") // {}
        //HashMap
        val hashMap = hashMapOf('d' to 'D', 'a' to 'C', 'b' to 'B', 'a' to 'A')
        println("HashMap: $hashMap")  // {a=A, b=B, d=D}
        //SortedMap
        val sortedMap = sortedMapOf('d' to 'D', 'a' to 'C', 'b' to 'B', 'a' to 'A')
        println("SortedMap: $sortedMap")  // {a=A, b=B, d=D}
    }

    fun checkTransform() {
        // 可变集合和不可变集合可以相互转换
        val list = listOf("one", "two", "two", "three")
        val mutableList = list.toMutableList()
        val listBack = mutableList.toList()

        val set = setOf('b', 'c', 'a', 'a', 'b')
        val mutableSet = set.toMutableSet()
        val setBack = mutableSet.toSet()

        val map = mapOf(1 to "one", 2 to "two", 3 to "Three")
        val mutableMap = map.toMutableMap()
        val mapBack = mutableMap.toMap()

        // List 转 Set/MutableSet，会去重
        val list2Set = list.toSet()
        val list2MutableSet = list.toMutableSet()
        println("List size:${list.size}, list2Set size:${list2Set.size}")

        // Set 转 List/MutableList
        val set2List = set.toList()
        val set2MutableList = set.toMutableList()

        // List/Set 转 Map，需要设置转换表达式
        val list2Map = list.associate { it to it }
        println("List to Map: $list2Map") // {one=one, two=two, three=three}

        // Map 转 List
        val map2List = map.toList()
        println("Map to List: $map2List") // [(1, one), (2, two), (3, Three)]

        // List/Set 可以和数组相互转换
        var array: Array<String>
        array = list.toTypedArray()
        print("List to Array：")
        for (index in array.indices) {
            print("${array[index]} \t")     // one  two  two  three
        }
        println()
        array = list2Set.toTypedArray()
        print("Set to Array: ")
        for (index in array.indices) {
            print("${array[index]} \t")     // one 	 two 	three
        }
        println()

        /* 所有转换结果都是返回一个新的对象，对其操作不会对原始对象有影响 */
    }

    fun checkNewFuns() {
        checkFunMap()
        checkFunFlatMap()
        checkSequence()
        checkUnknowAPIs()
    }

    fun checkFunMap() {
        /**
         * map()
         * 根据表达式对每个元素进行一一转换，返回结果集
         * */
        val listString: List<String> = listOf("1", "2", "3", "a", "yes")
        // 使用匿名函数
        val trans = fun(str: String): Int = str.toIntOrNull() ?: 0
        val listInt: List<Int> = listString.map(trans)
        //使用Lambda表达式
        val listInt2: List<Int> = listString.map {
            it.toIntOrNull() ?: 0
        }
        println(listInt)    // [1, 2, 3, 0, 0]
        println(listInt2)   // [1, 2, 3, 0, 0]
    }

    fun checkFunFlatMap() {
        /**
         * flatMap()
         * 将几个小列表合成一个单一列表
         * */
        // 把一个二级列表变换成一个一级列表
        val list = listOf(listOf(1, 2), listOf(2, 3, 4), listOf(5))
        val listSingle = list.flatMap { it }
        // 同上
        val listSingle2 = list.flatten()
        println(listSingle) // [1, 2, 2, 3, 4, 5]

        val groups = listOf(
            Group("Group1", listOf("item11", "item12")),
            Group("Group2", listOf("item21", "item22")),
            Group("Group3", listOf("item31", "item32", "item33"))
        )
        // 将一个复杂对象拆分成多个独立列表
        val listTitle = groups.flatMap { listOf(it.title) }
        println("Title List : $listTitle") // [Group1, Group2, Group3]
        val listData = groups.flatMap { it.data }
        println("Data List : $listData")    // [item11, item12, item21, item22, item31, item32, item33]
    }

    class Group(val title: String, val data: List<String>)


    fun checkSequence() {
        val list = listOf(1, 2, 3, 4, 5, 6)
        // 列表元素转换并条件过滤
        val result = list
            .map {
                println("map: $it")
                it * 2
            }
            .filter {
                println("filter: $it")
                it % 3 == 0
            }
        println("Before get List Average")
        // 求结果集合的平均值
        println("Average is ${result.average()}")

        val resultAsSequence = list
            .asSequence()
            .map {
                println("map: $it")
                it * 2
            }
            .filter {
                println("filter: $it")
                it % 3 == 0
            }
        println("Before get Sequence Average ")
        // 求结果集合的平均值
        println("ListAsSequence Average is ${resultAsSequence.average()}")


        // 求结果集合的平均值
        println("First elem >3 in List is ${result.first { it >3 }}")
        println("First elem >3 in Sequence is ${resultAsSequence.first { it >3 }}")
    }


    fun checkUnknowAPIs() {
        // maxOf() 获取符合表达式的最大值
        // Returns the largest value among all values produced by [selector] function
        //
        val maxOf = listInt.maxOf { it > 5 }
        println("maxOf : $maxOf")
        // maxByOrNull()  Returns the first element yielding the largest value of the given function or `null` if there are no elements.
        val maxByOrNull = listInt.maxByOrNull { it > 5 }
        println("maxByOrNull : $maxByOrNull")
        //  * Returns the largest value among all values produced by [selector] function
        // * applied to each element in the collection or `null` if there are no elements.
        val maxOfOrNull = listInt.maxOfOrNull { it > 10 }
        println("maxOfOrNull : $maxOfOrNull")


        val nameToAge = listOf("Alice" to 42, "Bob" to 28, "Carol" to 51)
        var oldestPerson = nameToAge.maxByOrNull { it.second }
        println(oldestPerson) // (Carol, 51)

        val emptyList = emptyList<Pair<String, Int>>()
        val emptyMax = emptyList.maxByOrNull { it.second }
        println(emptyMax) // null
    }

}