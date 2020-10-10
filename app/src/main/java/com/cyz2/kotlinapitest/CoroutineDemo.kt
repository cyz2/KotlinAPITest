package com.cyz2.kotlinapitest

import kotlinx.coroutines.*
import java.lang.Math.random
import java.lang.Thread.sleep

// 协程
//        CoroutineScope    顶级协程，有个CoroutineContext类型的参数，在launch一个协程时，可以传入CoroutineContext类型对象，如Dispatcher、Job及CoroutineExceptionHandler等
//        GlobalScope       GlobalScope.launch创建最顶层的协程

//        supervisorScope
//        runBlocking       创建协程，会阻塞当前线程

//        suspend

//        withTimeout   设置协程挂起的时间限制
//        launch    创建协程返回Job，可以控制协程声明周期
//            分析launch的参数
//        async     创建协程返回Deferred，与await完成并发

//        SupervisorJob

//        Job

//        isActive
//        isCompleted
//        isCancelled
//        start()
//        cancel()
//        join() //使用场景是：一个协程依赖于另一个协程的运行结果，所以需要调用另一个协程的join函数知道它运行完成
//        cancelAndJoin() 取消协程并等待协程job结束
//            取消协程往往需要等待job协程结束后调用join，否则可能会出现job还没结束主协程就一个结束的情况。
//            cancel() + join() = cancelAndJoin()
//        isActive
//        NonCancellable
//        withTimeout

//        Dispatcher        指定工作线程
//        withContext       灵活切换
//        CoroutineExceptionHandler     全局捕获异常
class CoroutineDemo {
    fun check() {
        globalScopeLaunch()
//        runBlockingLaunch()
//        joinlaunch()
       // val scope = CoroutineScope(SupervisorJob())
    }

    /*
    launch启动一个新协程，
    launch()是CoroutineScope的扩展函数，所以协程启动必须有一个CoroutineScope对象
    GlobalScope作用域是全局的
    通过GlobalScope启动的协程会被协程应用程序的声明周期限制，在程序退出后，协程也会推出。
    * */
    fun globalScopeLaunch() {
        val job = GlobalScope.launch {
            for (i in 0..9) {
                println("子协程执行第${i}次")
                val sleepTime = (1000 * random()).toLong()
                delay(sleepTime)    //只能用在协程中，将协程任务挂起
                if (i == 8) {
                    this.cancel()
                    return@launch
                }
            }
            println("子协程结束")
        }

        println("Job is Active: ${job.isActive}")
        println("Job is Cancelled: ${job.isCancelled}")
        println("Job is Completed: ${job.isCompleted}")
//        delay(10000L) // delay是suspend挂起函数，必须用在协程或是其他可挂起函数中
        runBlocking{
            // 主线程在执行到runBlocking代码块的时候会被阻塞，直到代码块内执行完毕
//            delay(10000L)
            job.join() // runBlocking协程会挂起，直到job协程执行完毕，而不是死死的等10秒
        }

//        sleep(10000L)     // 需要将主线程阻塞以防止进程推出

        println("主协程结束")
        println("Job is Active: ${job.isActive}")
        println("Job is Cancelled: ${job.isCancelled}")
        println("Job is Completed: ${job.isCompleted}")
    }

    fun runBlockingLaunch() = runBlocking<Unit> {// 使用runBlocking会阻塞主线程知道runBlocking代码块执行完毕

        println("主协程开始。。。")
        val job = launch {
            run("Job1")
        }
        // 启动一个带超时机制（2秒）的协程
        // 超时直接抛出异常：kotlinx.coroutines.TimeoutCancellationException: Timed out waiting for 2000 ms
//        val job2 = withTimeout(2000L) {
//            run("Job2")
//        }
//        delay(5000L)    //主协程挂起
        println("主协程结束。。。")

    }

    suspend fun run(name: String) {
        for (i in 0..5) {
            println("子协程${name}执行第${i}次")
            val sleepTime = (1000 * random()).toLong()
            delay(sleepTime)    //协程挂起
        }
        println("子协程${name}结束")
    }

    var value = 0
    fun joinlaunch() = runBlocking {
        println("主协程开始")
        //创建协程job1
        val job1 = launch {
            println("子协程开始。。。")
            for(i in 0..4){
                value++
            }
            println("子协程结束...value:$value")
        }
        job1.join() //主协程挂起，等待job1子协程结束
        println("value = $value")
        println("主协程结束...")
    }
}