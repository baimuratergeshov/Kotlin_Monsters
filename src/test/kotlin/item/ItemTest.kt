package org.example.item

import org.example.rival
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ItemTest {
    @Test
    fun `item stocke ses trois proprietes`() {
        val item = Item(1, "Potion", "Soigne")
        assertEquals(1, item.id)
        assertEquals("Potion", item.nom)
        assertEquals("Soigne", item.description)
    }

    @Test
    fun `badge herite d'item et a un champion`() {
        val badge = Badge(2, "Badge Roche", "Roche", rival)
        assertTrue(badge is Item)
        assertEquals("Badge Roche", badge.nom)
        assertEquals("Regis", badge.champion.nom)
    }

    @Test
    fun `badge n'est pas utilisable`() {
        val badge: Item = Badge(2, "Badge", "d", rival)
        assertTrue(badge !is Utilisable)
    }
}
