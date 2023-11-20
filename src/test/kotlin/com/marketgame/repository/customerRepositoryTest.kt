package com.marketgame.repository

import com.marketgame.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
//@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class customerRepositoryTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @Test
    fun `should return name containing`(){
        val marcos = customerRepository.save(buildCustomer(name = "Marcos"))
        val matheus = customerRepository.save(buildCustomer(name = "Matheus"))
        customerRepository.save(buildCustomer(name = "Artur"))

        val customers = customerRepository.findByNameContaining("Ma")

        assertEquals(listOf(marcos, matheus), customers)

    }

    @Nested
    inner class `exist by email` {
        @Test
        fun `should return true when email exists`(){
            val email = "email@email.com"
            customerRepository.save(buildCustomer(email = email))

            val exists = customerRepository.existsByEmail(email)

            assertTrue(exists)
        }

        @Test
        fun `should return false when email do not exists`(){
            val email = "noexistingemail@teste.com"

            val exists = customerRepository.existsByEmail(email)

            assertFalse(exists)
        }

    }

    @Nested
    inner class `exist find by email` {
        @Test
        fun `should return customer when email exists`(){
            val email = "email@email.com"
            val customer = customerRepository.save(buildCustomer(email = email))

            val result = customerRepository.findByEmail(email)

            assertNotNull(result)
            assertEquals(customer, result)
        }

        @Test
        fun `should return null when email do not exists`(){
            val email = "noexistingemail@teste.com"

            val result = customerRepository.findByEmail(email)

            assertNull(result)
        }

    }
}