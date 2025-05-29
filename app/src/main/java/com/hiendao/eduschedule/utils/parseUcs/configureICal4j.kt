package com.hiendao.eduschedule.utils.parseUcs

import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.Component
import net.fortuna.ical4j.model.component.CalendarComponent
import net.fortuna.ical4j.model.component.VEvent
import java.io.File
import java.io.FileInputStream

fun configureICal4j() {
    System.setProperty(
        "net.fortuna.ical4j.timezone.cache.impl",
        "net.fortuna.ical4j.util.MapTimeZoneCache"
    )
}

fun parseIcsFile(icsFile: File) {
    configureICal4j()
    val calendar = CalendarBuilder().build(FileInputStream(icsFile))

    calendar.getComponents<CalendarComponent>(Component.VEVENT).forEach {
        val event = it as VEvent
        val summary = event.summary.value
        val location = event.location.value
        val description = event.description.value
        val startDate = event.startDate.date
        val endDate = event.endDate.date

        println("event: $summary at $location from $startDate to $endDate with description: $description")
    }
}
