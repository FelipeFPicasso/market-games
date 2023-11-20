package com.marketgame.service

import com.marketgame.enums.CustomerStatus
import com.marketgame.enums.Errors
import com.marketgame.enums.Role
import com.marketgame.exception.NotFoundException
import com.marketgame.helper.buildCustomer
import com.marketgame.model.CustomerModel
import com.marketgame.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.hibernate.id.factory.internal.UUIDGenerationTypeStrategy
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import kotlin.random.Random

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var gameService: GameService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customers`() {
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findAll() } returns fakeCustomers

        val customers = customerService.getAll(null)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findAll() }
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }


    }

    @Test
    fun `should return customers when name is informed`() {
        val name = UUID.randomUUID().toString()
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findByNameContaining(name) } returns fakeCustomers

        val customers = customerService.getAll(name)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 0) { customerRepository.findAll() }
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }
    }

    @Test
    fun `should create customer and encrypt password`() {
        val initialPassword = Random.nextInt().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypter = fakeCustomer.copy(password = fakePassword)

        every { customerRepository.save(any()) } returns fakeCustomer
        every { bCrypt.encode(initialPassword) } returns fakePassword

        customerService.create(fakeCustomer)

        verify(exactly = 1) { customerRepository.save(fakeCustomerEncrypter) }
        verify(exactly = 1) { bCrypt.encode(initialPassword) }
    }

    @Test
    fun `should return customer by id`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findById(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    /*
    @Test
    fun `should throw error not found find by id`(){
        val id = Random.nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException>{customerService.findById(id)}

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("MG-201", error.errorCode)
        verify(exactly = 1) {customerRepository.findById(id)}
    }
*/
    @Test
    fun `should update customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns true
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer


        customerService.update(fakeCustomer)

        verify(exactly = 1) { customerRepository.existsById(id) }
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }

    }

    /*@Test
    fun `should not found exception when update customer`(){
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { customerRepository.existsById(id) } returns false
        every { customerRepository.save(fakeCustomer) } returns fakeCustomer



        val error = assertThrows<NotFoundException>{
            customerService.update(fakeCustomer)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("MG-201", error.errorCode)

        verify(exactly = 1) {customerRepository.existsById(id)}
        verify(exactly = 0) {customerRepository.save(any())}

    }
*/
    @Test
    fun `should delete customer`() {
        val id = Random.nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerService.findById(id) } returns fakeCustomer
        every { customerRepository.save(expectedCustomer) } returns expectedCustomer
        every { gameService.deleteByCustomer(fakeCustomer) } just runs

        customerService.delete(id)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { gameService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }

    }

    /*@Test
    fun `should throw not found exception when delete customer`(){
        val id = Random.nextInt()

        every { customerService.findById(id) } throws NotFoundException(Errors.MG101.message.format(id), Errors.MG101.code)

        val error = assertThrows<NotFoundException>{
            customerService.delete(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("MG-201", error.errorCode)

        verify  (exactly = 1) {customerService.findById(id)}
        verify  (exactly = 0) {gameService.deleteByCustomer(any())}
        verify (exactly = 0) {customerRepository.save(any())}

    }
*/
    @Test
    fun `should return true when email available`() {
        val email = "${Random.nextInt()}@email.com"

        every { customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }


    }

    @Test
    fun `should return false when email unavailable`() {
        val email = "${Random.nextInt()}@email.com"

        every { customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)

        verify(exactly = 1) { customerRepository.existsByEmail(email) }


    }
}




    /*@Test
    fun `fake test`(){
        val resultado = soma(2,3)
        assertEquals(6,resultado)
    }
    fun soma(a: Int, b: Int): Int {
        return a + b
    }*/

