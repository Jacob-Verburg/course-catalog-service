package com.kotlinspring.controller

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.service.CourseService
import com.kotlinspring.service.GreetingsService
import com.kotlinspring.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient


@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {


    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMock : CourseService


    @Test
    fun addCourse(){

        val courseDTO = CourseDTO(null, "Build Restful APIs using SpringBoot and Kotlin", "jacob")

        every { courseServiceMock.addCourse(any()) } returns courseDTO(id = 1)

        val savedCourseDTO =webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue {
            savedCourseDTO!!.id != null
        }
    }



    @Test
    fun retrieveAllCourses(){

        every { courseServiceMock.retrieveAllCourses() }.returnsMany(
            listOf(courseDTO(id = 1),
                courseDTO(id = 2, name = "KotlinCourse"),)
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        println("course DTOS : $courseDTOs")
        Assertions.assertEquals(2,courseDTOs!!.size)
    }


    @Test
    fun updateCourse(){

        //existing course
        val course = Course(null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development")

        every { courseServiceMock.updateCourse(any(), any()) } returns courseDTO(id=100, name="Updated Build RestFul APis using SpringBoot and Kotlin")
        //courseId
        //Updated CourseDTO
        val updatedCourseDTO = CourseDTO(null,
            "Updated Build RestFul APis using SpringBoot and Kotlin", "Development")


        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(updatedCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Updated Build RestFul APis using SpringBoot and Kotlin", updatedCourse!!.name)

    }


    @Test
    fun deleteCourse(){

        every { courseServiceMock.deleteCourse(any()) } just runs


        val updatedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 100)
            .exchange()
            .expectStatus().isNoContent

    }

}