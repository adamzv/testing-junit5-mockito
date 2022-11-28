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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
        // given
        Visit visit1 = new Visit();
        given(visitRepository.findAll()).willReturn(Set.of(visit, visit1));

        // when
        Set<Visit> foundVisits = service.findAll();

        // then
        assertThat(foundVisits).isNotNull();
        assertThat(foundVisits).hasSize(2);
        then(visitRepository).should().findAll();
    }

    @Test
    void findById() {
        // given
        given(visitRepository.findById(1L)).willReturn(Optional.of(visit));
        given(visitRepository.findById(2L)).willReturn(Optional.empty());

        // when
        assertThat(service.findById(1L)).isNotNull();
        assertThat(service.findById(2L)).isNull();

        // then
        then(visitRepository).should(times(2)).findById(anyLong());
    }

    @Test
    void save() {
        given(visitRepository.save(any(Visit.class))).willReturn(visit);

        Visit savedVisit = service.save(visit);

        then(visitRepository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        service.delete(visit);
        then(visitRepository).should().delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        then(visitRepository).should(times(1)).deleteById(1L);
    }
}