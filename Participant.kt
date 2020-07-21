package lan.tmsystem

class Participant(val name: Name, val email : String) {

    val participantName
        get() = name.name

    val canonicalEmail
        get() = email.toUpperCase()

    
}