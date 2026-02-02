package com.peluqueria.controllers;

import com.peluqueria.entity.Servicio;
import com.peluqueria.security.service.ServicioFavoritos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favoritos")
// @CrossOrigin(origins = "*") // Descomenta si tienes problemas de CORS con Flutter Web
public class FavoritoController {

    @Autowired
    private ServicioFavoritos servicioFavoritos;

    // 1. DAR / QUITAR LIKE
    // POST: /api/favoritos/toggle?idCliente=1&idServicio=5
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @RequestParam Long idCliente,
            @RequestParam Long idServicio) {

        return ResponseEntity.ok(servicioFavoritos.toggleFavorito(idCliente, idServicio));
    }

    // 2. VERIFICAR SI TIENE LIKE (Para pintar icono al cargar)
    // GET: /api/favoritos/check?idCliente=1&idServicio=5
    @GetMapping("/check")
    public ResponseEntity<Boolean> checkLike(
            @RequestParam Long idCliente,
            @RequestParam Long idServicio) {

        return ResponseEntity.ok(servicioFavoritos.esFavorito(idCliente, idServicio));
    }

    // 3. VER TODOS MIS FAVORITOS
    // GET: /api/favoritos/mis-favoritos/1
    @GetMapping("/mis-favoritos/{idCliente}")
    public ResponseEntity<List<Map<String, Object>>> misFavoritos(@PathVariable Long idCliente) {

        List<Servicio> lista = servicioFavoritos.obtenerFavoritosDelCliente(idCliente);

        // Mapeamos manualmente a un Map para enviar un JSON limpio y controlar qué datos enviamos
        List<Map<String, Object>> respuesta = new ArrayList<>();

        for (Servicio s : lista) {
            Map<String, Object> item = new HashMap<>();
            item.put("idServicio", s.getIdServicio());
            item.put("nombre", s.getNombre());
            item.put("precio", s.getPrecio());
            item.put("duracionBloques", s.getDuracionBloques());
            item.put("descripcion", s.getDescripcion());
            // item.put("imagenBase64", s.getImagenBase64()); // Cuidado con el tamaño de las imágenes

            respuesta.add(item);
        }

        return ResponseEntity.ok(respuesta);
    }
}