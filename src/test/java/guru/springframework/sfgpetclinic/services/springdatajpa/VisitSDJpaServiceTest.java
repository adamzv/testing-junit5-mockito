package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    static Visit visit;

    @BeforeEach
    void beforeEach() {
        visit = new Visit();
    }

    @Test
    void findAll() {
        Visit visit1 = new Visit();

        when(visitRepository.findAll()).thenReturn(Set.of(visit, visit1));

        Set<Visit> foundVisits = service.findAll();
        assertThat(foundVisits).isNotNull();
        assertThat(foundVisits).hasSize(2);

        verify(visitRepository).findAll();
    }

    @Test
    void findById() {
        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));
        when(visitRepository.findById(2L)).thenReturn(Optional.ofNullable(null));

        assertThat(service.findById(1L)).isNotNull();
        assertThat(service.findById(2L)).isNull();
        verify(visitRepository, times(2)).findById(anyLong());
    }

    @Test
    void save() {
        when(visitRepository.save(any(Visit.class))).thenReturn(visit);

        Visit savedVisit = service.save(visit);

        verify(visitRepository).save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        service.delete(visit);
        verify(visitRepository).delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        verify(visitRepository, times(1)).deleteById(1L);
    }
}