package com.example.digtal_arcade.ms_catalogo.item.service;

import com.example.digtal_arcade.ms_catalogo.item.dto.ItemDTO;
import com.example.digtal_arcade.ms_catalogo.item.model.Item;
import com.example.digtal_arcade.ms_catalogo.item.repository.ItemRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemDTO> obtenerTodos() {
        return itemRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public Optional<ItemDTO> buscarPorId(Integer id) {
        return itemRepository.findById(id).map(this::convertirADTO);
    }

    public ItemDTO guardar(ItemDTO dto) {
        if (itemRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("El item ya existe en el catalogo.");
        }
        Item item = convertirAEntidad(dto);
        item.setId(null);
        try {
            return convertirADTO(itemRepository.save(item));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("El item ya existe en el catalogo.");
        }
    }

    public Optional<ItemDTO> actualizarParcial(Integer id, ItemDTO dto) {
        return itemRepository.findById(id).map(item -> {
            if (dto.getNombre() != null) {
                boolean nombreCambia = !dto.getNombre().equals(item.getNombre());
                if (nombreCambia && itemRepository.existsByNombre(dto.getNombre())) {
                    throw new IllegalArgumentException("El item ya existe en el catalogo.");
                }
                item.setNombre(dto.getNombre());
            }
            if (dto.getIdJuego() != null) {
                item.setIdJuego(dto.getIdJuego());
            }
            return convertirADTO(itemRepository.save(item));
        });
    }

    public boolean eliminarPorId(Integer id) {
        if (!itemRepository.existsById(id)) {
            return false;
        }
        itemRepository.deleteById(id);
        return true;
    }

    public List<ItemDTO> buscarPorJuego(Integer idJuego) {
        return itemRepository.findByIdJuego(idJuego).stream().map(this::convertirADTO).toList();
    }

    public Optional<ItemDTO> buscarPorNombre(String nombre) {
        return itemRepository.findByNombre(nombre).map(this::convertirADTO);
    }

    public Integer contarPorJuego(Integer idJuego) {
        return itemRepository.countByIdJuego(idJuego);
    }

    private ItemDTO convertirADTO(Item item) {
        return new ItemDTO(item.getId(), item.getNombre(), item.getIdJuego());
    }

    private Item convertirAEntidad(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setNombre(dto.getNombre());
        item.setIdJuego(dto.getIdJuego());
        return item;
    }
}
