package com.hakif.StoryApp

import com.hakif.StoryApp.data.network.response.story.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1687337091374_dummy-pic.png",
                "2023-06-21T14:44:51.374Z",
                "User $i",
                "Description $i",
                "40.4319",
                "$i",
                "116.5704"
            )
            items.add(story)
        }
        return items
    }
}