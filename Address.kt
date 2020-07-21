package lan.tmsystem

abstract class PostalAddress(val firstLine: String,
        val secondLine: String,
        val city: String,
        val country: String,
        val postCode: PostalCode) : Location()