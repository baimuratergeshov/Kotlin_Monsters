package org.example.monstre

import org.example.especeFlamkip
import org.example.especeGalum
import org.example.especeSpringleaf
import java.io.ByteArrayInputStream
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class IndividuMonstreTest {

    private fun individu(exp: Double = 0.0, espece: EspeceMonstre = especeSpringleaf) =
        IndividuMonstre(1, "Test", espece, null, exp)

    private fun saisie(texte: String) = System.setIn(ByteArrayInputStream(texte.toByteArray()))

    @Test
    fun `potentiel dans la fourchette et pv egal a pvMax a la creation`() {
        repeat(50) {
            val m = individu()
            assertTrue(m.potentiel in 0.5..2.0)
            assertEquals(m.pvMax, m.pv)
        }
    }

    @Test
    fun `palierExp suit 100 fois (niveau-1) au carre`() {
        val m = individu()
        assertEquals(0.0, m.palierExp(1))
        assertEquals(100.0, m.palierExp(2))
        assertEquals(400.0, m.palierExp(3))
        assertEquals(1600.0, m.palierExp(5))
    }

    @Test
    fun `1500 exp donne le niveau 5`() {
        assertEquals(5, individu(1500.0).niveau)
    }

    @Test
    fun `gagner de l'exp fait monter de niveau`() {
        val m = individu(1500.0)
        m.exp = 1600.0
        assertEquals(6, m.niveau)
        assertEquals(1600.0, m.exp)
    }

    @Test
    fun `gagner peu d'exp ne change pas le niveau`() {
        val m = individu(1500.0)
        m.exp = 1550.0
        assertEquals(5, m.niveau)
    }

    @Test
    fun `levelUp augmente niveau et pv max, pv suit pvMax`() {
        val m = individu()
        val niveau = m.niveau
        val pvMaxAvant = m.pvMax
        val pvAvant = m.pv
        m.levelUp()
        assertEquals(niveau + 1, m.niveau)
        assertEquals(m.pvMax - pvMaxAvant, m.pv - pvAvant)
    }

    @Test
    fun `pv reste entre 0 et pvMax`() {
        val m = individu()
        m.pv = -50
        assertEquals(0, m.pv)
        m.pv = m.pvMax + 100
        assertEquals(m.pvMax, m.pv)
    }

    @Test
    fun `attaquer inflige au moins 1 degat`() {
        val faible = individu()
        faible.attaque = 1
        val tank = individu(espece = especeGalum)
        tank.defense = 200
        val pvAvant = tank.pv
        faible.attaquer(tank)
        assertEquals(pvAvant - 1, tank.pv)
    }

    @Test
    fun `attaquer applique attaque moins defense sur 2`() {
        val a = individu()
        a.attaque = 50
        val b = individu(espece = especeFlamkip)
        b.defense = 20
        val pvAvant = b.pv
        a.attaquer(b)
        assertEquals(pvAvant - 40, b.pv)
    }

    @Test
    fun `attaquer ne fait pas descendre les pv sous 0`() {
        val a = individu()
        a.attaque = 10_000
        val b = individu()
        a.attaquer(b)
        assertEquals(0, b.pv)
    }

    @Test
    fun `renommer change le nom`() {
        val m = individu()
        saisie("Bono\n")
        m.renommer()
        assertEquals("Bono", m.nom)
    }

    @Test
    fun `renommer avec saisie vide garde le nom`() {
        val m = individu()
        saisie("\n")
        m.renommer()
        assertEquals("Test", m.nom)
    }

    @Test
    fun `un monstre sans entraineur a un entraineur null`() {
        assertNull(individu().entraineur)
    }
}
