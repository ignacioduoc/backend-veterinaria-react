package cl.veterinaria.pagos.service;

import cl.veterinaria.pagos.dtos.PagoRequestDTO;
import cl.veterinaria.pagos.dtos.PagoResponseDTO;
import cl.veterinaria.pagos.exception.PagoConflictoException;
import cl.veterinaria.pagos.exception.PagoInvalidoException;
import cl.veterinaria.pagos.exception.PagoNoEncontradoException;
import cl.veterinaria.pagos.model.EstadoPago;
import cl.veterinaria.pagos.model.MetodoPago;
import cl.veterinaria.pagos.model.Pago;
import cl.veterinaria.pagos.repository.PagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PagoRepository repository;

    @Transactional
    public PagoResponseDTO registrar(PagoRequestDTO dto) {
        if (repository.existsByCitaId(dto.citaId())) {
            throw new PagoConflictoException("La cita " + dto.citaId() + " ya tiene un pago registrado");
        }

        MetodoPago metodo = MetodoPago.valueOf(dto.metodo().toUpperCase(Locale.ROOT));
        boolean tarjeta = metodo == MetodoPago.TARJETA;

        if (tarjeta && dto.ultimos4() == null) {
            throw new PagoInvalidoException("Con tarjeta debe indicar los últimos 4 dígitos");
        }
        if (!tarjeta && dto.ultimos4() != null) {
            throw new PagoInvalidoException("Una transferencia no lleva dígitos de tarjeta");
        }

        Pago pago = new Pago();
        pago.setCitaId(dto.citaId());
        pago.setMetodo(metodo);
        pago.setMonto(dto.monto());
        pago.setUltimos4(dto.ultimos4());
        pago.setCodigo(generarCodigo());

        // El servidor decide el estado: la tarjeta se paga al tiro;
        // la transferencia queda pendiente hasta que el admin la confirme.
        pago.setEstado(tarjeta ? EstadoPago.PAGADO : EstadoPago.PENDIENTE);

        try {
            return aDTO(repository.saveAndFlush(pago));
        } catch (DataIntegrityViolationException ex) {
            // Dos pagos de la misma cita llegando a la vez: gana el primero.
            throw new PagoConflictoException("La cita " + dto.citaId() + " ya tiene un pago registrado");
        }
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> listar() {
        return repository.findAllByOrderByFechaDesc().stream()
                .map(this::aDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPorCita(Long citaId) {
        return repository.findByCitaId(citaId)
                .map(this::aDTO)
                .orElseThrow(() -> new PagoNoEncontradoException("La cita " + citaId + " no tiene pago"));
    }

    // El admin confirma que recibió la transferencia: pendiente → pagado.
    @Transactional
    public PagoResponseDTO confirmar(Long id) {
        Pago pago = repository.findById(id)
                .orElseThrow(() -> new PagoNoEncontradoException("No se encontró el pago con id " + id));

        if (pago.getMetodo() != MetodoPago.TRANSFERENCIA) {
            throw new PagoConflictoException("Solo se pueden confirmar transferencias");
        }
        if (pago.getEstado() == EstadoPago.PAGADO) {
            throw new PagoConflictoException("Este pago ya estaba confirmado");
        }

        pago.setEstado(EstadoPago.PAGADO);
        return aDTO(pago);
    }

    // "VSM-" + 6 dígitos (VSM = Veterinaria San Marcos), igual que en el front.
    private String generarCodigo() {
        String codigo;
        do {
            codigo = "VSM-" + ThreadLocalRandom.current().nextInt(100000, 1000000);
        } while (repository.existsByCodigo(codigo));
        return codigo;
    }

    private PagoResponseDTO aDTO(Pago p) {
        return new PagoResponseDTO(
                p.getId(),
                p.getCodigo(),
                p.getCitaId(),
                p.getMetodo().name().toLowerCase(Locale.ROOT),
                p.getMonto(),
                p.getEstado().name().toLowerCase(Locale.ROOT),
                p.getUltimos4(),
                p.getFecha());
    }
}
