package lan.tmsystem

import lan.tmsystem.security.*

fun main(args: Array<String>) {
    val ishelp = if(args.isNotEmpty()) args[0] else "";

    if (args.size < 2 || ishelp == " --help") {
        println("usage: java SecurityTools --help:hash|sign|providers")

        
        println("signing: java SecurityToolsKt [-op 'sign'] -s [-f filename] [-d signaturefile] [-p provider] [-a algorithm]")
        // println("providers: java SecurityToolsKt [-op 'providers'] [-f filename] [-d destfileName] [-p provider] [-a algorithm] [-o] [-encode]")
    }

    if(ishelp.startsWith("--help")) {
        val helpon = ishelp.split(":")[1]
        when(helpon) {
            "providers" -> Providers.Help.help()
            "hash" -> Hash.Help.help()
            "sign" -> Sign.Help.help()
        }
        return
    }

    ParseArgs.setupDefaultValues(
        arrayOf(
            ArgumentInitializers("operation", ArgumentType.StringType(), "-op"),
            ArgumentInitializers("algorithm", ArgumentType.StringType(), "-a"),
            ArgumentInitializers("keystoretype", ArgumentType.StringType("JKS"), "-keystoretype"),
            ArgumentInitializers("encode", ArgumentType.BooleanType(true), "-encode"),
            ArgumentInitializers("sign", ArgumentType.BooleanType(true), "-s"),
            ArgumentInitializers("verify", ArgumentType.BooleanType(), "-v"),
            ArgumentInitializers("keystoreFilename", ArgumentType.StringType(), "-keystore"),
            ArgumentInitializers("keypass", ArgumentType.StringType(), "-keypass"),
            ArgumentInitializers("keyStorePass", ArgumentType.StringType(), "-keystorepass"),
            ArgumentInitializers("keyAlias", ArgumentType.StringType(), "-alias"),
            ArgumentInitializers("provider", ArgumentType.StringType(), "-p"),
            ArgumentInitializers("fileName", ArgumentType.StringType(), "-f"),
            ArgumentInitializers("destFileName", ArgumentType.StringType(), "-d"),
            ArgumentInitializers("signatureFileName", ArgumentType.StringType(), "-sigfilename"),
            ArgumentInitializers("overwrite", ArgumentType.BooleanType(), "-o"),
            ArgumentInitializers("decode", ArgumentType.StringType(), "-decode"),
            ArgumentInitializers("filter", ArgumentType.StringType(), "-filter")
        )
    )

    ParseArgs(args)

    val type: ArgumentType.StringType = ParseArgs.arguments.get("operation")!!.type as ArgumentType.StringType

    when(type.value.toLowerCase()) {
        "hash" -> {
            Hash(ConsoleOutputStrategy()).run()
        }

        "sign" -> {
            Sign(ConsoleOutputStrategy()).run()
        }

        "providers" -> {
            Providers(ConsoleOutputStrategy()).run()
        }
    }
}