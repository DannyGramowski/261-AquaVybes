package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.AquaController;
import com.estore.api.estoreapi.model.aqua.Aqua;
import com.estore.api.estoreapi.model.aqua.PackSize;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.aqua.AquaFileDAO;
import com.estore.api.estoreapi.persistence.flavor.FlavorDAO;
import com.estore.api.estoreapi.persistence.flavor.FlavorFileDAO;
import com.estore.api.estoreapi.services.FlavorService;

/**
 * Testing AquaController!
 * @author gsh3009
 */
@Tag("Controller-tier")
public class AquaControllerTests {
    private AquaController aquaController;
    private AquaFileDAO mockDao;
    private FlavorService flavorService;
    private ArrayList<Flavor> flavors = new ArrayList<Flavor>();

    /**
     * set up AquaController and mockDAO
     */
    @BeforeEach
    public void setUpAquaController() {
        mockDao = mock(AquaFileDAO.class);
        FlavorDAO mockFlavorDao = mock(FlavorFileDAO.class);

        aquaController = new AquaController(mockDao, flavorService);
        this.flavors.add(new Flavor(0, "Berry", "A berry good taste!", 0));
    }

    @Test
    public void testGetInventory() throws Exception {
        Aqua[] aquas = new Aqua[2];
        aquas[0] = new Aqua(0, "test1", flavors, PackSize.SIX, 0);
        aquas[1] = new Aqua(1, "test2", flavors, PackSize.EIGHT, 0);

        when(mockDao.getInventory()).thenReturn(aquas);

        ResponseEntity<Aqua[]> response = aquaController.getInventory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aquas, response.getBody());
    }

    @Test
    public void testGetInventoryException() throws IOException {
        doThrow(new IOException()).when(mockDao).getInventory();

        ResponseEntity<Aqua[]> response = aquaController.getInventory();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateAqua() throws Exception {
        Aqua aqua = new Aqua(0, "test1", this.flavors, PackSize.SIX, 0);

        when(mockDao.createAqua("test1", this.flavors, PackSize.SIX, 0)).thenReturn(aqua);

        ResponseEntity<Aqua> response = aquaController.createAqua(aqua);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aqua, response.getBody());
    }

    @Test
    public void testCreateAquaFailed() throws Exception {
        Aqua aqua = new Aqua(0, "test1", this.flavors, PackSize.SIX, 0);

        when(mockDao.createAqua("test1", this.flavors, PackSize.SIX, 0)).thenReturn(null);

        ResponseEntity<Aqua> response = aquaController.createAqua(aqua);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCreateAquaException() throws IOException {
        Aqua aqua = new Aqua(0, "test1", this.flavors, PackSize.SIX, 0);
        doThrow(new IOException()).when(mockDao).createAqua("test1", this.flavors, PackSize.SIX, 0);

        ResponseEntity<Aqua> response = aquaController.createAqua(aqua);
    }

    @Test
    public void testUpdateAqua() throws IOException {
        Aqua aqua = new Aqua(0, "test1", this.flavors, PackSize.SIX, 0);

        when(mockDao.updateAqua(0, "updated name", flavors, PackSize.SIX)).thenReturn(aqua);
        ResponseEntity<Aqua> response = aquaController.updateAqua(aqua);
        aqua.setName("updated name");

        response = aquaController.updateAqua(aqua); 

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aqua, response.getBody());
    }

    @Test
    public void testUpdateAquaFailed() throws IOException {
        Aqua aqua = new Aqua(0, "test1", flavors, PackSize.SIX, 0);

        when(mockDao.updateAqua(0, "updated name", flavors, PackSize.SIX)).thenReturn(null);

        ResponseEntity<Aqua> response = aquaController.updateAqua(aqua);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateAquaException() throws IOException {
        Aqua aqua = new Aqua(0, "test1", flavors, PackSize.SIX, 0);

        doThrow(new IOException()).when(mockDao).updateAqua(0, "test1", flavors, PackSize.SIX);

        ResponseEntity<Aqua> response = aquaController.updateAqua(aqua);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testGetAqua() throws IOException {
        Aqua aqua = new Aqua(0, "test1", flavors, PackSize.SIX, 0);
        when(mockDao.getAqua(aqua.getId())).thenReturn(aqua);

        ResponseEntity<Aqua> response = aquaController.getAquaById(aqua.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aqua, response.getBody());
    }

    @Test
    public void testGetAquaNotFound() throws Exception {
        int aquaId = 999;
        when(mockDao.getAqua(aquaId)).thenReturn(null);
        ResponseEntity<Aqua> response = aquaController.getAquaById(aquaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAquaException() throws IOException {
        int aquaId = 0;
        doThrow(new IOException()).when(mockDao).getAqua(aquaId);

        ResponseEntity<Aqua> response = aquaController.getAquaById(aquaId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
 
    @Test
    public void testDeleteAqua() throws IOException {
        int aquaId = 999;
        when(mockDao.deleteAqua(aquaId)).thenReturn(true);

        ResponseEntity response = aquaController.deleteAquaById(aquaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteAquaNotFound() throws IOException {
        int aquaId = 999;
        when(mockDao.deleteAqua(aquaId)).thenReturn(false);

        ResponseEntity response = aquaController.deleteAquaById(aquaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteAquaHandleException() throws IOException {
        int aquaId = 999;

        doThrow(new IOException()).when(mockDao).deleteAqua(aquaId);

        ResponseEntity response = aquaController.deleteAquaById(aquaId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchAquaByName() throws IOException {
        String target = "st";
        Aqua[] aquas = new Aqua[1];
        aquas[0] = new Aqua(0, "test1", flavors, PackSize.SIX, 0);

        when(mockDao.searchInventory(target)).thenReturn(aquas);

        ResponseEntity<Aqua[]> response = aquaController.searchAquasByName(target);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aquas, response.getBody());
    }

    @Test
    public void testSearchAquaByNameError() throws IOException {
        String target = "gh";

        when(mockDao.searchInventory(target)).thenReturn(null);

        ResponseEntity<Aqua[]> response = aquaController.searchAquasByName(target);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchAquaByNameHandleException() throws IOException {
        String target = "gh";

        doThrow(new IOException()).when(mockDao).searchInventory(target);

        ResponseEntity<Aqua[]> response = aquaController.searchAquasByName(target);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testSearchAquaByFlavor() throws IOException {
        Flavor target = flavors.get(0);
        Aqua[] aquas = new Aqua[1];
        aquas[0] = new Aqua(0,"test", flavors, PackSize.SIX, 0);

        when(mockDao.searchInventory(target)).thenReturn(aquas);

        ResponseEntity<Aqua[]> response = aquaController.searchAquas(target);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aquas, response.getBody());
    }

    @Test
    public void testSearchAquaByFlavor404() throws IOException {
        Flavor target = flavors.get(0);
        Aqua[] aquas = new Aqua[1];
        aquas[0] = new Aqua(0,"test",flavors,PackSize.SIX,0);

        when(mockDao.searchInventory(target)).thenReturn(null);

        ResponseEntity<Aqua[]> response = aquaController.searchAquas(target);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    } 

    @Test
    public void testSearchAquaByFlavorHandleException() throws IOException {
        Flavor target = new Flavor(999, "forbidden flavor", "the mysterious forbidden flavor", 10);

        doThrow(new IOException()).when(mockDao).searchInventory(target);

        ResponseEntity<Aqua[]> response = aquaController.searchAquas(target);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}