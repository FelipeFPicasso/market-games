package com.marketgame


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("admin")
class adminController (){


@GetMapping("/reports")
fun report (): String {
    return "This is a report. Only Admin can see it!"
}



}