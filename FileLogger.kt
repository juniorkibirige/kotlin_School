package lan.tmsystem

import java.nio.file.Path

interface Message {
    fun info() {

    }
}

class FileLogger(val file : Path) : Logger, Message {
    override fun debug(msg : String) {

    }

    override fun warn(msg : String) {
        
    }

    override fun info(msg : String) {
        super<Logger>.info(msg)
        super<Message>.info()
    }
}

class ConsoleLogger() : Logger {
    override fun debug(msg : String) {

    }

    override fun warn(msg : String) {
        
    }
}