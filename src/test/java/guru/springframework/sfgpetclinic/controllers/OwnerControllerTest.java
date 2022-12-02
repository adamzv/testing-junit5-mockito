package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    public static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    public static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController ownerController;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @Nested
    class ProcessFindForm {
        @BeforeEach
        void setUp() {
            given(ownerService.findAllByLastNameLike(stringArgumentCaptor.capture()))
                    .willAnswer(invocation -> {
                        List<Owner> owners = new ArrayList<>();

                        String name = invocation.getArgument(0);

                        if (name.equals("%Zverka%")) {
                            owners.add(new Owner(1L, "Adam", "Zverka"));
                            return owners;
                        } else if (name.equals("%Null%")) {
                            return owners;
                        } else if (name.equals("%Multiple%")) {
                            owners.add(new Owner(1L, "Adam", "Zverka"));
                            owners.add(new Owner(2L, "Adam2", "Zverka2"));
                            return owners;
                        }

                        throw new RuntimeException("Invalid Argument");
                    });
        }

        @Test
        void processFindFormWildcardFound() {
            // given
            Owner owner = new Owner(1L, "Adam", "Multiple");

            // when
            String viewName = ownerController.processFindForm(owner, bindingResult, Mockito.mock(Model.class));

            // then
            assertThat("%Multiple%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
            assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);
        }

        @Test
        void processFindFormWildcardNotFound() {
            // given
            Owner owner = new Owner(1L, "Adam", "Null");

            // when
            String viewName = ownerController.processFindForm(owner, bindingResult, null);

            // then
            assertThat("%Null%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
            assertThat("owners/findOwners").isEqualToIgnoringCase(viewName);
        }

        @Test
        void processFindFormWildcardStringAnnotation() {
            // given
            Owner owner = new Owner(1L, "Adam", "Zverka");

            // when
            String viewName = ownerController.processFindForm(owner, bindingResult, null);

            // then
            assertThat("%Zverka%").isEqualToIgnoringCase(stringArgumentCaptor.getValue());
            assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
        }
    }


    @Test
    void processCreationFormHasErrors() {
        // given
        Owner owner = new Owner(1L, "Adam", "Zverka");
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        String result = ownerController.processCreationForm(owner, bindingResult);

        // then
        assertThat(result).isEqualTo(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
    }

    @Test
    void processCreationFormNoErrors() {
        Owner owner = new Owner(5L, "Adam", "Zverka");
        given(bindingResult.hasErrors()).willReturn(false);
        given(ownerService.save(any(Owner.class))).willReturn(owner);

        String result = ownerController.processCreationForm(owner, bindingResult);

        then(ownerService).should(times(1)).save(any(Owner.class));
        assertThat(bindingResult.hasErrors()).isFalse();
        assertThat(result).isEqualTo(REDIRECT_OWNERS_5);
    }
}