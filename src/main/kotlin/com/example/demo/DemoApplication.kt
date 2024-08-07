package com.example.demo

import jakarta.persistence.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@RestController
class MessageController(val service: MessageService) {
    @GetMapping("/")
    fun index(): List<Message> = service.findMessages()

    @GetMapping("/{id}")
    fun index(@PathVariable id: String): ResponseEntity<List<Message>> {
        val messages = service.findMessageById(id)
        return if (messages.isNotEmpty()) ResponseEntity.ok(messages) else ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @PostMapping("/")
    fun post(@RequestBody message: Message) {
        service.save(message)
    }
}

@Entity
@Table(name = "MESSAGES")
data class Message(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: String?, val text: String)

@Service
class MessageService(val db: MessageRepository) {
    fun findMessages(): List<Message> = db.findAll().toList()

    fun findMessageById(id: String): List<Message> = db.findById(id).toList()

    fun save(message: Message) {
        db.save(message)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> =
        if (isPresent) listOf(get()) else emptyList()
}

interface MessageRepository : CrudRepository<Message, String>
