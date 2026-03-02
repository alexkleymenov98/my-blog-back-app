package org.blog.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // Указываем Spring, что этот компонент является контроллером
public class HomeController {

    @GetMapping("/home") // Принимаем GET-запрос по адресу /home
    @ResponseBody        // Указываем, что возвращаемое значение является ответом
    public String homePage() {
        return "<h1>Hello, Java! </h1>"; // Ответ
    }

    @GetMapping("/cska1")
    @ResponseBody
    public String homePage2() {
        return "<h1>Hello, Java 21! </h1>"; // Ответ
    }

}