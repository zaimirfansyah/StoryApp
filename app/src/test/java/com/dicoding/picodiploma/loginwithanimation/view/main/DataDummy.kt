package com.dicoding.picodiploma.loginwithanimation.view.main

import com.dicoding.picodiploma.loginwithanimation.api.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "CreatedAt + $i",
                "name $i",
            )
            items.add(story)
        }
        return items
    }
}