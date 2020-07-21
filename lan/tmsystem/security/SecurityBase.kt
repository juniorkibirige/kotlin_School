package lan.tmsystem.security
import java.io.*
import java.util.*
import lan.tmsystem.security.Argument.argument

abstract class SecurityBase(protected val outputStrategy: OutputStrategy) {
    abstract fun run()
    object Help

    protected val algorithm: String by argument()
    protected val fileName: String by argument()
    protected val destFileName: String by argument()
    protected val provider: String by argument()
    private val overwrite: Boolean by argument()
    private val encode: Boolean by argument()

    @Throws(IOException::class)
    fun createOutputStream(fileName: String): OutputStream {
        return if(fileName.isEmpty())
            System.out
        else {
            val file = File(fileName)

            val fileOutputStream = if(file.exists()) {
                if(overwrite)
                    FileOutputStream(file)
                else
                    throw IOException("Destination file already exists")
            } else {
                FileOutputStream(file)
            }
            fileOutputStream
        }
    }

    @Throws(FileNotFoundException::class)
    fun createInputStream(fileName: String): InputStream {
        return if (fileName.isEmpty())
            System.`in`
        else {
            val f = File(fileName)
            if(f.exists()) {
                FileInputStream(f)
            } else {
                throw FileNotFoundException()
            }
        }
    }

    @Throws(IOException::class)
    fun writeBytes(os: OutputStream, bytes: ByteArray) {
        if(encode) {
            val temp: String
            val encoder = Base64.getEncoder()
            temp = encoder.encodeToString(bytes)
            os.write(temp.toByteArray())
        } else {
            os.write(bytes)
        }
        os.flush()
    }

    @Throws(IOException::class)
    fun readBytes(inStream: InputStream): ByteArray {
        return inStream.readBytes()
    }
}