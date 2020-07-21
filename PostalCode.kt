package lan.tmsystem

import java.util.regex.*

abstract class PostalCode(val postCode: String) {
    abstract fun verify()

    init {
        verify()
    }
}

class USZipCode(zipCode : String) : PostalCode(zipCode) {

    override fun verify() {
        val pattern : Pattern! = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$")
        val matcher = pattern.matcher(postCode)
        if(!matcher.matches()) throw IllegalArgumentException()
    }
}



class UkPostCode(postCode : String) : PostalCode(postCode) {

    override fun verify() {
        
    }
}