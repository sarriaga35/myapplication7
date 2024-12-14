package com.mastertech.myapplication6

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.mastertech.myapplication6.Constants.Companion.TAG
import java.io.IOException

import java.util.LinkedList
import java.util.Queue

class SerialService: Service(), SerialListener {

    private enum class QueueType { Connect,ConnectError, Read, IoError}
    private class QueueItem
    internal constructor(var type:QueueType, var data: ByteArray?, var e: Exception?)

    inner class SerialBinder: Binder() {
        fun getService(): SerialService {
            Log.i(TAG,"getService innerclass service")
            return this@SerialService
        }
    }

    private var mainLooper: Handler? = null
    private var binder:IBinder? = null
    private var queue1:Queue<QueueItem>? = null
    private var queue2:Queue<QueueItem>? = null

    private var ssListener:SerialListener? = null
    private var connected = false
    private val ssSocket: SerialSocket? = null

    //Constructor of SerialService class
    init {
        mainLooper = Handler(Looper.getMainLooper())
        binder = SerialBinder()
        queue1 = LinkedList()
        queue2 = LinkedList()

    }


    override fun onBind(p0: Intent?): IBinder? {
       return binder
    }
    fun disconnect() {
        ssListener = null
        connected = false
    }
    fun connect(listener: SerialListener) {
        this.ssListener = listener
        connected = true
    }
    fun attach(listener: SerialListener?) {
        require(!(Looper.getMainLooper().thread !== Thread.currentThread())) {"Not in Main Thread"}
        //use synchronized() to preventnew items in queue2
        //new items will not be added to queue1 because mainLooper.post and attach() run in main Thread
        if (connected) {
            synchronized(this) { ssListener = listener}
            for (item in queue1!!) {
                when (item.type) {
                    QueueType.Connect -> ssListener?.inSerialConnect()
                    QueueType.Read -> ssListener!!.onSerialRead(item.data)
                    QueueType.IoError -> ssListener!!.onSerialIoError(item.e)
                    QueueType.ConnectError -> ssListener!!.onSerialConnectError(item.e)
                }
            }
            for (item in queue2!!) {
                when (item.type) {
                    QueueType.Connect -> ssListener?.inSerialConnect()
                    QueueType.Read -> ssListener!!.onSerialRead(item.data)
                    QueueType.IoError -> ssListener!!.onSerialIoError(item.e)
                    QueueType.ConnectError -> ssListener!!.onSerialConnectError(item.e)
                }
            }
        }
        queue1!!.clear()
        queue2!!.clear()
    }

    fun detach() {
        // items already in event queue (posted before detach() to mainLooper) will end up in queue1
        // items occurring later, will be moved directly to queue2
        // detach() and mainLooper.post run in the main thread, so all items are caught
        ssListener = null
    }
    @Throws(IOException::class)
    fun writeService(data: ByteArray?) {
        if (!connected) throw IOException("not connected")
        ssSocket!!.writeSocket(data)
    }


    override fun inSerialConnect() {
        if (connected) {
            synchronized(this) {
                if (ssListener != null) {
                    mainLooper!!.post {
                        if (ssListener != null) {
                            ssListener!!.inSerialConnect()
                        } else {
                            queue1!!.add(QueueItem(QueueType.Connect,null,null))
                        }
                    }
                } else {
                    queue2!!.add(QueueItem(QueueType.Connect,null,null))
                }
            }
        }
    }

    override fun onSerialConnectError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                if (ssListener != null) {
                    mainLooper!!.post {
                        if (ssListener != null) {
                            ssListener!!.onSerialConnectError(e)
                        } else {
                            queue1!!.add(QueueItem(QueueType.ConnectError,null,e))
                            disconnect()
                        }
                    }
                } else {
                    queue2!!.add(QueueItem(QueueType.ConnectError,null,e))
                    disconnect()
                }
            }
        }
    }

    override fun onSerialRead(data: ByteArray?) {
        if (connected) {
            synchronized(this) {
                if (ssListener != null) {
                    mainLooper!!.post {
                        if (ssListener != null) {
                            ssListener!!.onSerialRead(data)
                        } else {
                            queue1!!.add(QueueItem(QueueType.Read,data,null))
                        }
                    }
                } else {
                    queue2!!.add(QueueItem(QueueType.Read,data,null)
                    )
                }
            }
        }
    }

    override fun onSerialIoError(e: Exception?) {
        if (connected) {
            synchronized(this) {
                if (ssListener != null) {
                    mainLooper!!.post {
                        if (ssListener != null) {
                            ssListener!!.onSerialIoError(e)
                        } else {
                            queue1!!.add(QueueItem(QueueType.IoError,null,e))
                            disconnect()
                        }
                    }
                } else {
                    queue2!!.add(QueueItem(QueueType.IoError,null,e))
                    disconnect()
                }
            }
        }
    }
}