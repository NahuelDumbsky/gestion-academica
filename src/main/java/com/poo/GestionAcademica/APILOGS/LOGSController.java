package com.poo.GestionAcademica.APILOGS;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.poo.GestionAcademica.entities.Student;

public class LOGSController {

    static String baseLoginString = "https://poo2024.unsada.edu.ar/sistema_autogestion/logs/";

    public void findLogs(List<Student> students) {
        try {
            for (Student student : students) {
                String baseLoginStringAux = baseLoginString;
                int id = student.getStudentId();
                baseLoginStringAux += "student" + id;

                URL baseLogin = new URL(baseLoginStringAux);

                HttpURLConnection conexionLogin = (HttpURLConnection) baseLogin.openConnection();
                conexionLogin.setRequestMethod("GET");
                conexionLogin.connect();

                int responseCode = conexionLogin.getResponseCode();

                if (responseCode != 200) {
                    if (responseCode == 400) {
                        System.out.println("No hay logs para student" + id);
                    } else {
                        System.out.println("No es posible conectar para student " + id + ": " + responseCode);
                    }
                    continue;
                } else {
                    StringBuilder informacionJson = new StringBuilder();
                    Scanner in = new Scanner(baseLogin.openStream());
                    while (in.hasNext()) {
                        informacionJson.append(in.nextLine());
                    }
                    in.close();

                    // Parsear el JSON recibido
                    JSONArray jsonArray = new JSONArray(informacionJson.toString());

                    // Mapa para almacenar el evento más reciente por courseId
                    Map<String, JSONObject> latestEventsMap = new HashMap<>();

                    // Iterar sobre cada objeto JSON en el array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (jsonObject.has("timestamp") && jsonObject.has("event") && jsonObject.has("courseId")) {
                            String courseId = jsonObject.getString("courseId");
                            String timestamp = jsonObject.getString("timestamp");

                            // Si el courseId ya está en el mapa, comparamos timestamps
                            if (latestEventsMap.containsKey(courseId)) {
                                JSONObject currentEvent = latestEventsMap.get(courseId);
                                String currentTimestamp = currentEvent.getString("timestamp");

                                // Si el timestamp actual es más reciente, actualizamos el evento en el mapa
                                if (timestamp.compareTo(currentTimestamp) > 0) {
                                    latestEventsMap.put(courseId, jsonObject);
                                }
                            } else {
                                // Si no está en el mapa, agregamos el evento
                                latestEventsMap.put(courseId, jsonObject);
                            }
                        }
                    }

                    for (String courseId : latestEventsMap.keySet()) {
                        JSONObject event = latestEventsMap.get(courseId);
                        String eventDetail = event.getString("event");

                        System.out.println(eventDetail);
                        // Determinar la acción a tomar en base al evento más reciente
                        if ("ALTA".equalsIgnoreCase(eventDetail)) {
                            enrollStudent(student, courseId);
                        } else if ("BAJA".equalsIgnoreCase(eventDetail)) {
                            unenrollStudent(student, courseId);
                        }
                    }

                }
                baseLoginStringAux = baseLoginString;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void enrollStudent(Student student, String courseId) {
        try {
            String baseEnroll = "http://localhost:8080/courses/" + courseId + "/enroll/student"
                    + student.getStudentId();

            System.out.println(baseEnroll);

            URL baseEnrollUrl = new URL(baseEnroll);

            HttpURLConnection connectionEnroll = (HttpURLConnection) baseEnrollUrl.openConnection();
            connectionEnroll.setRequestMethod("POST");
            connectionEnroll.setDoOutput(true);

            connectionEnroll.connect();

            int responseCode = connectionEnroll.getResponseCode();

            System.out.println(responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unenrollStudent(Student student, String courseId) {
        try {
            String baseUnEnroll = "http://localhost:8080/courses/" + courseId + "/enroll/student"
                    + student.getStudentId();

            System.out.println(baseUnEnroll);

            URL baseUnEnrollUrl = new URL(baseUnEnroll);

            HttpURLConnection connectionUnEnroll = (HttpURLConnection) baseUnEnrollUrl.openConnection();
            connectionUnEnroll.setRequestMethod("DELETE");
            connectionUnEnroll.setDoOutput(true);

            connectionUnEnroll.connect();

            int responseCode = connectionUnEnroll.getResponseCode();

            System.out.println(responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
