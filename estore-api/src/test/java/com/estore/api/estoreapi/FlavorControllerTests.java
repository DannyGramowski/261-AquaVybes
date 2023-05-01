package com.estore.api.estoreapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.estore.api.estoreapi.controller.FlavorController;
import com.estore.api.estoreapi.model.flavor.Flavor;
import com.estore.api.estoreapi.persistence.flavor.FlavorFileDAO;

/**
 * tests for FlavorController
 * @author gsh3009
 */
@Tag("Controller-tier")
public class FlavorControllerTests {
    private FlavorController flavorController;
    private FlavorFileDAO mockDao;

    /**
     * set up FlavorController and mock DAO
     */
    @BeforeEach
    public void setUpFlavorController() {
        mockDao = mock(FlavorFileDAO.class);
        flavorController = new FlavorController(mockDao);
    }

    @Test
    public void testGetFlavors() {
        Flavor[] flavors = new Flavor[2];
        flavors[0] = new Flavor(0, "new flavor", "i'm tired", 10);
        flavors[1] = new Flavor(1, "Berry", "A berry good taste!", 10);

        when(mockDao.getAllFlavors()).thenReturn(flavors);

        ResponseEntity<Flavor[]> response = flavorController.getFlavors();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flavors, response.getBody());
    }

    @Test
    public void testSearchFlavor() {
        String target = "rr";
        Flavor[] flavors = new Flavor[2];
        flavors[0] = new Flavor(0, "test1", "test", 10);
        flavors[1] = new Flavor(1, "berry", "beryr", 10);

        when(mockDao.searchFlavors(target)).thenReturn(flavors);

        ResponseEntity<Flavor[]> response = flavorController.searchFlavors(target);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flavors, response.getBody());
    }

    public void testGetFlavorById() throws IOException {
        Flavor flavor = new Flavor(0, "test", "test desc", 10);

        when(mockDao.getFlavor(flavor.getId())).thenReturn(flavor);

        ResponseEntity<Flavor> response = flavorController.getFlavorById(flavor.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flavor, response.getBody());
    }

    @Test
    public void testGetFlavorId404() throws IOException {
        int flavorId = 999;

        when(mockDao.getFlavor(flavorId)).thenReturn(null);

        ResponseEntity<Flavor> response = flavorController.getFlavorById(flavorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetFlavorIdException() throws IOException {
        int flavorId = 999;

        doThrow(new IOException()).when(mockDao).getFlavor(flavorId);

        ResponseEntity<Flavor> response = flavorController.getFlavorById(flavorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testCreateFlavor() {
        Flavor flavor = new Flavor(0, "new flavor", "new desc", 10);

        when(mockDao.createFlavor("new flavor", "new desc", 10)).thenReturn(flavor);

        ResponseEntity<Flavor> response = flavorController.createFlavor(flavor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flavor, response.getBody());
    }

    @Test
    public void testCreateFlavorFailed() {
        Flavor flavor = new Flavor(0, "flavor", "desc", 10);

        when(mockDao.createFlavor("new", "desc", 10)).thenReturn(null);

        ResponseEntity<Flavor> response = flavorController.createFlavor(flavor);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testDeleteFlavor() throws IOException {
        int flavorId = 999;
        when(mockDao.deleteFlavor(flavorId)).thenReturn(true);

        ResponseEntity response = flavorController.deleteFlavorById(flavorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testDeleteFlavor404() throws IOException {
        int flavorId = 999;
        when(mockDao.deleteFlavor(flavorId)).thenReturn(false);

        ResponseEntity response = flavorController.deleteFlavorById(flavorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteFlavorException() throws IOException {
        int flavorId = 999;
        doThrow(new IOException()).when(mockDao).deleteFlavor(flavorId);

        ResponseEntity response = flavorController.deleteFlavorById(flavorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testUpdateFlavor() throws IOException {
        Flavor flavor = new Flavor(0, "new", "flavor", 10);

        when(mockDao.updateFlavor(0, "updated", "flavor", 10)).thenReturn(flavor);
        ResponseEntity<Flavor> response = flavorController.updateFlavor(flavor);
        flavor.setName("updated");

        response = flavorController.updateFlavor(flavor);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(flavor, response.getBody());
    }

    @Test
    public void testUpdateFlavor404() throws IOException {
        Flavor flavor = new Flavor(0, "new", "flavor", 10);
        when(mockDao.updateFlavor(0, "updated", null, 10)).thenReturn(null);
        ResponseEntity<Flavor> response = flavorController.updateFlavor(flavor);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testUpdateFlavorException() throws IOException {
        Flavor flavor = new Flavor(0, "g", "h", 10);
        doThrow(new IOException()).when(mockDao).updateFlavor(0, "updated", null, 10);
        ResponseEntity<Flavor> response = flavorController.updateFlavor(flavor);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}
