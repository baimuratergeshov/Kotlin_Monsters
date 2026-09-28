package org.example.item

import org.example.dresseur.Entraineur
import org.example.especeGalum
import org.example.joueur
import org.example.monstre.IndividuMonstre
import org.example.rival
import java.io.ByteArrayInputStream
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertSame
import kotlin.test.assertTrue

class MonsterKubeTest {

    private fun saisie(texte: String) = System.setIn(ByteArrayInputStream(texte.toByteArray()))

    private fun sauvage() = IndividuMonstre(9, "Galum", especeGalum, null, 0.0)

    private fun kube(chance: Double) = MonsterKube(1, "Kube", "d", chance)

    @BeforeTest
    fun reset() {
        joueur.equipeMonstre.clear()
        joueur.boiteMonstre.clear()
    }

    @Test
    fun `kube est un item utilisable`() {
        val k = kube(50.0)
        assertTrue(k is Item)
        assertTrue(k is Utilisable)
    }

    @Test
    fun `monstre deja possede ne peut pas etre capture`() {
        val m = sauvage()
        m.entraineur = rival
        saisie("\n")
        assertFalse(kube(100.0).utiliser(m))
        assertSame(rival, m.entraineur)
        assertTrue(joueur.equipeMonstre.isEmpty())
    }

    @Test
    fun `capture garantie ajoute a l'equipe`() {
        val m = sauvage()
        m.pv = 1 // ratio proche de 0 : chance effective elevee
        saisie("\n")
        assertTrue(kube(1000.0).utiliser(m))
        assertEquals(listOf(m), joueur.equipeMonstre)
        assertSame(joueur, m.entraineur)
        assertEquals("Galum", m.nom)
    }

    @Test
    fun `capture avec nouveau nom`() {
        val m = sauvage()
        saisie("Rocky\n")
        assertTrue(kube(1000.0).utiliser(m))
        assertEquals("Rocky", m.nom)
    }

    @Test
    fun `equipe pleine envoie le monstre dans la boite`() {
        repeat(6) { joueur.equipeMonstre.add(IndividuMonstre(it, "M$it", especeGalum, joueur, 0.0)) }
        val m = sauvage()
        saisie("\n")
        assertTrue(kube(1000.0).utiliser(m))
        assertEquals(6, joueur.equipeMonstre.size)
        assertEquals(listOf(m), joueur.boiteMonstre)
        assertSame(joueur, m.entraineur)
    }

    @Test
    fun `capture ratee laisse le monstre libre`() {
        // chanceCapture a 0 : la chance effective est au minimum de 5 %,
        // on tente plusieurs fois jusqu'a obtenir au moins un echec.
        var echec = false
        repeat(200) {
            val m = sauvage()
            saisie("\n")
            if (!kube(0.0).utiliser(m)) {
                echec = true
                assertEquals(null, m.entraineur)
            }
        }
        assertTrue(echec)
    }

    @Test
    fun `sac de l'entraineur accepte des items`() {
        val e = Entraineur(5, "Test", 0)
        e.sacAItems.add(kube(10.0))
        e.sacAItems.add(Badge(3, "B", "d", rival))
        assertEquals(2, e.sacAItems.size)
    }
}
