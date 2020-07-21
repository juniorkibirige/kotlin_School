package lan.tmsystem

class Name(val name : String) {
        init {
            if(name.isNullOrBlank()) throw IllegalArgumentException()
        }
}