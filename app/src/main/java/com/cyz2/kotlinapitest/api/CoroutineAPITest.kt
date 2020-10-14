package com.cyz2.kotlinapitest.api

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.lang.AssertionError
import java.util.concurrent.locks.ReentrantLock
import kotlin.system.measureTimeMillis


class CoroutineAPITest {
    val mContext = newFixedThreadPoolContext(1, "mtPool")
    var counter2 = 0

    fun main() {
        checkMutex()
        counter = 0
        checkMutex2()
        counter  =0
        checkCounter()
    }

    val mutex = Mutex()
    fun checkMutex() = runBlocking{
        GlobalScope.massiveRun {
            mutex.lock()
            counter++
            mutex.unlock()
        }
        println("checkMutex Count is $counter")
    }

    fun checkMutex2() = runBlocking{
        GlobalScope.massiveRun {
            mutex.withLock {
                counter++
            }
        }
        println("checkMutex2 Count is $counter")
    }

    fun checkCounterWithSingle() = runBlocking<Unit> {
        CoroutineScope(mContext).massiveRun {
            counter2++

        }
        println("Count is $counter2")
    }

    var counter = 0
    //    var counter = AtomicInteger()
    val mLock = ReentrantLock()
    fun checkCounter() = runBlocking{
            GlobalScope.massiveRun {
                synchronized(mLock) {
                    counter++
                }
//            counter.incrementAndGet()
            }
        println("checkCounter Count is $counter")
    }

    suspend fun CoroutineScope.massiveRun(acrtion: suspend () -> Unit) {
        val n = 100     // 启动协程数量
        val k = 1000    // 每个协程循环次数
        val time = measureTimeMillis {
            val jobs = List(n) {
                launch {
                    repeat(k) { acrtion() }
                }
            }
            jobs.forEach { it.join() }
        }
        println("Completeed ${n * k} actions is $time ms")
    }


