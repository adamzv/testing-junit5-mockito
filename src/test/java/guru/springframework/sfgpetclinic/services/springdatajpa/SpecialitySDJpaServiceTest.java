package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void delete() {
        Speciality speciality = new Speciality();

        service.delete(speciality);

        verify(specialtyRepository).delete(any(Speciality.class));
    }

    @Test
    void findById() {
        Speciality speciality = new Speciality();

        when(specialtyRepository.findById(1l)).thenReturn(Optional.of(speciality));

        Speciality foundSpeciality = service.findById(1l);

        assertThat(foundSpeciality).isNotNull();

        verify(specialtyRepository).findById(anyLong());
    }

    // Given When Then
    @Test
    void findByIdBddTest() {
        // given
        Speciality speciality = new Speciality();
        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));

        // when
        Speciality foundSpeciality = service.findById(1L);

        // then
        assertThat(foundSpeciality).isNotNull();
        // BDD way
        // then(specialtyRepository).should().findById(anyLong());
        then(specialtyRepository).should(times(1)).findById(anyLong());

        then(specialtyRepository).shouldHaveNoMoreInteractions();
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        service.deleteById(1L);

        verify(specialtyRepository, times(2)).deleteById(1l);
        // times() atLeastOnce() atMost

        verify(specialtyRepository, never()).deleteById(5l);
    }

    @Test
    void testDelete() {
        service.delete(new Speciality());
    }
}