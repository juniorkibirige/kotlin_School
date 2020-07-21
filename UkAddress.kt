package lan.tmsystem

class UkAddress(firstLine: String,
                secondLine: String,
                city: String,
                country: String,
                postCode: UkPostCode) : PostalAddress(firstLine, secondLine, city, country, postCode)

class UsAddress(firstLine: String,
                secondLine: String,
                city: String,
                country: String,
                zipCode: USZipCode) : PostalAddress(firstLine, secondLine, city, country, zipCode)