    fun fooExceptionHandler() = runBlocking {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Cought $exception")
        }
        val job = GlobalScope.launch(handler) {
            println("launch AssertionError")
            throw AssertionError()
        }
        job.join()
        yield()
        val deferred = GlobalScope.async(handler) {
            println("async ArithmeticException")
            throw ArithmeticException()
        }
        try {
            deferred.await()
            println(" async 出错了") //不会打印到
        } catch (e: ArithmeticException) {
            println("捕获异常:$e")
        }
//        joinAll(job,deferred)
    }

    fun childScope() = runBlocking {
        //启动一个新协程
        val request = launch {
            // 使用GlobalScope启动协程是独立协程，和外层的协程没有父子关系
            GlobalScope.launch {
                println("job1:start")
                delay(1000L)
                println("job1:End")
            }
            // 使用launch启动协程和外层协程是父子关系
            launch {
                println("job2:start")
                delay(1000L)
                println("job2:End")
            }
        }
        delay(500)
        request.cancel() // request调用cancel后，子协程也会随之消失
        delay(1000L)
        println("Main: Finish")
    }

    fun changeContext() {
        newSingleThreadContext("CTX1").use { ctx1 ->
            newSingleThreadContext("CTX2").use { ctx2 ->
                runBlocking(ctx1) {
                    println("started in ${Thread.currentThread().name}")
                    withContext(ctx2) {
                        println("Working in ${Thread.currentThread().name}")
                    }
                    println("End to ${Thread.currentThread().name}")
                }
            }
        }
    }

    fun checkUnconfinedDispatcher() = runBlocking {
        launch(Dispatchers.Unconfined) {
            // 未指定执行线程，在主线程执行
            println("Unconfined: in thread:${Thread.currentThread().name}")
            delay(500L)
            println("Unconfined: in thread:${Thread.currentThread().name}")
        }
        launch {
            // 使用外层协程的线程
            println("runBlocking: in thread:${Thread.currentThread().name}")
            delay(1000L)
            println("runBlocking: in thread:${Thread.currentThread().name}")
        }
    }

    fun checkDispatcher() = runBlocking {
        launch(Dispatchers.Unconfined) {
            // 未指定执行线程，在主线程执行
            println("Unconfined: in thread:${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) {
            // 有Dispatchers指定线程
            println("Default: in thread:${Thread.currentThread().name}")
        }
        launch(newSingleThreadContext("MyOwnerThread")) {
            // 会启动一个线程执行
            println("newSingleThreadContext: in thread:${Thread.currentThread().name}")
        }
        launch {
            // 使用外层协程的线程
            println("Unconfined: in thread:${Thread.currentThread().name}")
        }
    }

    suspend fun getNumber1(): Int {
        delay(1000L) // 模拟耗时操作
        return 1
    }

    suspend fun getNumber2(): Int {
        delay(400L) // 模拟耗时操作
        return 2
    }

    suspend fun fooAsync() = GlobalScope.async {
        getNumber1()
    }

    suspend fun fooAsync2() = GlobalScope.async {
        getNumber2()
    }

    fun checkAync() = runBlocking {
        val time = measureTimeMillis {
            println(" The answer is ${fooAsync().await() + fooAsync2().await()}")
        }
        println("Time: $time ms")
    }

    suspend fun checkMerge(): Int = coroutineScope {
        val one = async { getNumber1() }
        val two = async { getNumber2() }
        one.await() + two.await()
    }

    fun checkLazy() = runBlocking {
        val time = measureTimeMillis {
//            val one = async(start = CoroutineStart.LAZY) { getNumber1() }
//            val two = async(start = CoroutineStart.LAZY)  { getNumber2() }
//            one.start()
//            two.start()
//            println(" The answer is ${one.await() + two.await()}")
            println(checkMerge())
        }
        println("Time: $time ms")
    }

    fun checkAwait() = runBlocking {
        val time = measureTimeMillis {
            val one = async { getNumber1() }
            val two = async { getNumber2() }
            println(" The answer is ${one.await() + two.await()}")
        }
        println("Time: $time ms")
    }


    fun checkGetTime() = runBlocking {
        val time = measureTimeMillis {
            val one = getNumber1()
            val two = getNumber2()
            println(" The answer is ${one + two}")
        }
        println("Time: $time ms")
    }


    fun checkCapacity() = runBlocking {
        val sender = produce<Int>(capacity = 2) {
            repeat(10) {
                println("Send $it")
                send(it)
                println("Sended $it")
            }
        }
        delay(1000L)
        sender.cancel()
    }

    suspend fun sendString(channel: SendChannel<String>, name: String, time: Long) {
        while (true) {
            delay(time)
            channel.send(name)
        }
    }

    fun checkReceive() = runBlocking {
        val channel = Channel<String>()
        launch { sendString(channel, "Nick", 200L) }
        launch { sendString(channel, "Keyar", 500L) }
        repeat(5) {
            println("receive: ${channel.receive()}")
        }
        coroutineContext.cancelChildren() // 取消当前Job下的所有子任务
    }


    // produce 也是CoroutineScope的扩展函数，创建一个协程用来发送到一堆数据到一个channel(ReceiveChannel)
    fun CoroutineScope.produceNumbers() = produce<Int> {
        var x = 1
        while (true) {
            send(x++)
            delay(200L)
        }
    }

    fun CoroutineScope.launchProcessorEach(id: Int, channel: ReceiveChannel<Int>) = launch {
        channel.consumeEach {
            println("Processos #$id received $it")
        }
    }


    fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
        for (chl in channel) {
            println("Processos #$id received $chl")
        }
    }

    fun checkProduce() = runBlocking {
        val producer = produceNumbers()
//        repeat(5) { launchProcessor(it, producer) }
        repeat(5) { launchProcessorEach(it, producer) }
        delay(2000L)
        producer.cancel()
    }


    fun channelCloseFoo() = runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
            channel.close()
        }
        repeat(3) {
            println("channel receive: ${channel.receive()}")
        }
