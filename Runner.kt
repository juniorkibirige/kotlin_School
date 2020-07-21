package lan.tmsystem

import java.nio.file.Paths

fun main(){

    val postCode = USZipCode("12345")
    val logger : Logger = FileLogger(Paths.get("/some/file.log"))
    val meeting = Meeting("Review", UkAddress("a house", "a street", "a town", "a country", UkPostCode("12345")), logger)
    val review = PersonalReview(meeting.meetingName, Participant(Name("Alice"), ""), listOf(), Room("Room 1"), logger)

    print("Created:  $review with name ${review.meetingName} and at ${review.locationName}")
    review.closeReview()
    val name = Name("Kevin Jones")
    val participant = Participant(name, "info@tmsystem.live")

    val canonical_email = participant.canonicalEmail

    meeting.addParticipant(participant)
    canonical_email.compareTo(participant.email)
}