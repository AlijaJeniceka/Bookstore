package com.bookstore.business.service;

public interface EcommerceSyncService {

    /**
     * Method to synchronize books every midnight by EET
     */
    void booksSynchronization();

}
