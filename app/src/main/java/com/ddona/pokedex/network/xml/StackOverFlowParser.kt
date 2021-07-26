package com.ddona.pokedex.network.xml

import android.util.Log
import com.ddona.pokedex.model.Question
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.net.URL

object StackOverFlowParser {

    fun parserLink(link: String): List<Question> {

        val url = URL(link)
        val http = url.openConnection()

        val parserFactory = XmlPullParserFactory.newInstance()
        val xmlPullParser = parserFactory.newPullParser()
        xmlPullParser.setInput(http.getInputStream(), "utf-8")

        var text = ""
        var eventType = xmlPullParser.eventType
        var startParsing = false
        var question: Question? = null
        val questions = mutableListOf<Question>()
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    val startTag = xmlPullParser.name
                    if (startTag == "entry") {
                        startParsing = true
                        question = Question()
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (!startParsing) {
                        eventType = xmlPullParser.next()
                        continue
                    }
                    val endTag = xmlPullParser.name
                    if (endTag == "id") {
                        question?.url = text
                    } else if (endTag == "title") {
                        question?.title = text
                    } else if (endTag == "name") {
                        question?.author = text
                    } else if (endTag == "published") {
                        question?.pubDate = text
                    } else if (endTag == "summary") {
                        question?.summary = text
                    } else if (endTag == "entry") {
                        questions.add(question!!)
                    }
                }
                XmlPullParser.TEXT -> {
                    if (!startParsing) {
                        eventType = xmlPullParser.next()
                        continue
                    }
                    text = xmlPullParser.text
                }
            }
            eventType = xmlPullParser.next()
        }
        return questions
    }
}