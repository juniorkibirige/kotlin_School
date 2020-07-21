package lan.tmsystem

open class Meeting(val meetingName: String, open val location: Location, val logger : Logger) {

    // private val logger : Logger = FileLogger(Paths.get("some/file.txt"))
    // private val logger : Logger = ConsoleLogger()

    open val locationName = ""
    // val meetingName: String

    fun addParticipant(name: Participant) {
        logger.info("Participant added")
        if(verifyParticipant(name))
            println("Added: $name.participantName")
    }

    private fun verifyParticipant(name: Participant) : Boolean {
        println("Try to verify: $name.participantName")
        return true
    }

    protected open fun verifyMeeting() {
        println("Meeting: verify meeting")
    }
}

class PersonalReview(meetingName: String, val employee:Participant, reviewers: List<Participant>, override val location: Room, logger: Logger) 
    : Meeting(meetingName, location, logger) {
        override val locationName
            get() = location.roomName
        
        fun closeReview() {
            println("Review ended")
            verifyMeeting()
        }

        override fun verifyMeeting() {
            println("PersonalReview: verify meeting")
            super.verifyMeeting()
        }
}