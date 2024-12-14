package com.mastertech.myapplication6

interface SerialListener {
        fun inSerialConnect()
        fun onSerialConnectError (e:Exception?)
        fun onSerialRead (data: ByteArray?)
        fun onSerialIoError (e: Exception?)

}