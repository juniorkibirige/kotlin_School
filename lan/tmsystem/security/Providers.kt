package lan.tmsystem.security

import lan.tmsystem.security.Argument.argument
import java.security.Provider
import java.security.Security

class ProviderDetails(val providerName: String, val name: String)

class Providers(outputStrategy: OutputStrategy) : SecurityBase(outputStrategy) {

    object Help {
        fun help() {
            println("providers: java SecurityToolsKt [-op 'providers'] [-f filename] [-d destfileName] [-p provider] [-a algorithm] [-o] [-encode]")
        }
    }

    private val filter: String by argument()

    override fun run() {
        listAllProviders()
    }

    private fun listAllProviders() {
        if(filter.isEmpty()) {
            getProviders().forEach {
                display(it)
            }
        } else {
            getFilteredProviders().forEach{
                display("${it.providerName}: ${it.name}")
            }
        }
    }

    private fun display(provider: Provider) {
        outputStrategy.write(provider.name)
        outputStrategy.writeHeader()

        provider.entries.forEach { entry -> 
            outputStrategy.write("\t ${entry.key}, ${entry.value}")
        }
        
        outputStrategy.writeFooter()
    }

    private fun getProviders() : List<Provider> {
        val providers = Security.getProviders()
        val list: List<Provider> = providers.asList()
        return list
    }

    private fun display(message: String) {
        outputStrategy.write(message)
    }

    private fun getFilteredProviders(): List<ProviderDetails> {
        return Security.getProviders().flatMap { provider -> 
            provider.entries
                    .filter { it -> it.key.toString().contains(filter, true) }
                    .map { ProviderDetails(provider.name, it.key.toString()) }
        }
    }
}