package com.epam.lab.spider.model.db.service;

import com.epam.lab.spider.model.entity.AbstractEntityFactory;
import com.epam.lab.spider.model.entity.Owner;
import com.epam.lab.spider.model.entity.impl.BasicEntityFactory;
import com.epam.lab.spider.persistence.service.OwnerService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
/**
 * @author Oleksandra Lobanok
 */
public class OwnerServiceTest {
    public static final AbstractEntityFactory ENTITY_FACTORY = BasicEntityFactory.getSynchronized();
    Owner owner;
    OwnerService os = new OwnerService();

    @Before
    public void setUp() throws Exception {
        owner = ENTITY_FACTORY.createOwner();
        owner.setVkId(1);
        owner.setName("TestName");
        owner.setDomain("TestDomain");
        owner.setUserId(1);
    }

    @Test
    public void testInsert() throws Exception {
        Boolean check = os.insert(owner);
        assertTrue(check);
    }

    @Test
    public void testUpdate() throws Exception {
        Owner newOwner = ENTITY_FACTORY.createOwner();
        newOwner.setVkId(2);
        newOwner.setName("NewTestName");
        newOwner.setDomain("NewTestDomain");
        newOwner.setUserId(1);
        Boolean check = os.update(1, newOwner);
        assertTrue(check);
    }

    @Test
    public void testDelete() throws Exception {
        os.delete(5);
        Owner deletedOwner = os.getById(5);
        assertNull(deletedOwner);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Owner> owners = os.getAll();
        assertTrue(owners.size() > 0);
    }

    @Test
    public void testGetLimited() throws Exception {
        List<Owner> owners = os.getLimited(2, 4);
        assertTrue(owners.size() > 0);
    }

    @Test
    public void testGetById() throws Exception {
        Owner newOwner = os.getById(1);
        assertNotNull(newOwner);
    }

    @Test
    public void testGetByVkId() throws Exception {
        Owner newOwner = os.getByVkId(1);
        assertNotNull(newOwner);
    }

    @Test
    public void testGetByUserId() throws Exception {
        List<Owner> owners = os.getByUserId(1);
        assertTrue(owners.size() > 0);
    }

    @Test
    public void testGetCountByUserId() throws Exception {
        Integer count = os.getCountByUserId(1);
        assertNotNull(count);
    }

    @Test
    public void testGetCountAllUnique() throws Exception {
        Integer count = os.getCountAllUnique();
        assertNotNull(count);
    }
}