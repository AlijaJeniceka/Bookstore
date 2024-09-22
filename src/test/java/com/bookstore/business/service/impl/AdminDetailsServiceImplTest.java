package com.bookstore.business.service.impl;

import com.bookstore.business.repository.AdminRepository;
import com.bookstore.business.repository.model.Admin;
import com.bookstore.security.AdminDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminDetailsServiceImplTest {

    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminDetailsServiceImpl adminDetailsService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setUsername("admin");
        admin.setPassword("password123");
    }

    @Test
    void test_loadUserByUsername_success() {
        //given
        when(adminRepository.findByUsername("admin")).thenReturn(admin);
        //when
        AdminDetails adminDetails = (AdminDetails) adminDetailsService.loadUserByUsername("admin");
        //then
        assertNotNull(adminDetails);
        assertEquals("admin", adminDetails.getUsername());
        assertEquals("password123", adminDetails.getPassword());
        verify(adminRepository).findByUsername("admin");
    }

    @Test
    void test_loadUserByUsername_throwException_adminDoesNotExist() {
        //given-when
        when(adminRepository.findByUsername("admin")).thenReturn(null);
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            adminDetailsService.loadUserByUsername("admin");
        });
        //then
        assertEquals("Admin not found", exception.getMessage());
        verify(adminRepository).findByUsername("admin");
    }
}