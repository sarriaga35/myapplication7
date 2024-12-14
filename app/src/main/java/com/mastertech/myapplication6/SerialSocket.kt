package com.mastertech.myapplication6

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.mastertech.myapplication6.Constants.Companion.TAG
import java.io.IOException
import java.util.UUID
import java.util.concurrent.Executors

class SerialSocket : Runnable {
    private  val BLUETOOTH_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var connectado = true
    private var btSocket : BluetoothSocket? = null
    private var context: Context? = null
    private var socketListener: SerialListener? = null
    private var btDevice: BluetoothDevice? = null
    private var disconnectBroadcastReceiver: BroadcastReceiver? = null


    init {
        disconnectBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                //operator safe call, se utiliza para que en caso de que el Observador del socket,
                //llamado aqui socketListener,  es Non-null envia el mensaje "desconecta el servicio"
                socketListener?.onSerialIoError(IOException  ("background disconnect"))
                desconnectar()
            }
        }

    }
    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    fun connectar(context: Context, listener: SerialListener?, device: BluetoothDevice?) {
        Log.i(TAG,"Antes del if already connected"+ connectado.toString())

//        if (connectado || btSocket != null) throw IOException("ALREADY CONNECTED")

        this.context = context
        socketListener = listener
        btDevice = device
        Log.i(TAG,"Continua flujo")
        context.registerReceiver(disconnectBroadcastReceiver,
            IntentFilter(Constants.INTENT_ACTION_DISCONNECT)
        )
        Executors.newSingleThreadExecutor().submit(this)
    }


    private fun desconnectar() {
        socketListener = null
        try {
            btSocket?.close()
        } catch (ignored : Exception) {
        }
        btSocket = null
        try {
            context!!.unregisterReceiver(disconnectBroadcastReceiver)
        } catch ( ignored : Exception) {
        }
    }
    fun writeSocket(data: ByteArray?) {
        if (!connectado) throw IOException("not CONNECTED")
        btSocket!!.outputStream.write(data)
    }


    @SuppressLint("MissingPermission")
    override fun run() {
        try {
            btSocket = btDevice!!.createRfcommSocketToServiceRecord(BLUETOOTH_SPP)
            btSocket?.connect()
            socketListener?.inSerialConnect()
        } catch (e:    Exception) {
            socketListener?.onSerialConnectError(e)
            try {
                btSocket!!.close()
            } catch (ignored: Exception) {
            }
            btSocket = null
            return
        }
        connectado = true
        try {
            val buffer = ByteArray(1024)
            var len: Int
            //non inspection Infinite Loop Statement
            while (true) {
                len = btSocket!!.inputStream.read(buffer)
                val data = buffer.copyOf(len)
                socketListener?.onSerialRead(data)
            }
        } catch (e: Exception) {
            connectado = false
            socketListener?.onSerialIoError(e)
            try {
                btSocket?.close()
            } catch (ignored: Exception) {
            }
            btSocket = null
        }
    }
}