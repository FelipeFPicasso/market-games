package com.marketgame.service

import com.marketgame.events.PurchaseEvent
import com.marketgame.helper.buildCustomer
import com.marketgame.helper.buildPurchase
import com.marketgame.model.CustomerModel
import com.marketgame.model.GameModel
import com.marketgame.model.PurchaseModel
import com.marketgame.repository.PurchaseRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import java.math.BigDecimal
import java.util.UUID
import javax.xml.bind.PrintConversionEvent

@ExtendWith(MockKExtension::class)
class PurchseServiceTest {

    @MockK
    private lateinit var purchaseRepository: PurchaseRepository

    @MockK
    private lateinit var applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun `should create purchase and publish event`(){
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(purchase)

        verify (exactly = 1) {purchaseRepository.save(purchase)}
        verify  (exactly = 1) {applicationEventPublisher.publishEvent(capture(purchaseEventSlot))}

        assertEquals(purchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun `should update purchase`(){
        val purchase = buildPurchase()

        every { purchaseRepository.save(purchase) } returns purchase

        purchaseService.update(purchase)

        verify (exactly = 1) {purchaseRepository.save(purchase)}

    }



}