package com.epam.lab.spider.model.db.dao.savable;

import com.epam.lab.spider.controller.utils.validation.ValidateResult;
import com.epam.lab.spider.controller.utils.validation.Validator;
import com.epam.lab.spider.model.db.dao.CRUD;
import com.epam.lab.spider.model.db.dao.mysql.BaseDAO;
import com.epam.lab.spider.model.db.dao.mysql.DAOFactory;
import com.epam.lab.spider.model.db.dao.savable.exception.InvalidEntityException;
import com.epam.lab.spider.model.db.dao.savable.exception.ResolvableDAOException;
import com.epam.lab.spider.model.db.dao.savable.exception.UnsupportedDAOException;
import com.epam.lab.spider.model.db.service.savable.exception.SavableTransactionException;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shell on 6/18/2015.
 */
public class SavableCRUDUtil {
    public static final boolean VALIDATION_SUPPORT = true;
    private static Map<Class,Method> idFieldFromClassMap = new HashMap<>();

    private static Integer getId(Object entity) throws InvalidEntityException {
        Class  clazz = entity.getClass();
        Method method = idFieldFromClassMap.get(clazz);
        try {
            if(method ==null){
                method = clazz.getDeclaredMethod("getId");
                idFieldFromClassMap.put(clazz,method);
            }
            Object result =  method.invoke(entity);
            return (Integer) result;

        } catch (NoSuchMethodException|ClassCastException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new InvalidEntityException("Can not find ID!");
    }
    public static boolean  safeSave(Connection conn,Object entity,BaseDAO dao){
        try {
            return save(conn,entity,dao);
        } catch (InvalidEntityException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedDAOException e) {
            e.printStackTrace();
            return false;
        } catch (ResolvableDAOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean save(Connection conn,Object entity, BaseDAO dao) throws UnsupportedDAOException, ResolvableDAOException, InvalidEntityException {
        if (entity == null) return false;
        if (VALIDATION_SUPPORT) {
            Validator validator = new Validator();
            ValidateResult validateResult = validator.valideteWithResult(entity);
            if(!validateResult.isValid()){
                InvalidEntityException exception = new InvalidEntityException("Invalid entity");
                exception.setValidateResult(validateResult);
                throw exception;
            }
        }
        CRUD crudDAO;
        try{
            crudDAO = (CRUD) dao;
        }catch (ClassCastException x){
            throw new UnsupportedDAOException(""+dao.getClass().getName()+" must implement "+CRUD.class.getName(),x);
        }

        if (!(dao instanceof SavableDAO)) {
            throw new UnsupportedDAOException("" + crudDAO.getClass().getName() + " must implements " + SavableDAO.class.getName());
        }
        boolean result ;
        try{
            Integer index = getId(entity);
            //if(entity.id!=null)
            if (index != null) {
                return crudDAO.update(conn, index, entity);
            } else {
                result = crudDAO.insert(conn, entity);
                if(!result)throw new SavableTransactionException();
                return result;
            }
        }catch (SQLException x){
            throw new ResolvableDAOException(x);
        }
    }



    public static boolean saveFromInterface(java.sql.Connection conn,Object entity) throws UnsupportedDAOException, ResolvableDAOException, InvalidEntityException{
        BaseDAO dao = (BaseDAO) DAOFactory.getInstance().getCrudDAOByEntity(entity.getClass());
        return SavableCRUDUtil.save(conn,entity, dao);
    }

}
