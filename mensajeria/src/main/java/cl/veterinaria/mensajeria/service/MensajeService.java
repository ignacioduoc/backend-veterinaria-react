package cl.veterinaria.mensajeria.service;

import cl.veterinaria.mensajeria.dtos.MensajeRequestDTO;
import cl.veterinaria.mensajeria.dtos.MensajeResponseDTO;
import cl.veterinaria.mensajeria.exception.MensajeNoEncontradoException;
import cl.veterinaria.mensajeria.model.Mensaje;
import cl.veterinaria.mensajeria.repository.MensajeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MensajeService {

    private final MensajeRepository repository;

    @Transactional
    public MensajeResponseDTO crear(MensajeRequestDTO dto) {
        Mensaje mensaje = new Mensaje();
        mensaje.setNombre(dto.nombre().trim());
        mensaje.setCorreo(dto.correo().trim());
        mensaje.setTexto(dto.texto().trim());

        return aDTO(repository.save(mensaje));
    }

    @Transactional(readOnly = true)
    public List<MensajeResponseDTO> listar() {
        return repository.findAllByOrderByFechaDesc().stream()
                .map(this::aDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MensajeResponseDTO obtener(Long id) {
        return aDTO(buscar(id));
    }

    @Transactional
    public void eliminar(Long id) {
        repository.delete(buscar(id));
    }

    private Mensaje buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new MensajeNoEncontradoException(id));
    }

    private MensajeResponseDTO aDTO(Mensaje m) {
        return new MensajeResponseDTO(
                m.getId(), m.getNombre(), m.getCorreo(), m.getTexto(), m.getFecha());
    }
}
