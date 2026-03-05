package org.blog.init;


import jakarta.servlet.MultipartConfigElement;
import org.blog.WebConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

import java.io.File;

public class WebAppInitializer implements WebApplicationInitializer {

    private static final int MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final int MAX_REQUEST_SIZE = 20 * 1024 * 1024; // 20MB
    private static final int FILE_SIZE_THRESHOLD = 1024 * 1024; // 1MB

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("=== WebAppInitializer STARTED ==="); // Отладочный вывод

        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebConfiguration.class);

        DispatcherServlet servlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", servlet);

        registration.setLoadOnStartup(1);
        registration.addMapping("/"); // Все запросы идут через DispatcherServlet

        // ВАЖНО: Сначала выводим параметры, потом устанавливаем конфигурацию
        String tempDir = System.getProperty("java.io.tmpdir");
        System.out.println("=== Multipart Configuration ===");
        System.out.println("Temp dir: " + tempDir);
        System.out.println("Temp dir exists: " + new File(tempDir).exists());
        System.out.println("Temp dir writable: " + new File(tempDir).canWrite());
        System.out.println("Max file size: " + MAX_FILE_SIZE + " bytes (" + (MAX_FILE_SIZE / 1024 / 1024) + "MB)");
        System.out.println("Max request size: " + MAX_REQUEST_SIZE + " bytes (" + (MAX_REQUEST_SIZE / 1024 / 1024) + "MB)");
        System.out.println("File size threshold: " + FILE_SIZE_THRESHOLD + " bytes");

        // Создаем и устанавливаем MultipartConfig
        MultipartConfigElement multipartConfig = new MultipartConfigElement(
                tempDir,
                MAX_FILE_SIZE,
                MAX_REQUEST_SIZE,
                FILE_SIZE_THRESHOLD
        );

        registration.setMultipartConfig(multipartConfig);

        // Проверка что конфигурация установлена
//        System.out.println("MultipartConfig set: " + (registration.getMultipartConfig() != null));
        System.out.println("=== WebAppInitializer FINISHED ===");

    }
}