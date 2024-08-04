package com.poo.GestionAcademica.APILOGIN;

import com.poo.GestionAcademica.entities.Student;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class LoginABMAPI {

    public static int RegisterAndGetUserID(Student student) {

            String url = "http://poo-dev.unsada.edu.ar:4000/sistema_abm/registerStudent";

            String PASSWORD = "1234";

            // Generar un número aleatorio
            Random random = new Random();
            int randomNumber = random.nextInt(1000);

            // Crear el username
            String username = student.getFirstName() + student.getLastName() + randomNumber;

            // Crear el JSON a enviar
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("username", username);
            requestBody.put("password", PASSWORD);

            // Crear la entidad de la solicitud
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

            // Enviar la solicitud POST
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.POST, request, Integer.class);

            return response.getBody();

    }
}
