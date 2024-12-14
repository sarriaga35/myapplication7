package com.mastertech.myapplication6

class Constants {
    //valores unicos y globales, CONSTANTES

    companion object {
        const val TAG = "simple-terminal03"
        const val INTENT_ACTION_DISCONNECT: String = BuildConfig.APPLICATION_ID.toString() + ".Desconecta"
        const val NOTIFICATION_CHANNEL: String = BuildConfig.APPLICATION_ID.toString() + ".Canal"
        const val INTENT_CLASS_MAIN_ACTIVITY: String = BuildConfig.APPLICATION_ID.toString() + ".MainActivity"

        // values have to be unique within each app
        const val NOTIFY_MANAGER_START_FOREGROUND_SERVICE = 1001
    }
}