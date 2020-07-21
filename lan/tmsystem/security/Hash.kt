package lan.tmsystem.security

import lan.tmsystem.security.Argument.argument
import lan.tmsystem.security.logger
import java.util.logging.Logger
import java.io.InputStream
import java.security.MessageDigest

class Hash(outputStrategy: OutputStrategy) : SecurityBase(outputStrategy) {

    private val logger: Logger by logger()

    object Help {
        fun help() {
            println("hashing: java Hash -op 'hash' [-f filename] [-d destfilename] [-p provider] [-a algorithm] [-o] [-encode]")
            println("\tf filename\t: read input data from filename")
            println("\td destfilename\t: write output hash to destfilename")
            println("\tp provider\t: use specific provider")
            println("\ta algorithm\t: algorithm to use for digest")
            println("\to\t\t: overwrite destfilename file")
            println("\te\t\t: BASE64 encode output")
        }
    }

    init {
        if(algorithm.isEmpty()) throw IllegalArgumentException()
    }

    override fun run() {
        val md = createDigestInstance(algorithm, provider)
        logger.info("About to digest using ${md.algorithm} from ${md.provider}")
        
        createInputStream(fileName).use { input -> 
            createOutputStream(destFileName).use { output ->
                
                val hashBytes = digestData(md, input)
                writeBytes(output, hashBytes)
                logger.info("Data digested using ${md.algorithm} from ${md.provider}")
            }
        }
    }

    private fun digestData(md: MessageDigest, input: InputStream): ByteArray {
        val bytesToHash = input.readBytes()
        md.update(bytesToHash)
        return md.digest()
    }

    private fun createDigestInstance(algorithm: String, provider: String?): MessageDigest {
        return if (provider.isNullOrEmpty()) {
            MessageDigest.getInstance(algorithm)
        } else {
            MessageDigest.getInstance(algorithm, provider)
        }
    }

}