//        for (y in channel){
//            println(y)
//        }
        println("End")
    }

    fun channelFoo() = runBlocking {
        val channel = Channel<Int>()
        launch {
            for (x in 1..5) channel.send(x * x)
        }
        repeat(3) {
            println("channel receive: ${channel.receive()}")
        }
//        for (y in channel){
//            println(y)
//        }
        println("End")
    }

    fun cancelScopeWithtimeoutOrNull() = runBlocking {
        var result = withTimeoutOrNull(1200L) {
            repeat(1000) { i ->
                println(" print NO. $i")
                delay(500L)
            }
        }
        println(" print result: $result")
    }

    fun cancelScopeWithtimeout() = runBlocking {
        withTimeout(1200L) {
            repeat(1000) { i ->
                println(" print NO. $i")
                delay(500L)
            }
        }
    }

    fun cancelScopeWithContext() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println(" print NO. $i")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    println("outer: Finally...")
                    delay(1000L)
                    println("outer: delay 1 sec in Finally...")
                }
            }
        }
        delay(1200L)
        println("outer: I'm tired of waiting...")
        job.cancelAndJoin()
        println("outer: Quit!")
    }

    fun cancelScopeFinally() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println(" print NO. $i")
                    delay(500L)
                }
            } finally {
                println("outer: Finally...")
            }

        }
        delay(1200L)
        println("outer: I'm tired of waiting...")
        job.cancelAndJoin()
        println("outer: Quit!")
    }

    fun cannotcancelScope2() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextTime = startTime
            var i = 0
            while (isActive) {
                if (System.currentTimeMillis() >= nextTime) {
                    println("Print NO.${i++}")
                    nextTime += 500L
                }
            }
        }
        delay(1200L)
        println("outer: I'm tired of waiting...")
        job.cancelAndJoin()
        println("outer: Quit!")
    }


    fun cannotcancelScope() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextTime = startTime
            var i = 0
            while (i < 5) {
                if (System.currentTimeMillis() >= nextTime) {
                    println("Print NO.${i++}")
                    nextTime += 500L
                }
            }
        }
        delay(1200L)
        println("outer: I'm tired of waiting...")
        job.cancelAndJoin()
        println("outer: Quit!")
    }

    fun cancelScope() = runBlocking {
        val job = launch {
            repeat(1000) { i ->
                println(" print NO. $i")
                delay(500L)
            }
        }
        delay(1200L)
        println("outer: I'm tired of waiting...")
//        job.cancel()
//        job.join()
        job.cancelAndJoin()
        println("outer: Quit!")
    }

    suspend fun CoroutineScope.suspendLaunch() {
        this.launch {
            doprint()
        }
    }

    suspend fun doprint() {
        delay(500L)
        println("Coroutine")
        GlobalScope.launch {
            println("doprint-globalScope")
        }
    }

    fun coroutineScopeLaunch() = runBlocking {
        launch { // 启动一个新协程
            doprint()
//            delay(500L)
//            println("Coroutine")
        }
        coroutineScope {// 创建一个协程作用域
            launch {// 协程作用域中可以创建子协程
                delay(3000L)
                println(" Nested launch")
            }
            delay(300L)
            println("coroutineScope")
        }
        println("runBlocking end")
    }

    fun unjoinLaunch() = runBlocking {
        launch {
            delay(1000L)
            println("Coroutine")
        }
        println("First")
        println("End")
    }

    fun joinLaunch() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
            println("Coroutine")
        }
        println("First")
        job.join()
        println("End")
    }


    fun runBlockingLaunch() = runBlocking {
        GlobalScope.launch {
            delay(1000L)
            println("Coroutine")
        }
        println("First")
        delay(2000L)
        println("End")
    }

    fun globalscopelaunch() {
        GlobalScope.launch {
            delay(1000L)
            println("Coroutine")
        }
        println("First")
//        Thread.sleep(2000L)
        runBlocking {
            delay(1000L)
        }
        println("End")
    }

}