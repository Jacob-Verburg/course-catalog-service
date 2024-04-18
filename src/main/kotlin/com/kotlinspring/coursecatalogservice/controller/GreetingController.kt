package com.kotlinspring.coursecatalogservice.controller

import com.kotlinspring.coursecatalogservice.service.GreetingsService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/v1/greetings")
class GreetingController(val greetingsService: GreetingsService) {


    //http://localhost:8080/v1/greetings/jacob
    @GetMapping("/{name}")
    fun retrieveGreeting(@PathVariable name: String):String {
        logger.info { "Name is $name" }
        return greetingsService.retrieveGreeting(name)
    }
}