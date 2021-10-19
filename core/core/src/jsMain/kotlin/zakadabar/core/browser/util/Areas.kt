/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package zakadabar.core.browser.util

import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import zakadabar.core.browser.util.w3c.IntersectionObserver
import zakadabar.core.browser.util.w3c.IntersectionObserverEntry
import zakadabar.core.resource.css.percent
import zakadabar.core.resource.css.px
import kotlin.math.floor

/**
 * Uses an intersection observer to figure out which parts of the content is
 * shown to the user at the moment.
 *
 * Call [adjustAreas] to add / remove the necessary number of areas of fixed height
 * to the content [element].
 *
 * Areas have a fixed height (may be set in the constructor parameter [areaHeight]
 * and a z-index of 10, so they will be under the actual content.
 *
 * When an area is about to be shown the [changed] function is called, so the caller
 * can render the content of that area. Content render is usually pretty fast,
 * so this should be seamless from the user point of view.
 *
 * Trace level above 100 shows area changes.
 */
class Areas(
    val id: String,
    val changed: () -> Unit,
    val element: HTMLElement,
    val trace: Int = 0,
    val areaHeight: Float = 1000f
) {
    private lateinit var observer: IntersectionObserver
    val activeAreas = mutableListOf<Int>()

    protected var areaNumber = 0
    protected var lastAreaHeight = 0f

    internal var start = 0f
    internal var end = 0f

    fun onCreate() {
        val options: dynamic = object {}
        options["root"] = element.parentElement
        options["rootMargin"] = "200px" // pre-load when the area is as close as 200px to the screen
        options["threshold"] = 0 // pre-load when even a pixel of the area is about to be shown

        observer = IntersectionObserver(observerCallback, options)
    }

    fun onDestroy() {
        removeAreas(0)
        observer.disconnect()
    }

    val observerCallback = fun(entries: Array<IntersectionObserverEntry>, _: IntersectionObserver) {
        try {
            entries.forEach {
                val target = it.target as HTMLElement
                val index = target.id.substringAfterLast('-').toInt()

                if (it.isIntersecting) {
                    activeAreas.add(index)
                } else {
                    activeAreas.remove(index)
                }
            }

            activeAreas.sort()

            start = if (activeAreas.size == 0) 0f else activeAreas.first() * areaHeight
            end = if (activeAreas.size == 0) 0f else (activeAreas.last() + 1) * areaHeight

            // stdoutTrace("areas changed", "start: $start, end: $end, active: $activeAreas")

            changed()

        } catch (ex: Throwable) {
            log(ex)
        }
    }

    fun adjustAreas(height: Float) {

        val currentHeight = (areaNumber - 1) * areaHeight + lastAreaHeight
        val diff = height - currentHeight

        if (diff == 0f) return // no difference, nothing to do

        var neededAreaNumber = floor(height / areaHeight).toInt()

        if (neededAreaNumber * areaHeight != height) {
            lastAreaHeight = height - (neededAreaNumber * areaHeight)
            neededAreaNumber += 1
        } else {
            lastAreaHeight = areaHeight
        }

        if (areaNumber < neededAreaNumber) {
            val area = document.getElementById("${id}-area-${areaNumber-1}") as? HTMLElement
            area?.style?.height = "${areaHeight}px"
            addAreas(neededAreaNumber)
        } else {
            removeAreas(neededAreaNumber)
        }

        areaNumber = neededAreaNumber

        val lastArea = document.getElementById("${id}-area-${areaNumber - 1}") as? HTMLElement ?: return
        lastArea.style.height = "${lastAreaHeight}px"
    }

    protected fun addAreas(neededAreaNumber: Int) {
        for (i in areaNumber until neededAreaNumber) {
            val area = document.createElement("div") as HTMLElement
            area.id = "${id}-area-$i"
            area.style.position = "absolute"
            area.style.width = 100.percent
            area.style.height = areaHeight.px
            area.style.top = (i * areaHeight).px
            area.style.zIndex = "10"
            element.appendChild(area)
            observer.observe(area)
        }
    }

    protected fun removeAreas(neededAreaNumber: Int) {
        for (i in neededAreaNumber until areaNumber) {
            val area = document.getElementById("${id}-area-$i") ?: continue
            observer.unobserve(area)
            area.remove()
        }
    }

}