package lan.tmsystem.security

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object ParseArgs {
    private val argumentKeys = mutableMapOf<String, String>()

    val arguments = mutableMapOf<String, ArgumentInitializers>()

    private val strArguments = mutableMapOf<String, Any>()

    fun setupDefaultValues(argumentSetup: Array<ArgumentInitializers>) {

        initialzeKeyNameMaps(argumentSetup)

        initializeDefaultArgumentValues(argumentSetup)
    }

    operator fun get(argumentName: String): Any? {
        val argument = arguments[argumentName.toLowerCase()] ?: throw IllegalArgumentException(argumentName)

        return when(argument.type) {
            is ArgumentType.StringType -> {
                argument.type.value
            }
            is ArgumentType.BooleanType -> {
                argument.type.value
            }
        }
    }

    operator fun invoke(args: Array<String>) {
        var ndx = 0
        while (ndx < args.size) {

            val argKey = getTheArgumentKey(args, ndx)
            val argName = getTheArgumentNameFromTheKey(argKey)

            if (arguments[argName.toLowerCase()] == null) throw IllegalArgumentException()

            val argument: ArgumentInitializers = arguments[argName.toLowerCase()]!!

            when (argument.type) {
                is ArgumentType.StringType -> {
                    argument.type.value = args[++ndx]
                }
                is ArgumentType.BooleanType -> {
                    argument.type.value = true
                }
            }
            ndx++
        }

    }

    private fun getTheArgumentNameFromTheKey(argKey: String): String {
        when {
            argumentKeys[argKey.toLowerCase()] == null -> throw IllegalArgumentException(argKey)
            else -> return argumentKeys[argKey.toLowerCase()]!!
        }
    }

    private fun getTheArgumentKey(args: Array<String>, ndx: Int): String = args[ndx].toLowerCase()

    private fun initializeDefaultArgumentValues(argumentSetup: Array<ArgumentInitializers>) {
        argumentSetup.forEach {
            when (it.type) {
                is ArgumentType.StringType -> {
                    if (strArguments[it.name] == null)
                        strArguments[it.name] = it.type.value
                }
                is ArgumentType.BooleanType -> {
                    if (strArguments[it.name] == null)
                        strArguments[it.name] = it.type.value
                }
            }
        }
    }

    private fun initialzeKeyNameMaps(argumentSetup: Array<ArgumentInitializers>) {
        argumentSetup.forEach {
            argumentKeys[it.key.toLowerCase()] = it.name.toLowerCase()
            arguments[it.name.toLowerCase()] = it
        }
    }

}

sealed class ArgumentType {
    class StringType(var value: String = "") : ArgumentType()
    class BooleanType(var value: Boolean = false) : ArgumentType()
}

data class ArgumentInitializers(val name: String,
                                val type: ArgumentType,
                                val key: String)


object Argument {
    fun <T> argument():
            ReadOnlyProperty<Any, T> = object : ReadOnlyProperty<Any, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: Any, property: KProperty<*>): T {
            return ParseArgs[property.name] as T
        }
    }
}