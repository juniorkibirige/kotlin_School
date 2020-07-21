package lan.tmsystem.security

interface OutputStrategy {
    fun write(value: String)
    fun writeHeader()
    fun writeFooter()
}

class ConsoleOutputStrategy : OutputStrategy {
    override fun write(value: String) {
        println(value)
    }
    override fun writeHeader() {
        println("---------------------------------------------------------")
    }
    override fun writeFooter() {
        println("---------------------------------------------------------")
    }
}
