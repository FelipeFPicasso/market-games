package com.marketgame.events.listener

import com.marketgame.events.PurchaseEvent
import com.marketgame.helper.buildPurchase
import com.marketgame.service.PurchaseService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID


@ExtendWith(MockKExtension::class)

class GenerateNfeListenerTest{

    @MockK
    private lateinit var purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun `sould generate nfe` () {
        val fakeNfe = UUID.randomUUID()
        val purchase = buildPurchase(nfe = null)
        val purchaseExpected = purchase.copy(nfe = fakeNfe.toString())
        mockkStatic(UUID::class)

        every { UUID.randomUUID() } returns fakeNfe
        every { purchaseService.update(purchaseExpected) } just runs

        generateNfeListener.listen(PurchaseEvent(this, purchase))

        verify (exactly = 1) {purchaseService.update(purchaseExpected)}

    }

}