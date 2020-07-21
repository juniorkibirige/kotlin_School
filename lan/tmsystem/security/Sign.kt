package lan.tmsystem.security

import lan.tmsystem.security.Argument.argument
import lan.tmsystem.security.logger
import java.io.FileInputStream
import java.security.KeyStore
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.util.*

class Sign(outputStrategy: OutputStrategy) : SecurityBase(outputStrategy) {

    private val logger by logger()

    val keystoreType: String by argument()
    val keystoreFilename: String by argument()

    val keypass: String by argument()
    val keyStorePass: String by argument()
    val keyAlias: String by argument()
    val signatureFileName: String by argument()

    val decode: Boolean by argument()
    val verify: Boolean by argument()

    private var keystore: KeyStore? = null

    override fun run() {
        val signature = createSignatureInstance()

        val isDataToSignOrVerify = createInputStream(fileName)
        val os = createOutputStream(destFileName)

        val ks = loadKeyStore()
        keystore = ks

        if(verify) {
            val isSig = createInputStream(signatureFileName)
            if(verifyData(signature, readBytes(isDataToSignOrVerify), readBytes(isSig))) {
                logger.info("Data verified")
            } else {
                logger.info("Data not verified")
            }
        } else {
            val bytesToSign = readBytes(isDataToSignOrVerify)
            val signedBytes = signData(signature, bytesToSign)
            writeBytes(os, signedBytes)

            if (destFileName.isEmpty()) {
                logger.info("Data signed to file: $destFileName")
            } else 
                logger.info("Data signed")
        }
    }

    private fun signData(signature: Signature, bytesToSign: ByteArray): ByteArray {
        val pk = loadPrivateKey()
        signature.initSign(pk)
        signature.update(bytesToSign)
        val signedBytes = signature.sign()
        return signedBytes
    }

    private fun loadPrivateKey(): PrivateKey {
        val alias = keyAlias
        val password = keypass

        if (alias.isEmpty() || password.isEmpty())
            throw IllegalArgumentException("Either alias or password is empty")
        
        return keystore?.getKey(alias, password.toCharArray()) as PrivateKey
    }

    private fun verifyData(signature: Signature, bytesToVerify: ByteArray, bytesSignature: ByteArray): Boolean {
        var internalByteSignature = bytesSignature
        val key = loadPublicKey()
        signature.initVerify(key)

        if(decode)
            internalByteSignature = decodeData(bytesSignature)
        signature.update(bytesToVerify)
        return signature.verify(internalByteSignature)
    }

    private fun decodeData(bytesSignature: ByteArray): ByteArray {
        val decoder = Base64.getDecoder()

        return decoder.decode(String(bytesSignature))
    }

    private fun createSignatureInstance(): Signature {
        val sign = if (provider.isEmpty()) {
            Signature.getInstance(algorithm)
        } else {
            Signature.getInstance(algorithm, provider)
        }
        return sign
    }

    private fun loadPublicKey(): PublicKey? {
        val alias = keyAlias
        return keystore?.getCertificate(alias)?.publicKey
    }

    private fun loadKeyStore(): KeyStore {
        if (keystoreType.isEmpty() || keystoreFilename.isEmpty())
            throw IllegalArgumentException("Either keystore type or keystore name is null")
        val keystore = KeyStore.getInstance(keystoreType)
        keystore.load(FileInputStream(keystoreFilename), keyStorePass.toCharArray())
        return keystore
    }

    object Help {
        fun help() {
            println("signing: java SecurityTools [-op 'sign'] -s [-f filename] [-d signaturefile] [-p provider] [-encode] [-o] [-a algorithm]")
            println("\t\t[-o] [-encode] [-keystoretype keystoretype] [-keyStorePass storepassword]")
            println("\t\t-keystore storename -keypass keypassword -alias alias")
            println()
            println("verifying: java SecurityTools [-op 'sign'] -v [-f filename] [-p provider] [-a algorithm] [-decode]")
            println("\t\t[-keystoretype keystoretype] [-sigfilename signaturefilename]")
            println("\t\t[-keyStorePass storepassword]")
            println("\t\t-keystore storename -keypass keypassword -alias alias")
            println()
            println("\tf filename\t: read input data from filename")
            println("\td signaturefile\t: write output signature to signaturefile")
            println("\tp provider\t: use specific provider")
            println("\ta algorithm\t: algorithm to use for digest")
            println("\to\t\t: overwrite destfilename file")
            println("\tencode\t\t: BASE64 encode output")
            println("\tdecode\t\t: BASE64 decode intput")
            println("\ts\t\t: sign data")
            println("\tv\t\t: verify signature")
            println("\tsigfilename\t: name of file containing signature (if verifying)")
            println("\tkeystoretype\t: type of keystore in use")
            println("\tstorepasst\t: password to keystore")
            println("\tkeystore\t\t: keystore")
            println("\tkeypass\t\t: password for key")
            println("\talias\t\t: alias for key")
        }
    }
}